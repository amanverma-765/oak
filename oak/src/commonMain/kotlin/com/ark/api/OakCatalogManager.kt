package com.ark.api

import com.ark.domain.model.MarketPlace
import com.ark.domain.model.SearchFilter
import com.ark.domain.usecase.ProductCatalogUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OakCatalogManager: KoinComponent {

    private val productCatalogUseCase: ProductCatalogUseCase by inject()

    suspend fun fetchCatalog(
        query: String,
        page: Int,
        filter: SearchFilter,
        marketPlace: MarketPlace
    ) {
        productCatalogUseCase.fetchProductCatalog(
            query = query,
            page = page,
            filter = filter,
            marketPlace = marketPlace
        )
    }
}