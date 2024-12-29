package com.ark.data.remote.amazon

import co.touchlab.kermit.Logger
import com.ark.core.data.safeCall
import com.ark.core.domain.ApiResponse
import com.ark.core.domain.DataError
import com.ark.core.utils.ProductIdTransformer
import com.ark.data.model.amazon.AmazonProductCatalog
import com.ark.data.model.amazon.AmazonSearchFilter
import com.ark.data.model.amazon.AmazonSearchRequest
import com.fleeksoft.ksoup.Ksoup
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.math.roundToInt

internal class AmazonCatalogProvider(
    private val httpClient: HttpClient
) {

    internal suspend fun fetchProductCatalog(
        query: String,
        page: Int,
        filter: AmazonSearchFilter,
    ): ApiResponse<List<AmazonProductCatalog>, DataError.Remote> {
        try {

            val searchUrl =
                "https://www.amazon.in/s/query?k=$query&page=$page&s=${filter.filterString}"

            val body = AmazonSearchRequest(
                progressiveScroll = "true",
                customerAction = "query"
            )

            val headers = getAmazonHeaders()

            val response = safeCall<String> {
                httpClient.post(searchUrl) {
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
            Logger.e("Failed to fetch amazon product catalog", ex)
            return ApiResponse.Error(
                DataError.Remote.UnknownError(
                    ex.message ?: "Failed to fetch amazon catalog"
                )
            )
        }
    }

    private fun parseCatalogResponse(response: String): List<AmazonProductCatalog> {
        try {

            val searchResultJson = "[${response.replace("&&&", ",").trim().removeSuffix(",")}]"

            val productArray = Json.parseToJsonElement(searchResultJson).jsonArray
            val products = mutableListOf<AmazonProductCatalog>()

            productArray.forEach { element ->
                try {
                    val productData = element.jsonArray.getOrNull(2)?.jsonObject
                    productData?.let { item ->
                        val asin = item["asin"]?.jsonPrimitive?.contentOrNull
                        val html = item["html"]?.jsonPrimitive?.contentOrNull

                        if (!asin.isNullOrBlank() && !html.isNullOrBlank()) {
                            val product = parseProductCatalog(asin, html)
                            product?.let {
                                products.add(it)
                            }
                        }
                    }
                } catch (ex: Exception) {
                    println("Error while parsing $element: ${ex.message}")
                }
            }
            return products

        } catch (ex: Exception) {
            Logger.e("Failed to parse amazon response", ex)
            return emptyList()
        }
    }


    private fun parseProductCatalog(asin: String, productHtml: String): AmazonProductCatalog? {
        try {

            val doc = Ksoup.parse(productHtml)

            // Ignore Ads
            if (doc.selectFirst("div.AdHolder") != null) {
                Logger.i("Ads found, skipping product.")
                return null
            }

            // Creating product URL
            val productUrl = "https://www.amazon.in/dp/$asin"

            // Creating product Id
            val originalId = ProductIdTransformer.OriginalId.create(asin)

            // Extracting image URL
            val imageElement = doc.selectFirst("[data-cy=image-container] img")
            val imgUrl = imageElement?.attr("src") ?: run {
                Logger.e("Product image not found")
                return null
            }

            // Extracting title
            val titleElement = doc.selectFirst("[data-cy=title-recipe]")
            val title = run {
                titleElement?.selectFirst("span.a-text-normal")
                    ?: titleElement?.selectFirst("span")
            }?.text() ?: run {
                Logger.e("Product title not found")
                return null
            }

            // Extracting current price and MRP
            val priceTag = doc.select("[data-cy=price-recipe] span.a-price span.a-offscreen")
            val mrp = priceTag.getOrNull(1)?.text()
                ?.replace(",", "")
                ?.replace("₹", "")
                ?.toFloatOrNull()
            val displayPrice = priceTag.getOrNull(0)?.text()
                ?.replace(",", "")
                ?.replace("₹", "")
                ?.toFloatOrNull()


            // Extracting rating and rating count
            val reviewElement = doc.selectFirst("[data-cy=reviews-block] div")
            val ratingTag = reviewElement?.selectFirst("[data-cy=reviews-ratings-slot]")
            val rating = ratingTag?.text()?.let { text ->
                Regex("""\d+(\.\d+)?""").find(text)?.value
            }?.toFloatOrNull()
            val ratingCountElement = reviewElement?.selectFirst("span[aria-hidden=true]")
            val ratingCount = ratingCountElement?.text()?.replace(",", "")?.toIntOrNull()

            // Calculating discount
            val discount = if (mrp != null && displayPrice != null && mrp > displayPrice) {
                mrp - displayPrice
            } else null

            // Calculating discount percent
            val discountPercent = discount?.let {
                val calculatedDiscountPercent = (it / mrp!!) * 100
                ((calculatedDiscountPercent * 100).roundToInt() / 100.0).toFloat()
            }

            return AmazonProductCatalog(
                id = originalId,
                title = title,
                displayPrice = displayPrice,
                mrp = mrp,
                rating = rating,
                ratingCount = ratingCount,
                imgUrl = imgUrl,
                discount = discount,
                availability = "",
                discountPercent = discountPercent,
                productUrl = productUrl
            )

        } catch (ex: Exception) {
            Logger.e("Error while parsing product HTML: ${ex.message}")
            return null
        }
    }

}