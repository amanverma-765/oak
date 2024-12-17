package com.ark.data.remote.flipkart

import com.ark.core.utils.Platform
import com.ark.core.utils.getPlatform


internal enum class ScreenType { DETAIL, LIST }

internal data class ImageSpecs(val width: String, val height: String)

internal fun flipkartImageSpecs(screenType: ScreenType): ImageSpecs {

    val platform = getPlatform()

    return when (platform) {
        Platform.DESKTOP -> when (screenType) {
            ScreenType.DETAIL -> ImageSpecs(width = "700", height = "700")
            ScreenType.LIST -> ImageSpecs(width = "300", height = "300")
        }
        else -> when (screenType) {
            ScreenType.DETAIL -> ImageSpecs(width = "400", height = "400")
            ScreenType.LIST -> ImageSpecs(width = "150", height = "150")
        }
    }
}