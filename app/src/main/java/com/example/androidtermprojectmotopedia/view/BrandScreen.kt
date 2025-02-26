package com.example.androidtermprojectmotopedia.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldScope
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.androidtermprojectmotopedia.model.Brand
import com.example.androidtermprojectmotopedia.viewModel.BrandViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BrandScreen(modifier: Modifier = Modifier) {
    val brandNavigator = rememberListDetailPaneScaffoldNavigator<Brand>()
    val brandViewModel: BrandViewModel = viewModel()
    val brandList = brandViewModel.brands.collectAsState().value

    BackHandler(brandNavigator.canNavigateBack()) {
        brandNavigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = brandNavigator.scaffoldDirective,
        value = brandNavigator.scaffoldValue,
        listPane = {
            BrandListPane(
                navigator = brandNavigator,
                modifier = modifier,
                brandList = brandList
            )
        },
        detailPane = {
            BrandDetailPane(
                navigator = brandNavigator,
                modifier = modifier
            )
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ThreePaneScaffoldScope.BrandListPane(
    navigator: ThreePaneScaffoldNavigator<Brand>,
    modifier: Modifier,
    brandList: List<Brand>
) {
    AnimatedPane {
        BrandList(
            modifier = modifier,
            onBrandClicked = { brand ->
                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, content = brand)
            },
            brandList = brandList
        )
    }
}

@Composable
fun BrandList(
    modifier: Modifier,
    onBrandClicked: (Brand) -> Unit,
    brandList: List<Brand>
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Motorcycle Brands",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(brandList) { brand ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable { onBrandClicked(brand) },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Logo
                        AsyncImage(
                            model = brand.logo,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                        // Brand name
                        Text(
                            text = brand.brand,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ThreePaneScaffoldScope.BrandDetailPane(
    navigator: ThreePaneScaffoldNavigator<Brand>,
    modifier: Modifier
) {
    val brand = navigator.currentDestination?.content
    AnimatedPane {
        if (brand != null) {
            BrandDetailScreen(brand)
        } else {
            // Optionally show an empty state or instructions
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Select a brand to see details.")
            }
        }
    }
}

@Composable
fun BrandDetailScreen(brand: Brand) {
    val scrollState = rememberScrollState()

    // A surface that allows you to set a background color or gradient
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Brand Logo
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(bottom = 16.dp)
            ) {
                Box {
                    AsyncImage(
                        model = brand.logo,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            // Brand Name
            Text(
                text = "Brand Name: ${brand.brand}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )

            // Founded info
            InfoCard(
                title = "Founded",
                body = "By ${brand.founder} at ${brand.founded}"
            )

            // Headquarters
            InfoCard(
                title = "Headquarters",
                body = brand.headquarters
            )

            // Brand details
            InfoCard(
                title = "About ${brand.brand}",
                body = brand.detail
            )

            // Google Map
            Spacer(modifier = Modifier.height(16.dp))

            GoogleMapScreen(brand.brand, brand.stores)
        }
    }
}

/**
 * A small reusable Card for displaying labeled info
 */
@Composable
fun InfoCard(
    title: String,
    body: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
