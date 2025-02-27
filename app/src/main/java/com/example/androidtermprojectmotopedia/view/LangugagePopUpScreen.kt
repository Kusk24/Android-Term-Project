import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.androidtermprojectmotopedia.viewModel.UserViewModel

@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit,
    userViewModel: UserViewModel
) {
    // Map language codes to their full names
    val languageMap = mapOf(
        "en" to "English",
        "zh-rCN" to "Chinese",
        "my" to "Myanmar"
    )

    // Local state holds the selected language code
    var selectedLanguageCode by remember { mutableStateOf(currentLanguage) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Language") },
        text = {
            Column {
                // Display radio buttons for each language code
                languageMap.forEach { (code, fullName) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedLanguageCode = code
                                userViewModel.setLanguage(code)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedLanguageCode == code),
                            onClick = {
                                selectedLanguageCode = code
                                userViewModel.setLanguage(code)
                            }
                        )
                        Text(text = fullName)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Return the final code to the caller
                    onLanguageSelected(selectedLanguageCode)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

