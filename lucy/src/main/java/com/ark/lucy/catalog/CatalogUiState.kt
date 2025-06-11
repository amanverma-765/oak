package com.ark.lucy.catalog

import com.ark.domain.model.ProductCatalog

data class CatalogUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val productItems: List<ProductCatalog> = emptyList(),
    val searchQuery: String = ""
)
