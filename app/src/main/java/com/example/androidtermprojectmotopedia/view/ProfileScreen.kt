package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.androidtermprojectmotopedia.model.Motorcycle
import com.example.androidtermprojectmotopedia.ui.backgrounds.ProfilePageAnimatedBackground
import com.example.androidtermprojectmotopedia.viewModel.MotorcycleViewModel
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    motorcycleViewModel: MotorcycleViewModel
) {
    // Observe current user
    val currentUser by userViewModel.currentUser.collectAsState(initial = null)
    // Observe motorcycles posted by this user
    val userMotorcycles by motorcycleViewModel.userMotorcycles.collectAsState()

    // Once we have a user docId, load the user's motorcycles
    LaunchedEffect(currentUser) {
        currentUser?.docId?.let { docId ->
            if (docId.isNotEmpty()) {
                motorcycleViewModel.loadMotorcyclesByUserId(docId)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        ProfilePageAnimatedBackground()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1) Profile Image
        AsyncImage(
            model = currentUser?.user?.profile_image
                ?.takeIf { it.isNotBlank() }
                ?: "https://i.pinimg.com/736x/53/fe/d1/53fed15d25b9308613788977fca0d509.jpg",
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .border(
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentScale  = ContentScale.Crop,
            )

        Spacer(modifier = Modifier.height(16.dp))

        // 2) Profile Name
        Text(
            text = currentUser?.user?.name ?: "Profile Name",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3) Title or heading for the user's contributions
        Text(
            text = "Your Distribution to Community",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4) LazyColumn of user's motorcycles

        MotorcycleListScreen(
        motorcycleViewModel,
            motorcycles = userMotorcycles,
        )

    }
    }
}

@Composable
fun MotorcycleListScreen(
    motorcycleViewModel: MotorcycleViewModel,
    motorcycles: List<Motorcycle>
) {
    // State to control the Edit dialog
    var showEditDialog by remember { mutableStateOf(false) }
    var motorcycleToEdit by remember { mutableStateOf<Motorcycle?>(null) }

    var showConfirmDelete by remember { mutableStateOf(false) }
    var deleteCandidate by remember { mutableStateOf<Motorcycle?>(null) }

    // If the user selected "Edit" on some motorcycle
    if (showEditDialog && motorcycleToEdit != null) {
        EditMotorcycleDialog(
            motorcycle = motorcycleToEdit!!,
            onDismissRequest = { showEditDialog = false },
            onSave = { updatedMotorcycle ->
                // 1) Prepare the map of fields to update
                val updatedData = mapOf(
                    "brand" to updatedMotorcycle.brand,
                    "model" to updatedMotorcycle.model,
                    "detail" to updatedMotorcycle.detail,
                    "status" to "pending"
                    // you can omit or add other fields
                )

                // 2) Call your ViewModel
                motorcycleViewModel.updateMotorcycle(
                    docId = updatedMotorcycle.docId,
                    newData = updatedData
                )

                // 3) Close dialog
                showEditDialog = false
            }
        )
    }

    if (showConfirmDelete && deleteCandidate != null) {
        ConfirmDeleteDialog(
            onConfirm = {
                motorcycleViewModel.requestDeleteMotorcycle(deleteCandidate!!.docId)
                showConfirmDelete = false
            },
            onDismiss = { showConfirmDelete = false }
        )
    }

    // Then display your list of motorcycles
    LazyColumn {
        items(motorcycles) { bike ->
            MotorcycleDetailCard(
                motorcycle = bike,
                onEditClick = { selected ->
                    motorcycleToEdit = selected
                    showEditDialog = true
                },
                onRequestDeleteClick = { selected ->
                    deleteCandidate = selected
                    showConfirmDelete = true                }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun MotorcycleDetailCard(
    motorcycle: Motorcycle,
    onEditClick: (Motorcycle) -> Unit,
    onRequestDeleteClick: (Motorcycle) -> Unit,
    modifier: Modifier = Modifier
) {
    // Decide status color
    val statusColor = when (motorcycle.status.lowercase()) {
        "pending" -> Color(0xFFFFC107) // Yellow-ish
        "approved" -> Color(0xFF4CAF50) // Green-ish
        "awaiting_delete" -> Color.Red
        else -> Color.Gray
    }

    // Card background color (slightly off-white)
//    val cardBackground = Color(0xFFF5F5F5)

    Card(
        modifier = modifier
            .fillMaxWidth(),
        border = BorderStroke(1.dp, Color.LightGray),
        elevation = CardDefaults.cardElevation(6.dp)
        // Optionally you can set a shape if you want
        // shape = RoundedCornerShape(12.dp),
        ,colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Row: image on left, status on right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Motorcycle image
                AsyncImage(
                    model = motorcycle.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                // Status text
                Text(
                    text = motorcycle.status,
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Top),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title text: brand - model
            Text(
                text = "${motorcycle.brand} - ${motorcycle.model}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Detail
            Text(
                text = motorcycle.detail,
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Uploaded date
            Text(
                text = "Uploaded: ${motorcycle.uploaded_date}",
                fontSize = 12.sp,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Buttons if status != "requested_delete"
            if (!motorcycle.status.equals("awaiting_delete", ignoreCase = true)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Edit button
                    Button(
                        onClick = { onEditClick(motorcycle) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3), // Blue
                            contentColor = Color.White
                        )
                    ) {
                        Text("Edit")
                    }

                    // Request Delete button
                    Button(
                        onClick = { onRequestDeleteClick(motorcycle) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Request Delete")
                    }
                }
            }
        }
    }
}