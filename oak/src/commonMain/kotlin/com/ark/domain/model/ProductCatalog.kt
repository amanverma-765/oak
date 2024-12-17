package com.ark.domain.model

import com.ark.core.utils.ProductIdTransformer

data class ProductCatalog(
    val marketPlace: MarketPlace,
    var id: ProductIdTransformer.MarketplaceId,
    val productUrl: String,
    val imgUrl: String,
    val title: String,
    val mrp: Float?,
    val displayPrice: Float?,
    val discountPercent: Float?,
    val discount: Float?,
    val rating: Float?,
    val ratingCount: Int?
)