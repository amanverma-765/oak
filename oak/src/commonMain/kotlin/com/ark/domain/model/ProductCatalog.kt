package com.ark.domain.model

import com.ark.core.utils.ProductIdTransformer

data class ProductCatalog(
    val marketPlace: MarketPlace,
    var id: ProductIdTransformer.MarketplaceId,
    val productUrl: String,
    val imgUrl: String,
    val title: String,
    val mrp: Int,
    val displayPrice: Int?,
    val discountPercent: Float?,
    val rating: Float?,
    val ratingCount: Int?
)