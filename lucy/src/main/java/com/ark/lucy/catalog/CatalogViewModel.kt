package com.ark.lucy.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ark.api.OakCatalogManager
import com.ark.core.domain.ApiResponse
import com.ark.domain.model.MarketPlace
import com.ark.domain.model.SearchFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogViewModel(private val catalogManager: OakCatalogManager) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: CatalogUiEvent) {
        when (event) {
            CatalogUiEvent.ClearSearch -> TODO()
            is CatalogUiEvent.ProductClicked -> TODO()
            is CatalogUiEvent.SearchQueryChanged -> _uiState.update { it.copy(searchQuery = event.query) }
            CatalogUiEvent.SearchForProduct -> searchForProduct()
        }
    }

    private fun searchForProduct() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val productResponse = catalogManager.fetchCatalog(
                query = _uiState.value.searchQuery,
                page = 1,
                filter = SearchFilter.POPULARITY_DESCENDING,
                marketPlaces = listOf(MarketPlace.FLIPKART, MarketPlace.AMAZON)
            )
            when (productResponse) {
                is ApiResponse.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = "failed to fetch product data",
                            isLoading = false
                        )
                    }
                }

                is ApiResponse.Success -> {
                    _uiState.update { it.copy(productItems = productResponse.data, isLoading = false) }
                }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
        }
    }

}