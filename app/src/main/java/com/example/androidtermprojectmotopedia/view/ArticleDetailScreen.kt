package com.example.androidtermprojectmotopedia.view

import VideoPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.androidtermprojectmotopedia.viewModel.MotorcycleViewModel

@Composable
fun ArticleDetailScreen(
    docId: String,
    viewModel: MotorcycleViewModel
) {
    // Trigger data load when docId changes
    LaunchedEffect(docId) {
        viewModel.fetchMotorcycleById(docId)
    }

    // Observe state from the ViewModel
    val motorcycle by viewModel.selectedMotorcycle.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Show any error
    errorMessage?.let { error ->
        Text(
            text = "Error: $error",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    }

    if (motorcycle == null) {
        // Loading indicator
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Display the motorcycle's details
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp)
        ) {
            item {
                // A single card to contain all detail content
                    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                        // Motorcycle image
                        AsyncImage(
                            model = motorcycle!!.image,
                            contentDescription = "Motorcycle Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(MaterialTheme.shapes.medium),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Brand & Model row + Release Date
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Brand: ${motorcycle!!.brand}",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = "Model: ${motorcycle!!.model}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            // Release Date near the top
                            Text(
                                text = "Release Date:\n${motorcycle!!.release_date}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = Italic
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Detail
                        Text("Article about ${motorcycle!!.brand} ${motorcycle!!.model}",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontStyle = Italic
                            ),
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp),)
                        Text(
                            text = "${motorcycle!!.detail}",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Left
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Video
                        VideoPlayer(
                            videoUri = motorcycle!!.video,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Uploaded date at the bottom
                        Text(
                            text = "Uploaded Date: ${motorcycle!!.uploaded_date}",
                            fontStyle = Italic,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
            }
        }
    }
}
