package com.ark.domain.repo

import com.ark.core.domain.ApiResponse
import com.ark.core.domain.DataError
import com.ark.domain.model.MarketPlace
import com.ark.domain.model.ProductCatalog
import com.ark.domain.model.SearchFilter


internal interface ProductCatalogRepo {

    suspend fun fetchProductCatalog(
        query: String,
        page: Int,
        filter: SearchFilter,
        marketPlaces: Set<MarketPlace>
    ): ApiResponse<List<ProductCatalog>, DataError>

}