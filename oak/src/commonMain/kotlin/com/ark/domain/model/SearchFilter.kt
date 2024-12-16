package com.ark.domain.model

enum class SearchFilter {
    PRICE_ASCENDING,       // Price Low to High
    PRICE_DESCENDING,      // Price High to Low
    FEATURED,              // Featured by Platform
    POPULARITY_DESCENDING, // Most Popular
    LATEST_ARRIVAL,        // Latest Products
    RATING_DESCENDING,     // Highest Rated
    DISCOUNT_DESCENDING    // Highest Discounted
}