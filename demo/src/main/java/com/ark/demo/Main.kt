package com.ark.demo

import com.ark.api.OakCatalogManager
import com.ark.domain.model.MarketPlace
import com.ark.domain.model.SearchFilter
import com.ark.koin.OakInitialiser


suspend fun main() {

    OakInitialiser.initOak()

    val oakCatalogManager = OakCatalogManager()

    oakCatalogManager.fetchCatalog(
        query = "iphone",
        page = 1,
        filter = SearchFilter.FEATURED,
        marketPlace = MarketPlace.FLIPKART
    )

}