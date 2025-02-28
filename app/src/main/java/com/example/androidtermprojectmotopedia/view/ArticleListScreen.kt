package com.example.androidtermprojectmotopedia.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.androidtermprojectmotopedia.viewModel.MotorcycleViewModel

@Composable
fun ArticleListScreen(
    modifier: Modifier,
    onArticleClick: (String) -> Unit
) {

    val viewModel: MotorcycleViewModel = viewModel()
    val motorcycles by viewModel.motorcycles.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val groupedByBrand = remember(motorcycles) {
        motorcycles.groupBy { it.brand }
    }

    if (errorMessage != null) {
        Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
    }
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    )
    {
        groupedByBrand.forEach { (brand, brandMotorcycles) ->
            item {
                Text(
                    text = brand,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(width = 400.dp, height = 450.dp)
                ) {
                    items(brandMotorcycles) { motorcycle ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(200.dp)
                                .clickable {
                                    onArticleClick(motorcycle.docId)
                                },
                            colors = CardDefaults.cardColors(Color.White)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                AsyncImage(
                                    model = motorcycle.image,
                                    contentDescription = "Motorcycle Image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = motorcycle.brand,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = motorcycle.model,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = motorcycle.release_date,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }


        }


    }


}