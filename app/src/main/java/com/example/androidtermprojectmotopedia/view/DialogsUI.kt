package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.androidtermprojectmotopedia.model.Motorcycle

@Composable
fun SuccessDialog(title: String, text: String,onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        title = { Text(title) },
        text = { Text(text) }
    )
}

@Composable
fun EditMotorcycleDialog(
    motorcycle: Motorcycle,
    onDismissRequest: () -> Unit,
    onSave: (Motorcycle) -> Unit
) {
    // Local state for brand, model, detail, etc.
    var brand by remember { mutableStateOf(motorcycle.brand) }
    var model by remember { mutableStateOf(motorcycle.model) }
    var detail by remember { mutableStateOf(motorcycle.detail) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Edit Motorcycle", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Brand") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Model") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                // Make the article field bigger:
                OutlinedTextField(
                    value = detail,
                    onValueChange = { detail = it },
                    label = { Text("Article / Detail") },
                    modifier = Modifier
                        .fillMaxWidth()
                        // Option A: Use a fixed taller height
                        .height(200.dp),
                    // Option B: Or let lines expand with minLines/maxLines
                    // minLines = 5,
                    // maxLines = 10
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        val updated = motorcycle.copy(
                            brand = brand,
                            model = model,
                            detail = detail
                        )
                        onSave(updated)
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}


@Composable
fun ConfirmDeleteDialog(
    message: String = "Are you sure you want to request deletion for this item?",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { androidx.compose.material3.Text("Confirm Deletion") },
        text = { androidx.compose.material3.Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                androidx.compose.material3.Text("Yes, Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                androidx.compose.material3.Text("Cancel")
            }
        }
    )
}

@Composable
fun ErrorDialog(title: String, text: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK") }
        }
    )
}

@Composable
fun LogoutWarningDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Out") },
        text = { Text("Are you sure you want to log out?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}



@Composable
fun HelpSupportDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Help & Support") },
        text = {
            Text(
                text = "For assistance or inquiries, please contact our support:\n" +
                        "Email: support@example.com\n" +
                        "Phone: +1 234 567 890\n\n" +
                        "Our team will be happy to help!"
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

/**
 * Simple FAQ dialog that displays a list of Q&A items.
 * Customize or replace the hardcoded Q&A with your own content.
 */
@Composable
fun FAQDialog(
    onDismiss: () -> Unit
) {
    // Sample list of FAQs
    val faqs = listOf(
        "Q: How do I change my password?" to
                "A: Go to Settings > Account Information > Change Password.",
        "Q: Can I sync data across devices?" to
                "A: Yes, simply sign in with the same account on each device.",
        "Q: How do I report a bug?" to
                "A: Please contact our support email with details and screenshots."
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "FAQs") },
        text = {
            Column {
                faqs.forEach { (question, answer) ->
                    Text(text = question, fontWeight = FontWeight.Bold)
                    Text(text = answer)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}