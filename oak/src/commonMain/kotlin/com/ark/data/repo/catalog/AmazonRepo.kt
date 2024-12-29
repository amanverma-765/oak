package com.ark.data.repo.catalog

import com.ark.core.data.mapSuccess
import com.ark.core.domain.ApiResponse
import com.ark.core.domain.DataError
import com.ark.data.mapper.ProductCatalogMapper.toMainProductCatalog
import com.ark.data.mapper.SearchFilterMapper.toAmazonSearchFilter
import com.ark.data.mapper.SearchFilterMapper.toFlipkartSearchFilter
import com.ark.data.remote.amazon.AmazonCatalogProvider
import com.ark.data.repo.CatalogProvider
import com.ark.domain.model.ProductCatalog
import com.ark.domain.model.SearchFilter

internal class AmazonRepo(
    private val amazonCatalogProvider: AmazonCatalogProvider
) : CatalogProvider {
    override suspend fun fetchProductCatalog(
        query: String,
        page: Int,
        filter: SearchFilter
    ): ApiResponse<List<ProductCatalog>, DataError> {
        return amazonCatalogProvider.fetchProductCatalog(
            query = query,
            page = page,
            filter = filter.toAmazonSearchFilter()
        ).mapSuccess { catalog -> catalog.map { it.toMainProductCatalog() } }
    }
}