package com.ark.data.model.flipkart

import kotlinx.serialization.Serializable

@Serializable
internal data class FlipkartSearchRequest(
    val pageUri: String,
    val pageContext: PageContext,
    val requestContext: RequestContext
) {
    @Serializable
    internal data class PageContext(
        val fetchSeoData: Boolean,
        val paginatedFetch: Boolean,
        val pageNumber: Int
    )

    @Serializable
    internal data class RequestContext(
        val type: String,
        val ssid: String,
        val sqid: String
    )
}