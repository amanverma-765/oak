package com.ark.data.model.amazon

import com.ark.core.utils.ProductIdTransformer.OriginalId

internal data class AmazonProductCatalog(
    val id: OriginalId,
    val productUrl: String,
    val imgUrl: String,
    val title: String,
    val mrp: Float?,
    val displayPrice: Float?,
    val discountPercent: Float?,
    val availability: String?,
    val rating: Float?,
    val ratingCount: Int?,
    val discount: Float?
)