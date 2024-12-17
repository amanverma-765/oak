package com.ark.core.data

import kotlin.random.Random

internal object HttpClientUtils {

    internal fun getRandomUserAgent(): String {
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