package com.ark.data.model.flipkart

import com.ark.core.utils.ProductIdTransformer.OriginalId
import kotlinx.serialization.Serializable

@Serializable
internal data class FlipkartProductCatalog(
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
    val reviewCount: Int?,
    val keySpecs: List<String>,
    val discount: Float?
)