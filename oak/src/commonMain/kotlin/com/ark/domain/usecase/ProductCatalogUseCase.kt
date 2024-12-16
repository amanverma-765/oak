package com.ark.domain.usecase

import com.ark.core.domain.ApiResponse
import com.ark.core.domain.DataError
import com.ark.domain.model.MarketPlace
import com.ark.domain.model.ProductCatalog
import com.ark.domain.model.SearchFilter
import com.ark.domain.repo.ProductCatalogRepo


internal class ProductCatalogUseCase(
    private val productCatalogRepo: ProductCatalogRepo
) {
    internal suspend fun fetchProductCatalog(
        query: String,
        page: Int,
        filter: SearchFilter,
        marketPlace: MarketPlace
    ): ApiResponse<List<ProductCatalog>, DataError.Remote> {
        return productCatalogRepo.fetchProductCatalog(
            query = query,
            page = page,
            filter = filter,
            marketPlace = marketPlace
        )
    }
}