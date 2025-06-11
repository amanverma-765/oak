package com.ark.lucy.catalog

sealed class CatalogUiEvent {
    data class SearchQueryChanged(val query: String) : CatalogUiEvent()
    data class ProductClicked(val productId: String) : CatalogUiEvent()
    data object ClearSearch : CatalogUiEvent()
    data object SearchForProduct : CatalogUiEvent()
}