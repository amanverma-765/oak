package com.ark.data.mapper

import com.ark.data.model.flipkart.FlipkartSearchFilter
import com.ark.domain.model.SearchFilter

internal object SearchFilterMapper {

    internal fun SearchFilter.toFlipkartSearchFilter(): FlipkartSearchFilter {
        return when (this) {
            SearchFilter.PRICE_ASCENDING -> FlipkartSearchFilter.PRICE_ASCENDING
            SearchFilter.PRICE_DESCENDING -> FlipkartSearchFilter.PRICE_DESCENDING
            SearchFilter.FEATURED -> FlipkartSearchFilter.FEATURED
            SearchFilter.POPULARITY_DESCENDING -> FlipkartSearchFilter.POPULARITY_DESCENDING
            SearchFilter.LATEST_ARRIVAL -> FlipkartSearchFilter.LATEST_ARRIVAL

            SearchFilter.RATING_DESCENDING -> FlipkartSearchFilter.POPULARITY_DESCENDING      // Will be custom created
            SearchFilter.DISCOUNT_DESCENDING -> FlipkartSearchFilter.FEATURED                 // Will be custom created
        }
    }
}