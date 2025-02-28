package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.androidtermprojectmotopedia.service.VideoPlayer
import com.example.androidtermprojectmotopedia.viewModel.MotorcycleViewModel

@Composable
fun ArticleDetailScreen(
    docId: String
) {
    val viewModel: MotorcycleViewModel = viewModel()
    LaunchedEffect(docId) {
        viewModel.fetchMotorcycleById(docId)
    }

    val motorcycle by viewModel.selectedMotorcycle.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Display any errors
    if (errorMessage != null) {
        Text(
            text = "Error: $errorMessage",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(8.dp)
        )
    }

    if (motorcycle == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Display the motorcycle's details
        LazyColumn (modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
        ) {
            item {
                AsyncImage(
                    model = motorcycle!!.image,
                    contentDescription = "Motorcycle Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Brand: ${motorcycle!!.brand}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Model: ${motorcycle!!.model}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Detail: ${motorcycle!!.detail}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                VideoPlayer(
                    videoUri = motorcycle!!.video,
                    modifier = Modifier.fillMaxWidth().height(500.dp)
                )
                Text(
                    text = "Release Date: ${motorcycle!!.release_date}",
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Uploaded Date: ${motorcycle!!.uploaded_date}",
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium
                )

            }
        }
    }
}

