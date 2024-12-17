package com.ark.data.remote.amazon

import com.ark.core.data.HttpClientUtils.getRandomUserAgent


internal fun getAmazonHeaders(): Map<String, String> {
    return mapOf(
        "User-Agent" to getRandomUserAgent(),
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8",
        "Accept-Language" to "en-GB,en;q=0.5",
        "Priority" to "u=0, i",
        "Referer" to "https://www.amazon.in/",
        "Sec-CH-UA" to "\"Brave\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"",
        "Sec-CH-UA-Platform" to "\"Windows\"",
        "Sec-CH-UA-Platform-Version" to "\"11.0\"",
        "Sec-Fetch-Dest" to "document",
        "Sec-Fetch-Mode" to "navigate",
        "Sec-Fetch-Site" to "same-origin",
        "Sec-Fetch-User" to "?1",
        "Sec-GPC" to "1",
        "Upgrade-Insecure-Requests" to "1"
    )
}