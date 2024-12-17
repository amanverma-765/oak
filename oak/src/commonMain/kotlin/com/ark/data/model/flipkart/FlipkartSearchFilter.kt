package com.ark.data.model.flipkart

enum class FlipkartSearchFilter(val filterString: String) {
    PRICE_ASCENDING("price_asc"),        // Price Low to High
    PRICE_DESCENDING("price_desc"),      // Price High to Low
    FEATURED("relevance"),               // Featured by Platform
    POPULARITY_DESCENDING("popularity"), // Most Popular
    LATEST_ARRIVAL("recency_desc"),      // Latest Products
}