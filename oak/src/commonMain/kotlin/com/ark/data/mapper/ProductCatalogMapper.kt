package com.ark.data.mapper

import com.ark.data.model.flipkart.FlipkartProductCatalog
import com.ark.domain.model.MarketPlace
import com.ark.domain.model.ProductCatalog

internal object ProductCatalogMapper {

    internal fun FlipkartProductCatalog.toMainProductCatalog(): ProductCatalog {
        return ProductCatalog(
            marketPlace = MarketPlace.FLIPKART,
            id = this.id.toMarketplaceId(MarketPlace.FLIPKART),
            productUrl = this.productUrl,
            imgUrl = this.imgUrl,
            title = this.title,
            mrp = this.mrp,
            displayPrice = this.displayPrice,
            discountPercent = this.discountPercent,
            rating = this.rating,
            ratingCount = this.ratingCount,
            discount = this.discount
        )
    }
}