package com.ark.core.utils


internal expect fun getPlatform(): Platform

internal enum class Platform {
    ANDROID,
    IOS,
    DESKTOP
}