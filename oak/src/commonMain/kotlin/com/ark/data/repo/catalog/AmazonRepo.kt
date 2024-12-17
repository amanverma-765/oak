package com.ark.data.repo.catalog

import com.ark.core.domain.ApiResponse
import com.ark.core.domain.DataError
import com.ark.data.repo.CatalogProvider
import com.ark.domain.model.ProductCatalog
import com.ark.domain.model.SearchFilter

internal class AmazonRepo: CatalogProvider {
    override suspend fun fetchProductCatalog(
        query: String,
        page: Int,
        filter: SearchFilter
    ): ApiResponse<List<ProductCatalog>, DataError> {
        TODO("Not yet implemented")
    }
}