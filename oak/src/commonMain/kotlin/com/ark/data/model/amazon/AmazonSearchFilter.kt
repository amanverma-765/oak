package com.ark.data.model.amazon

internal enum class AmazonSearchFilter(val filterString: String) {
    PRICE_ASCENDING("price-asc-rank"),                    // Price Low to High
    PRICE_DESCENDING("price-desc-rank"),                  // Price High to Low
    FEATURED("relevancerank"),                            // Featured by Platform
    POPULARITY_DESCENDING("exact-aware-popularity-rank"), // Most Popular
    LATEST_ARRIVAL("date-desc-rank"),                     // Latest Products
    RATING_DESCENDING("review-rank")                      // Highest Rated
}