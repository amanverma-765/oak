package com.ark.koin

import com.ark.core.data.HttpClientFactory
import com.ark.data.remote.flipkart.FlipkartCatalogProvider
import com.ark.data.repo.CatalogProvider
import com.ark.data.repo.ProductCatalogRepoImpl
import com.ark.data.repo.catalog.AmazonRepo
import com.ark.data.repo.catalog.FlipkartRepo
import com.ark.domain.model.MarketPlace
import com.ark.domain.repo.ProductCatalogRepo
import com.ark.domain.usecase.ProductCatalogUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal expect val platformModule: Module

internal val sharedModule = module {

    single { HttpClientFactory.create(engine = get()) }

    singleOf(::FlipkartCatalogProvider)

    singleOf(::ProductCatalogRepoImpl).bind<ProductCatalogRepo>()

    singleOf(::ProductCatalogUseCase)

    singleOf(::FlipkartRepo).bind<CatalogProvider>()
    singleOf(::AmazonRepo).bind<CatalogProvider>()

    single<Map<MarketPlace, CatalogProvider>> {
        mapOf(
            MarketPlace.FLIPKART to get<FlipkartRepo>(),
            MarketPlace.AMAZON to get<AmazonRepo>()
        )
    }
}