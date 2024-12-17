package com.ark.api

import com.ark.core.domain.ApiResponse
import com.ark.core.domain.DataError
import com.ark.domain.model.MarketPlace
import com.ark.domain.model.ProductCatalog
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
        marketPlaces: List<MarketPlace>
    ): ApiResponse<List<ProductCatalog>, DataError> {
        return productCatalogUseCase.fetchProductCatalog(
            query = query,
            page = page,
            filter = filter,
            marketPlaces = marketPlaces
        )
    }
}