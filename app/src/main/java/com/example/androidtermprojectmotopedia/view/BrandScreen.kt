package com.example.androidtermprojectmotopedia.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.androidtermprojectmotopedia.model.Brand
import com.example.androidtermprojectmotopedia.viewModel.BrandViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BrandScreen(modifier : Modifier){
    val brandNavigator = rememberListDetailPaneScaffoldNavigator<Brand>()
    val brandViewModel : BrandViewModel = viewModel()
    val brandList = brandViewModel.brands.collectAsState().value

    BackHandler(brandNavigator.canNavigateBack()) {
        brandNavigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = brandNavigator.scaffoldDirective,
        value = brandNavigator.scaffoldValue,
        listPane = {
            BrandListPane(brandNavigator,
                modifier = modifier,
                brandList)
        },
        detailPane = {
            BrandDetailPane(
                brandNavigator,
                modifier = modifier)
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ThreePaneScaffoldScope.BrandListPane(
    navigator : ThreePaneScaffoldNavigator<Brand>,
    modifier : Modifier,
    brandList : List<Brand>
    ) {

    AnimatedPane() {
        BrandList(
            modifier, onBrandClicked = { brand ->
                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, content = brand)
            },
            brandList = brandList
        )
    }
}

@Composable
fun BrandList(modifier: Modifier, onBrandClicked : (Brand) -> Unit, brandList: List<Brand>){

        Column(modifier = modifier.padding(16.dp)) {
//            Text(
//                text = "Motorcycle Brands",
//                fontWeight = FontWeight.Bold,
//                fontSize = 24.sp
//            )

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
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = brand.logo,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .weight(1f),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = brand.brand,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                                fontWeight = FontWeight.SemiBold
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
    modifier : Modifier,
){
    val brand  = navigator.currentDestination?.content

    AnimatedPane {
            if (brand != null) {
                BrandDetailScreen(brand)
            }

    }
}

@Composable
fun BrandDetailScreen(brand: Brand) {

    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .verticalScroll(scrollState)
        .padding(start = 16.dp, top = 100.dp, end = 16.dp, bottom = 16.dp)){

        AsyncImage(model = brand.logo,
            contentDescription = null, modifier = Modifier.height(250.dp).fillMaxWidth().wrapContentSize(Alignment.Center))

        Card(modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally).padding(vertical = 20.dp)) {
            Text(brand.brand, fontWeight = FontWeight.SemiBold, fontSize = 30.sp)
        }

        Card(modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.Start).padding(vertical = 10.dp)) {
            Text("Founded by ${brand.founder} at ${brand.founded}")
        }


        Card(modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.Start).padding(vertical = 10.dp)) {
            Text("Headquarter : ${brand.headquarters}")
        }

        Card(modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.Start).padding(vertical = 10.dp)) {
            Text(brand.detail, fontSize = 14.sp)
        }

        GoogleMapScreen(
            brand.stores
        )
    }
}

//val scrollState = rememberScrollState()
//
//ConstraintLayout (modifier.verticalScroll(scrollState)){
//
//    val (item1,item2,item3,item4,item5,item6) = createRefs()
//
//    AsyncImage(model = brand.logo,
//        contentDescription = null,
//        modifier.constrainAs(item1){
//            top.linkTo(parent.top)
//            start.linkTo(parent.start)
//            end.linkTo(parent.end)
//        })
//
//    Text(brand.brand,
//        modifier.constrainAs(item2){
//            top.linkTo(item1.bottom)
//            start.linkTo(parent.start)
//            end.linkTo(parent.end)
//        })
//
//    Text("founded by ${brand.founded}",
//        modifier.constrainAs(item3){
//            top.linkTo(item2.bottom)
//            start.linkTo(parent.start)
//            end.linkTo(parent.end)
//        })
//
//    Text(brand.headquarters,
//        modifier.constrainAs(item4){
//            top.linkTo(item3.bottom)
//            start.linkTo(parent.start)
//            end.linkTo(parent.end)
//        })
//
//    Text(brand.detail,
//        modifier.constrainAs(item5){
//            top.linkTo(item4.bottom)
//            start.linkTo(parent.start)
//            end.linkTo(parent.end)
//        })
//
//    GoogleMapScreen(
//        brand.stores,
//        modifier.height(500.dp).fillMaxWidth()
//            .constrainAs(item6){
//                top.linkTo(item5.bottom)
//                start.linkTo(parent.start)
//                end.linkTo(parent.end)
//                bottom.linkTo(item6.bottom)
//            }
//    )