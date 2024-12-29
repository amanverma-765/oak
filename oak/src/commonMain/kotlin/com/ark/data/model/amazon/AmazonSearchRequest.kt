package com.ark.data.model.amazon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AmazonSearchRequest(
    @SerialName("progressiveScroll")
    val progressiveScroll: String,
    @SerialName("customer-action")
    val customerAction: String
)