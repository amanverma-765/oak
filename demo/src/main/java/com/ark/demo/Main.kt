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
        query = "blue lehanga",
        page = 1,
        filter = SearchFilter.DISCOUNT_DESCENDING,
        marketPlaces = listOf(MarketPlace.FLIPKART, MarketPlace.AMAZON)
    )

    when (resp) {
        is ApiResponse.Error -> Unit
        is ApiResponse.Success -> {
            resp.data.forEach {
                println("MRP: ${it.mrp} PRICE: ${it.displayPrice} DISCPER: ${it.discountPercent} DISC: ${it.discount} URl: ${it.productUrl}")
            }
        }
    }

}