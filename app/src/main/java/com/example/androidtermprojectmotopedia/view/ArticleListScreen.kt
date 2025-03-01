package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.androidtermprojectmotopedia.ui.backgrounds.WaveBackground
import com.example.androidtermprojectmotopedia.viewModel.MotorcycleViewModel

@Composable
fun ArticleListScreen(
    modifier: Modifier,
    onArticleClick: (String) -> Unit,
    motorcycleViewModel: MotorcycleViewModel
) {

    val motorcycles by motorcycleViewModel.motorcycles.collectAsState()
    val errorMessage by motorcycleViewModel.errorMessage.collectAsState()
    val approvedMotorcycles = motorcycles.filter { it.status != "pending" }
    val groupedByBrand = remember(approvedMotorcycles) {
        approvedMotorcycles.groupBy { it.brand }.toSortedMap()
    }

    if (errorMessage != null) {
        Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
    }
    Box(modifier = Modifier.fillMaxSize()){
        WaveBackground()

        LazyColumn(
        modifier = modifier
            .padding(start = 16.dp)
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
                    rows = GridCells.Fixed(1),
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(width = 400.dp, height = 200.dp)
                ) {
                    items(brandMotorcycles) { motorcycle ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(200.dp)
                                .clickable {
                                    onArticleClick(motorcycle.docId)
                                },
                            elevation = CardDefaults.cardElevation(6.dp),
                            colors = CardDefaults.cardColors(
                                MaterialTheme.colorScheme.surfaceVariant)
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
                HorizontalDivider(thickness = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    modifier = modifier.padding(vertical = 10.dp))

            }
        }
    }}
}