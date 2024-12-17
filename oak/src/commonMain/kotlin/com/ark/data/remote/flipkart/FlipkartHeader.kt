package com.ark.data.remote.flipkart

import com.ark.core.data.HttpClientUtils.getRandomUserAgent

internal fun getFlipkartHeaders(): Map<String, String> {
    val userAgent = getRandomUserAgent()
    return mapOf(
        "User-Agent" to userAgent,
        "X-User-Agent" to "$userAgent FKUA/website/42/website/Desktop",
        "Content-Type" to "application/json",
        "Accept" to "*/*",
        "Accept-Language" to "en-GB,en;q=0.5",
        "Connection" to "keep-alive",
        "Referer" to "https://www.flipkart.com/",
        "Origin" to "https://www.flipkart.com",
        "Sec-Fetch-Dest" to "empty",
        "Sec-Fetch-Mode" to "cors",
        "Sec-Fetch-Site" to "same-site",
        "Sec-GPC" to "1",
        "sec-ch-ua" to "\"Brave\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"",
        "sec-ch-ua-platform" to "\"Windows\""
    )
}