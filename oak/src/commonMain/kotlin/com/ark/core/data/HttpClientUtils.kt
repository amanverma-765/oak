package com.ark.core.data

import kotlin.random.Random

internal object HttpClientUtils {

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

    private fun getRandomUserAgent(): String {
        val osList = listOf(
            "Macintosh; Intel Mac OS X 10_15_7",
            "Macintosh; Intel Mac OS X 13_1",
            "Macintosh; Intel Mac OS X 13_0",
            "Macintosh; Intel Mac OS X 12_6",
            "Macintosh; Intel Mac OS X 12_5",
            "Windows NT 10.0; Win64; x64",
            "Windows NT 10.0; WOW64",
            "Windows NT 11.0; Win64; x64",
            "Windows NT 10.0; Win64; x64",
            "Windows NT 10.0; WOW64",
            "Windows NT 10.0"
        )

        val os = osList.random()
        val chromeVersion = Random.nextInt(100, 104)
        val buildVersion = Random.nextInt(4100, 4290)
        val safariVersion = Random.nextInt(140, 190)

        return "Mozilla/5.0 ($os) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/$chromeVersion.0.$buildVersion.$safariVersion Safari/537.36"
    }
}