package com.ark.data.remote.flipkart

import com.ark.core.data.HttpClientUtils.getFlipkartHeaders
import com.ark.core.data.safeCall
import com.ark.core.domain.ApiResponse
import com.ark.core.domain.DataError
import com.ark.core.domain.onError
import com.ark.core.domain.onSuccess
import com.ark.core.utils.Constants
import com.ark.data.model.flipkart.FlipkartSearchRequest
import com.ark.domain.model.ProductCatalog
import com.ark.domain.model.SearchFilter
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class FlipkartCatalogProvider(
    private val httpClient: HttpClient
) {

    internal suspend fun fetchProductCatalog(
        query: String,
        page: Int,
        filter: SearchFilter,
    ): ApiResponse<List<ProductCatalog>, DataError.Remote> {
        return try {

            val searchString =
                "/search?q=${
                    query.replace(" ", "+")
                }&otracker=search&otracker1=search&marketplace=FLIPKART&as-show=on&as=off&sort=relevance"

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

            response.onSuccess { data ->
                println(data)
            }.onError {
                println(it)
            }

            ApiResponse.Error(DataError.Remote.UNKNOWN_ERROR)
        } catch (ex: Exception) {
            ApiResponse.Error(DataError.Remote.UNKNOWN_ERROR)
        }
    }

}