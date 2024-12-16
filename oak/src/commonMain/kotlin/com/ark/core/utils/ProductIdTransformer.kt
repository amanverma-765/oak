package com.ark.core.utils

import com.ark.domain.model.MarketPlace
import kotlinx.serialization.Serializable


object ProductIdTransformer {

    @Serializable
    class OriginalId private constructor(private val value: String) {
        companion object {
            private val regex = Regex("^[A-Za-z0-9]+$") // Alphanumeric characters

            fun create(value: String): OriginalId {
                require(value.isNotBlank()) { "OriginalId cannot be blank." }
                require(value.matches(regex)) { "OriginalId must contain only alphanumeric characters." }
                return OriginalId(value)
            }
        }

        fun toMarketplaceId(marketPlace: MarketPlace): MarketplaceId {
            val marketplaceId = "${marketPlace.prefix}-$value"
            return MarketplaceId.create(marketplaceId)
        }

        override fun toString(): String {
            return value
        }
    }

    @Serializable
    class MarketplaceId private constructor(private val value: String) {
        companion object {
            private val regex =
                Regex("^[A-Za-z]+-[A-Za-z0-9]+$") // Any valid prefix followed by alphanumeric characters

            fun create(value: String): MarketplaceId {
                require(value.isNotBlank()) { "MarketplaceId cannot be blank." }
                require(value.matches(regex)) { "MarketplaceId must be in the format PREFIX-OriginalID (e.g., AMZ-B0CJJ9H4XK)." }

                val prefix = value.substringBefore("-")
                require(MarketPlace.entries.any { it.prefix == prefix }) {
                    "Invalid marketplace prefix '$prefix'."
                }

                return MarketplaceId(value)
            }
        }

        fun toOriginalId(): OriginalId {
            val prefix = value.substringBefore("-")
            require(value.startsWith("$prefix-")) { "ID '$value' does not match the expected format." }
            return OriginalId.create(value.removePrefix("$prefix-"))
        }

        override fun toString(): String {
            return value
        }
    }
}