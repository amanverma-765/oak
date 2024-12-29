package com.ark

import com.ark.api.OakCatalogManager
import com.ark.core.domain.ApiResponse
import com.ark.domain.model.MarketPlace
import com.ark.domain.model.SearchFilter
import com.ark.koin.OakInitialiser
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class OakCatalogManagerTest {

    private var oakCatalogManager: OakCatalogManager

    init {
        oakCatalogManager = getOakCatalogManager()
    }

    companion object {
        private var isInitialized = false
        private lateinit var oakCatalogManager: OakCatalogManager

        fun getOakCatalogManager(): OakCatalogManager {
            if (!isInitialized) {
                OakInitialiser.initOak()
                oakCatalogManager = OakCatalogManager()
                isInitialized = true
            }
            return oakCatalogManager
        }
    }

    @Test
    fun `fetchCatalog should return catalog for valid marketplaces`() = runBlocking {
        val marketplaces = listOf(MarketPlace.AMAZON, MarketPlace.FLIPKART)

        marketplaces.forEach { marketplace ->
            val resp = oakCatalogManager.fetchCatalog(
                query = "iphone 15",
                page = 1,
                filter = SearchFilter.FEATURED,
                marketPlaces = listOf(marketplace)
            )

            when (resp) {
                is ApiResponse.Success -> {
                    assertTrue(
                        resp.data.isNotEmpty(),
                        "Catalog should not be empty for $marketplace"
                    )
                }

                is ApiResponse.Error -> {
                    fail("Failed to fetch catalog for $marketplace: ${resp.error}")
                }
            }
        }
    }

    @Test
    fun `fetchCatalog should return catalog for multiple marketplaces`() = runBlocking {

        val marketplaces = listOf(MarketPlace.AMAZON, MarketPlace.FLIPKART)

        val resp = oakCatalogManager.fetchCatalog(
            query = "iphone 15",
            page = 1,
            filter = SearchFilter.FEATURED,
            marketPlaces = listOf(MarketPlace.AMAZON, MarketPlace.FLIPKART)
        )

        when (resp) {
            is ApiResponse.Success -> {
                assertTrue(
                    actual = resp.data.size >= marketplaces.size,
                    message = "Expected catalog data for multiple marketplaces"
                )
            }

            is ApiResponse.Error -> {
                fail("Failed to fetch catalog for multiple marketplaces at once: ${resp.error}")
            }
        }
    }

    @Test
    fun `fetchCatalog should return error for invalid query`() = runBlocking {
        val resp = oakCatalogManager.fetchCatalog(
            query = "", // Empty query
            page = 1,
            filter = SearchFilter.FEATURED,
            marketPlaces = listOf(MarketPlace.AMAZON)
        )

        assertTrue(resp is ApiResponse.Error, "Expected an error response for an invalid query")
    }
}