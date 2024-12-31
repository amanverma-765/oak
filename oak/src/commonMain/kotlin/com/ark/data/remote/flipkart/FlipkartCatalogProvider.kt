package com.ark.data.remote.flipkart

import co.touchlab.kermit.Logger
import com.ark.core.data.safeCall
import com.ark.core.domain.ApiResponse
import com.ark.core.domain.DataError
import com.ark.core.utils.Constants
import com.ark.core.utils.ProductIdTransformer
import com.ark.data.model.flipkart.FlipkartProductCatalog
import com.ark.data.model.flipkart.FlipkartSearchFilter
import com.ark.data.model.flipkart.FlipkartSearchRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.math.roundToInt

internal class FlipkartCatalogProvider(
    private val httpClient: HttpClient
) {

    internal suspend fun fetchProductCatalog(
        query: String,
        page: Int,
        filter: FlipkartSearchFilter
    ): ApiResponse<List<FlipkartProductCatalog>, DataError.Remote> {
        try {

            val searchString =
                "/search?q=${
                    query.replace(" ", "+")
                }&otracker=search&otracker1=search&marketplace=FLIPKART&as-show=on&as=off&sort=${filter.filterString}"

            val body = FlipkartSearchRequest(
                pageUri = searchString,
                pageContext = FlipkartSearchRequest.PageContext(
                    fetchSeoData = false,
                    paginatedFetch = false,
                    pageNumber = page
                ),
                requestContext = FlipkartSearchRequest.RequestContext(
                    type = "BROWSE_PAGE",
                    ssid = "",
                    sqid = ""
                )
            )

            val headers = getFlipkartHeaders()

            val response = safeCall<String> {
                httpClient.post(Constants.FLIPKART_SEARCH_URL) {
                    headers.forEach { (key, value) -> header(key, value) }
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }
            }

            return when (response) {
                is ApiResponse.Error -> ApiResponse.Error(response.error)
                is ApiResponse.Success -> {
                    ApiResponse.Success(parseCatalogResponse(response.data))
                }
            }

        } catch (ex: Exception) {
            Logger.e("Failed to fetch flipkart product catalog", ex)
            return ApiResponse.Error(
                DataError.Remote.UnknownError(
                    ex.message ?: "Failed to fetch flipkart catalog"
                )
            )
        }
    }

    private fun parseCatalogResponse(response: String): List<FlipkartProductCatalog> {
        try {

            val searchResponseJson = Json.parseToJsonElement(response).jsonObject
            val slots = searchResponseJson["RESPONSE"]
                ?.jsonObject
                ?.get("slots")
                ?.jsonArray

            return slots?.asSequence()
                ?.filter { slot ->
                    slot.jsonObject["widget"]
                        ?.jsonObject
                        ?.get("type")
                        ?.jsonPrimitive
                        ?.contentOrNull == "PRODUCT_SUMMARY"
                }
                ?.flatMap { slot ->
                    val productInfoArray = slot.jsonObject["widget"]
                        ?.jsonObject
                        ?.get("data")
                        ?.jsonObject
                        ?.get("products")
                        ?.jsonArray

                    productInfoArray?.mapNotNull { productInfoElement ->
                        val productInfo = productInfoElement.jsonObject["productInfo"]
                            ?.jsonObject
                            ?.get("value")

                        if (productInfo != null) {
                            try {
                                parseProductCatalog(productInfo)
                            } catch (e: Exception) {
                                Logger.e("Error parsing product info: $productInfo", e)
                                null
                            }
                        } else {
                            Logger.e("Missing productInfo")
                            null
                        }
                    } ?: emptyList()
                }
                ?.toList()
                ?: emptyList()

        } catch (ex: Exception) {
            Logger.e("Error while parsing Flipkart response", ex)
            return emptyList()
        }
    }

    private fun parseProductCatalog(productResponse: JsonElement): FlipkartProductCatalog? {
        try {

            // Extracting product Id
            val originalId = ProductIdTransformer.OriginalId.create(
                productResponse.jsonObject["id"]?.jsonPrimitive?.contentOrNull
                    ?: run {
                        Logger.e("Flipkart product id not found")
                        return null
                    }
            )

            // Extracting image URL
            val imageUrl =
                productResponse.jsonObject["media"]?.jsonObject?.get("images")?.jsonArray
                    ?.firstOrNull()?.jsonObject?.get("url")?.jsonPrimitive?.contentOrNull
                    ?.replace("{@width}", flipkartImageSpecs(ScreenType.LIST).width)
                    ?.replace("{@height}", flipkartImageSpecs(ScreenType.LIST).height)
                    ?.replace("?q={@quality}", "")
                    ?: run {
                        Logger.e("Flipkart product image not found")
                        return null
                    }

            // Extracting title
            val title =
                productResponse.jsonObject["titles"]?.jsonObject?.get("title")?.jsonPrimitive?.contentOrNull
                    ?: run {
                        Logger.e("Flipkart product title not found")
                        return null
                    }

            // Extracting rating
            val rating =
                productResponse.jsonObject["rating"]?.jsonObject?.get("average")?.jsonPrimitive?.floatOrNull

            // Extracting rating count
            val ratingCount =
                productResponse.jsonObject["rating"]?.jsonObject?.get("count")?.jsonPrimitive?.intOrNull

            // Extracting rating
            val reviewCount =
                productResponse.jsonObject["rating"]?.jsonObject?.get("reviewCount")?.jsonPrimitive?.intOrNull

            // Extracting product URL
            val productUrl =
                "https://www.flipkart.com" + (productResponse.jsonObject["baseUrl"]?.jsonPrimitive?.contentOrNull)

            // Extracting key specs
            val keySpecs =
                productResponse.jsonObject["keySpecs"]?.jsonArray?.mapNotNull { it.jsonPrimitive.contentOrNull }
                    ?: emptyList()

            // Extracting availability
            val availability =
                productResponse.jsonObject["availability"]?.jsonObject?.get("displayState")?.jsonPrimitive?.contentOrNull

            // Extracting mrp
            val mrp =
                productResponse.jsonObject["pricing"]?.jsonObject?.get("mrp")?.jsonObject?.get("decimalValue")?.jsonPrimitive?.floatOrNull

            // Extracting price
            val displayPrice =
                productResponse.jsonObject["pricing"]?.jsonObject?.get("finalPrice")?.jsonObject?.get("decimalValue")?.jsonPrimitive?.floatOrNull

            // Calculating discount
            val discount = if (mrp != null && displayPrice != null && mrp > displayPrice) {
                mrp - displayPrice
            } else null

            // Calculating discount percent
            val discountPercent = discount?.let {
                val calculatedDiscountPercent = (it / mrp!!) * 100
                ((calculatedDiscountPercent * 100).roundToInt() / 100.0).toFloat()
            }

            return FlipkartProductCatalog(
                title = title,
                rating = rating,
                ratingCount = ratingCount,
                productUrl = productUrl,
                id = originalId,
                keySpecs = keySpecs,
                availability = availability,
                imgUrl = imageUrl,
                mrp = mrp,
                displayPrice = displayPrice,
                discountPercent = discountPercent,
                discount = discount,
                reviewCount = reviewCount
            )

        } catch (ex: Exception) {
            Logger.e("Error while parsing Flipkart product catalog", ex)
            return null
        }
    }

}