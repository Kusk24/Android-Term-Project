package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.androidtermprojectmotopedia.model.Motorcycle
import com.example.androidtermprojectmotopedia.viewModel.MotorcycleViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onMotorcycleClicked: (String) -> Unit
) {
    val pageSize = 5 // Fixed items per page
    var currentPage by remember { mutableStateOf(0) } // Track the current page
    var searchQuery by remember { mutableStateOf("") } // User's search query

    val viewModel: MotorcycleViewModel = viewModel()
    val motorcycles by viewModel.motorcycles.collectAsState()

    val filteredMotorcycles = motorcycles.filter {
        it.model.contains(searchQuery, ignoreCase = true)
    }
    val totalPages = (filteredMotorcycles.size + pageSize - 1) / pageSize

    val motorcyclesToShow = filteredMotorcycles
        .drop(currentPage * pageSize)
        .take(pageSize)

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                currentPage = 0 
            }, trailingIcon = { Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null) },

            label = { Text("Search Motorcycles") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Motorcycle List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(motorcyclesToShow) { motorcycle ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onMotorcycleClicked(motorcycle.docId) },
                    elevation = CardDefaults.cardElevation(6.dp)

                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        AsyncImage(
                            model = motorcycle.image,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            text = motorcycle.model,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { if (currentPage > 0) currentPage-- },
                enabled = currentPage > 0
            ) {
                Text("Previous")
            }

            Text(
                text = "Page ${currentPage + 1} of $totalPages",
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Button(
                onClick = { if (currentPage < totalPages - 1) currentPage++ },
                enabled = currentPage < totalPages - 1
            ) {
                Text("Next")
            }
        }
    }
}
