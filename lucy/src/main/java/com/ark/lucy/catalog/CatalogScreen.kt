package com.ark.lucy.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ark.lucy.R
import com.ark.lucy.component.TopBarWithSearch
import com.ark.lucy.util.plus
import org.koin.compose.viewmodel.koinViewModel
import java.text.NumberFormat
import java.util.Locale


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    viewModel: CatalogViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyState = rememberLazyListState()
    val uriHandler = LocalUriHandler.current

    Scaffold(
        modifier = modifier,
        topBar = {
            Box {
                TopBarWithSearch(
                    query = uiState.searchQuery,
                    onQueryChange = {
                        viewModel.onEvent(CatalogUiEvent.SearchQueryChanged(it))
                    },
                    onSearch = {
                        viewModel.onEvent(CatalogUiEvent.SearchForProduct)
                    },
                    onBackPressed = {}
                )
                if (uiState.isLoading) LinearProgressIndicator(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = lazyState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = innerPadding.plus(
                PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp)
            )
        ) {
            if (uiState.productItems.isNotEmpty()) {
                items(uiState.productItems) { product ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 1.dp,
                            pressedElevation = 4.dp
                        ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            // Product Image Section - Left Side
                            Box(Modifier.size(150.dp)) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(product.imgUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = product.title,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Discount Badge on Image
                                product.discountPercent?.let { percent ->
                                    if (percent > 0) {
                                        Surface(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(4.dp),
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0xFFE53935)
                                        ) {
                                            Text(
                                                text = "${percent.toInt()}%",
                                                color = Color.White,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(
                                                    horizontal = 4.dp,
                                                    vertical = 2.dp
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            // Product Details Section - Right Side
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 12.dp)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Top Section: Marketplace, Title, Rating
                                Column {
                                    // Marketplace Badge
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(
                                            alpha = 0.7f
                                        )
                                    ) {
                                        Text(
                                            text = product.marketPlace.name,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(
                                                horizontal = 6.dp,
                                                vertical = 2.dp
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Product Title
                                    Text(
                                        text = product.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Normal,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 18.sp
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Rating Section
                                    if (product.rating != null) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = when (product.rating ?: 0f) {
                                                    in 4.0..5.0 -> Color(0xFF4CAF50)
                                                    in 3.0..3.9 -> Color(0xFFFF9800)
                                                    else -> Color(0xFFF44336)
                                                }
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(
                                                        horizontal = 4.dp,
                                                        vertical = 2.dp
                                                    ),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = String.format(
                                                            "%.1f",
                                                            product.rating
                                                        ),
                                                        color = Color.White,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Icon(
                                                        imageVector = Icons.Filled.Star,
                                                        contentDescription = "Rating",
                                                        modifier = Modifier.size(8.dp),
                                                        tint = Color.White
                                                    )
                                                }
                                            }

                                            product.ratingCount?.let { count ->
                                                Text(
                                                    text = " ($count)",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.padding(start = 4.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Bottom Section: Price and Button
                                Column {
                                    // Price Section
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            // Current Price
                                            product.displayPrice?.let { price ->
                                                Text(
                                                    text = "₹${
                                                        NumberFormat.getNumberInstance(
                                                            Locale("en", "IN")
                                                        ).format(price)
                                                    }",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            // Original Price and Discount
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (product.mrp != null && product.mrp != product.displayPrice) {
                                                    Text(
                                                        text = "₹${
                                                            NumberFormat.getNumberInstance(
                                                                Locale("en", "IN")
                                                            ).format(product.mrp)
                                                        }",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        textDecoration = TextDecoration.LineThrough,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )

                                                    product.discount?.let { discount ->
                                                        if (discount > 0) {
                                                            Text(
                                                                text = " Save ₹${
                                                                    NumberFormat.getNumberInstance(
                                                                        Locale("en", "IN")
                                                                    ).format(discount)
                                                                }",
                                                                style = MaterialTheme.typography.bodySmall,
                                                                color = Color(0xFF4CAF50),
                                                                fontWeight = FontWeight.Medium
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        // View Product Button
                                        OutlinedButton(
                                            onClick = { uriHandler.openUri(product.productUrl) },
                                            modifier = Modifier.padding(start = 8.dp),
                                            shape = RoundedCornerShape(6.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = MaterialTheme.colorScheme.primary
                                            ),
                                            border = BorderStroke(
                                                1.dp,
                                                MaterialTheme.colorScheme.primary
                                            ),
                                            contentPadding = PaddingValues(
                                                horizontal = 12.dp,
                                                vertical = 6.dp
                                            )
                                        ) {
                                            Text(
                                                text = "View",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            fontWeight = FontWeight.W400,
                            text = "Search for a product...",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}