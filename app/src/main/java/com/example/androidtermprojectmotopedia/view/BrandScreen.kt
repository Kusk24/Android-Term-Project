import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidtermprojectmotopedia.model.Brand
import com.example.androidtermprojectmotopedia.viewModel.BrandViewModel
import androidx.compose.ui.unit.dp
import com.example.androidtermprojectmotopedia.view.BrandDetailScreen
import com.example.androidtermprojectmotopedia.view.BrandListScreen

@Composable
fun BrandScreen(
    windowSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
) {
    val brandViewModel: BrandViewModel = viewModel()
    val brandList = brandViewModel.brands.collectAsState().value
    var selectedBrand by remember { mutableStateOf<Brand?>(null) }

    // Add back navigation for Compact screens when detail is showing
    if (windowSizeClass == WindowWidthSizeClass.Compact && selectedBrand != null) {
        BackHandler {
            selectedBrand = null
        }
    }

    // Decide layout based on window size
    when (windowSizeClass) {
        WindowWidthSizeClass.Compact -> {
            if (selectedBrand == null) {
                BrandListScreen(
                    brandList = brandList,
                    onBrandClicked = { brand ->
                        selectedBrand = brand
                    }
                )
            } else {
                BrandDetailScreen(brand = selectedBrand!!)
            }
        }
        else -> {
            Row(Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    BrandListScreen(
                        brandList = brandList,
                        onBrandClicked = { brand ->
                            selectedBrand = brand
                        }
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    selectedBrand?.let {
                        BrandDetailScreen(brand = it)
                    } ?: run {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Select a brand to see details.")
                        }
                    }
                }
            }
        }
    }
}
