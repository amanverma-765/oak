package com.ark.data.repo

import com.ark.core.domain.ApiResponse
import com.ark.core.domain.DataError
import com.ark.domain.model.MarketPlace
import com.ark.domain.model.ProductCatalog
import com.ark.domain.model.SearchFilter
import com.ark.domain.repo.ProductCatalogRepo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

internal class ProductCatalogRepoImpl(
    private val catalogProviders: Map<MarketPlace, CatalogProvider>
) : ProductCatalogRepo {

    override suspend fun fetchProductCatalog(
        query: String,
        page: Int,
        filter: SearchFilter,
        marketPlaces: List<MarketPlace>
    ): ApiResponse<List<ProductCatalog>, DataError> {

        if (marketPlaces.isEmpty()) {
            return ApiResponse.Error(DataError.Remote.UnknownError("No marketplaces selected"))
        }

        return coroutineScope {
            val deferredResponses = marketPlaces.toSet().mapNotNull { marketPlace ->
                catalogProviders[marketPlace]?.let { provider ->
                    async { provider.fetchProductCatalog(query, page, filter) }
                }
            }

            val responses = deferredResponses.awaitAll()

            val combinedProducts = mutableListOf<ProductCatalog>()
            val errors = mutableListOf<DataError>()

            responses.forEach { response ->
                when (response) {
                    is ApiResponse.Success -> combinedProducts.addAll(response.data)
                    is ApiResponse.Error -> errors.add(response.error)
                }
            }

            if (combinedProducts.isNotEmpty()) {
                val filteredProducts = applyFilter(combinedProducts, filter)
                ApiResponse.Success(filteredProducts)
            } else {
                ApiResponse.Error(
                    errors.firstOrNull()
                        ?: DataError.Remote.UnknownError("No product catalog found")
                )
            }
        }
    }

    private fun applyFilter(products: List<ProductCatalog>, filter: SearchFilter): List<ProductCatalog> {
        return when (filter) {
            SearchFilter.PRICE_ASCENDING -> products.sortedBy { it.displayPrice ?: it.mrp ?: Float.MAX_VALUE }
            SearchFilter.PRICE_DESCENDING -> products.sortedByDescending { it.displayPrice ?: it.mrp ?: Float.MIN_VALUE }
            SearchFilter.POPULARITY_DESCENDING -> products.sortedByDescending { it.ratingCount ?: 0 }
            SearchFilter.RATING_DESCENDING -> products.sortedByDescending { it.rating ?: 0f }
            SearchFilter.DISCOUNT_DESCENDING -> products.sortedByDescending { it.discountPercent ?: 0f }
            SearchFilter.FEATURED -> products
            SearchFilter.LATEST_ARRIVAL -> products
        }
    }
}