package com.ark.data.repo

import com.ark.core.domain.ApiResponse
import com.ark.core.domain.DataError
import com.ark.domain.model.ProductCatalog
import com.ark.domain.model.SearchFilter

internal interface CatalogProvider {
    suspend fun fetchProductCatalog(
        query: String,
        page: Int,
        filter: SearchFilter
    ): ApiResponse<List<ProductCatalog>, DataError>
}