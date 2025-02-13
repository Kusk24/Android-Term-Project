package com.example.androidtermprojectmotopedia.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card

import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.AnimatedPaneScope
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldScope
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.sourceInformationMarkerEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImage
import com.example.androidtermprojectmotopedia.model.Brand
import com.example.androidtermprojectmotopedia.model.Motorcycle

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun MotorcycleListDetailPane(modifier: Modifier) {
    val brandNavigator = rememberListDetailPaneScaffoldNavigator<Brand>()
    val motorcycleNavigator = rememberListDetailPaneScaffoldNavigator<Motorcycle>()

    BackHandler(brandNavigator.canNavigateBack()) {
        brandNavigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = brandNavigator.scaffoldDirective,
        value = brandNavigator.scaffoldValue,
        listPane = { BrandListPane(brandNavigator, modifier) },
        detailPane = { MotorcycleListPane(brandNavigator, motorcycleNavigator, modifier) },
        extraPane = { MotorcycleDetailExtraPane(motorcycleNavigator, modifier) }
    )
}



@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun ThreePaneScaffoldScope.BrandListPane(
    navigator: ThreePaneScaffoldNavigator<Brand>,
    modifier: Modifier
) {

    AnimatedPane {
        BrandList(modifier, onBrandClicked = { brand ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, brand)
        })
    }

}

@Composable
fun BrandList(modifier: Modifier, onBrandClicked: (Brand) -> Unit) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Motorcycle Brands",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(demoBrands) { brand ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable { onBrandClicked(brand) },
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = brand.name
                        )
                    }
                }
            }
        }
    }

}

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun ThreePaneScaffoldScope.MotorcycleListPane(
    brandNavigator: ThreePaneScaffoldNavigator<Brand>,
    motorcycleNavigator: ThreePaneScaffoldNavigator<Motorcycle>,
    modifier: Modifier
) {
    val brand = brandNavigator.currentDestination?.content  // ✅ Get selected brand

    AnimatedPane {
        if (brand != null) {
            MotorcycleList(brand, modifier, onMotorcycleClicked = { motorcycle ->
                println("Clicked motorcycle: ${motorcycle.name}")  // Debugging
                motorcycleNavigator.navigateTo(ListDetailPaneScaffoldRole.Extra, motorcycle)  // ✅ Update extra pane
            })
        } else {
            Text(text = "No Brand Selected", modifier = modifier.padding(16.dp))
        }
    }
}

@Composable
fun MotorcycleList(
    brand: Brand,
    modifier: Modifier = Modifier,
    onMotorcycleClicked: (Motorcycle) -> Unit
) {
    val models = brand.models

    Column(modifier = modifier.padding(16.dp)) {
        // Header Text
        Text(
            text = "Motorcycle lists of ${brand.name}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(models) { motorcycle ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onMotorcycleClicked(motorcycle) },
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        // Motorcycle Image
                        Row (
                            modifier = Modifier.fillMaxWidth()
                        ){
                            AsyncImage(
                                model = motorcycle.image,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(150.dp)
                                    .padding(end = 16.dp)
                            )
                            // Motorcycle Name
                            Text(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                text = "${brand.name} ${motorcycle.name}",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )

                        }
                    }
                }
            }
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun ThreePaneScaffoldScope.MotorcycleDetailExtraPane(
    navigator: ThreePaneScaffoldNavigator<Motorcycle>,
    modifier: Modifier
) {
    val motorcycle = navigator.currentDestination?.content

    AnimatedPane {
        if (motorcycle != null) {
            println("Displaying details for: ${motorcycle.name}")  // Debugging
            MotorcycleDetailScreen(motorcycle, modifier)
        } else {
            Text(
                text = "No Motorcycle Selected",
                modifier = modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun MotorcycleDetailScreen(motorcycle: Motorcycle, modifier: Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        AsyncImage(
            model = motorcycle.image,
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .padding(end = 16.dp)
        )
        Text(
            text = """
                Model Name: ${motorcycle.name}
                Engine: ${motorcycle.engine}
            """.trimIndent()
        )

    }

}

val demoBrands = listOf(
    Brand(
        name = "Kawasaki",
        models = listOf(
            Motorcycle(
                name = "Z900",
                engine = "948cc inline-4",
                image = "https://www.kawasaki.co.th/uploads/products/z900/z900-black-gray-2023-02.jpg"
            )
        )
    ),
    Brand(
        name = "Yamaha",
        models = listOf(
            Motorcycle(
                name = "R1",
                engine = "998cc inline-4",
                image = "https://storagetym.blob.core.windows.net/www2021/images/product-2021/bigbike/model-big-bike-2023/up-date-big-bike-2023/r1-2022/r1-2022-blue-01.png?sfvrsn=da9d3df8_2"
            ),
            Motorcycle(
                name = "MT-09",
                engine = "847cc inline-3",
                image = "https://storagetym.blob.core.windows.net/www2021/images/product-2021/bigbike/model-2024/mt-09/model-mt-09-size-700x525/mt09-icon-blue-02-700x525.png?sfvrsn=b52dc159_2"
            ),
            Motorcycle(
                name = "MT-07",
                engine = "699cc parellel twin",
                image = ""
            ),
            Motorcycle(
                name = "MT-03",
                engine = "",
                image = ""
            ),
            Motorcycle(
                name = "R6",
                engine = "",
                image = ""
            ),
            Motorcycle(
                name = "R3",
                engine = "",
                image = ""
            ),
            Motorcycle(
                name = "XSR900",
                engine = "",
                image = ""
            ),
            Motorcycle(
                name = "XSR700",
                engine = "",
                image = ""
            ),
            Motorcycle(
                name = "XSR155",
                engine = "",
                image = ""
            )

        )
    )
)
