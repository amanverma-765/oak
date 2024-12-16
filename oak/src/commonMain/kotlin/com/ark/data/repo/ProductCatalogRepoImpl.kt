package com.ark.data.repo

import com.ark.core.domain.ApiResponse
import com.ark.core.domain.DataError
import com.ark.data.remote.flipkart.FlipkartCatalogProvider
import com.ark.domain.model.MarketPlace
import com.ark.domain.model.ProductCatalog
import com.ark.domain.model.SearchFilter
import com.ark.domain.repo.ProductCatalogRepo

internal class ProductCatalogRepoImpl(
    private val flipkartCatalogProvider: FlipkartCatalogProvider
) : ProductCatalogRepo {

    override suspend fun fetchProductCatalog(
        query: String,
        page: Int,
        filter: SearchFilter,
        marketPlace: MarketPlace
    ): ApiResponse<List<ProductCatalog>, DataError.Remote> {
        return when (marketPlace) {
            MarketPlace.AMAZON -> ApiResponse.Error(DataError.Remote.UNKNOWN_ERROR)
            MarketPlace.FLIPKART -> flipkartCatalogProvider.fetchProductCatalog(query, page, filter)
        }
    }
}