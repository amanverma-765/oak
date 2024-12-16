package com.ark.data.model.flipkart

import kotlinx.serialization.Serializable

@Serializable
data class FlipkartSearchRequest(
    val pageUri: String,
    val pageContext: PageContext,
    val requestContext: RequestContext
) {
    @Serializable
    data class PageContext(
        val fetchSeoData: Boolean,
        val paginatedFetch: Boolean,
        val pageNumber: Int
    )

    @Serializable
    data class RequestContext(
        val type: String,
        val ssid: String,
        val sqid: String
    )
}