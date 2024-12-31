package com.ark.demo

import com.ark.api.OakCatalogManager
import com.ark.core.domain.ApiResponse
import com.ark.domain.model.MarketPlace
import com.ark.domain.model.SearchFilter
import com.ark.koin.OakInitialiser


suspend fun main() {

    OakInitialiser.initOak()
    val oakCatalogManager = OakCatalogManager()

    val resp = oakCatalogManager.fetchCatalog(
        query = "ghaghra chooli",
        page = 1,
        filter = SearchFilter.FEATURED,
        marketPlaces = listOf(MarketPlace.FLIPKART, MarketPlace.AMAZON)
    )

    when (resp) {
        is ApiResponse.Error -> {
            println(resp.error)
        }

        is ApiResponse.Success -> {
            resp.data.forEach {
                println("${it.marketPlace}: ${it.displayPrice}: ${it.productUrl}")
            }
        }
    }

}