    package com.example.androidtermprojectmotopedia.view

    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Search
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.Card
    import androidx.compose.material3.CardDefaults
    import androidx.compose.material3.Icon
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Text
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
    import androidx.lifecycle.viewmodel.compose.viewModel
    import coil3.compose.AsyncImage
    import com.example.androidtermprojectmotopedia.ui.backgrounds.CurlyLineBackgroundVariant1
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
        val approvedMotorcycles = motorcycles.filter { it.status != "pending" }

        val filteredMotorcycles = approvedMotorcycles.filter {
            it.model.contains(searchQuery, ignoreCase = true)
        }
        // Ensure totalPages is at least 1 to avoid division by zero.
        val totalPages = if (filteredMotorcycles.isEmpty()) 1 else (filteredMotorcycles.size + pageSize - 1) / pageSize

        val motorcyclesToShow = filteredMotorcycles
            .drop(currentPage * pageSize)
            .take(pageSize)

        Box(modifier = Modifier.fillMaxSize()) {
            // Use one of background composables
            CurlyLineBackgroundVariant1()

            Column(modifier = modifier.padding(16.dp)) {
                // Improved TextField with Outlined style
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        currentPage = 0
                    },
                    label = { Text("Search Motorcycles") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Motorcycle List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(motorcyclesToShow) { motorcycle ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onMotorcycleClicked(motorcycle.docId) },
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(
                                MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(modifier = Modifier.padding(16.dp)) {
                                AsyncImage(
                                    model = motorcycle.image,
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = motorcycle.model,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pagination Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { if (currentPage > 0) currentPage-- },
                        enabled = currentPage > 0,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground)
                    ) {
                        Text("Previous")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Page ${currentPage + 1} of $totalPages",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { if (currentPage < totalPages - 1) currentPage++ },
                        enabled = currentPage < totalPages - 1,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground)
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }
