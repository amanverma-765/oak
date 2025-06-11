package com.ark.lucy.koin

import com.ark.api.OakCatalogManager
import com.ark.lucy.catalog.CatalogViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {

    single { OakCatalogManager() }
    viewModelOf(::CatalogViewModel)

}