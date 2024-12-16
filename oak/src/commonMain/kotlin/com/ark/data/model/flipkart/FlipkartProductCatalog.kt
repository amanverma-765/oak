package com.ark.data.model.flipkart

import com.ark.core.utils.ProductIdTransformer.OriginalId
import kotlinx.serialization.Serializable

@Serializable
internal data class FlipkartProductCatalog(
    val id: OriginalId,
    val productUrl: String,
    val imgUrl: String,
    val title: String,
    val mrp: Int,
    val displayPrice: Int?,
    val discountPercent: Float?,
    val rating: Float?,
    val ratingCount: Int?,
    val reviewCount: Int?,
    val keySpecs: List<String?>,
    val minKeySpecs: List<String?>
)