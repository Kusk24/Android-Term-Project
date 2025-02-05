package com.example.androidtermprojectmotopedia.view

import android.content.res.Resources.Theme
import android.graphics.drawable.shapes.Shape
import android.provider.ContactsContract.Profile
import android.widget.Switch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedDrawerExample(
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem  by remember {mutableStateOf("Home")}
    var theme by remember {mutableStateOf(false)}


    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text("Motopedia", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                    HorizontalDivider()

                    Spacer(Modifier.height(12.dp))

                    Box (modifier = Modifier.fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)){
                        AsyncImage(
                            model = "https://i.pinimg.com/736x/53/fe/d1/53fed15d25b9308613788977fca0d509.jpg",
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .border(
                                    border = BorderStroke(1.dp, Color.Black),
                                    shape = RoundedCornerShape(50.dp)
                                ),
                            clipToBounds = true
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Text("Profile Name", modifier = Modifier.fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.SemiBold, fontSize = 22.sp)

                    Spacer(Modifier.height(12.dp))

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))


                    Text("Uses", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    NavigationDrawerItem(
                        label = { Text("Home") },
                        selected = false,
                        icon = { Icon(Icons.Default.Home, contentDescription = null )},
                        onClick = {
                            selectedItem = "Home"
                            scope.launch {drawerState.close()}
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Search") },
                        selected = false,
                        icon = { Icon(Icons.Default.Search, contentDescription = null)},
                        onClick = {
                            selectedItem = "Search"
                            scope.launch {drawerState.close()}
                        }
                    )

                    NavigationDrawerItem(
                        label = {Text("Upload")},
                        selected = false,
                        icon = { Icon(Icons.Default.Upload, contentDescription = null) },
                        onClick = {
                            selectedItem = "Search"
                            scope.launch { drawerState.close()}
                        }
                    )

                    NavigationDrawerItem(
                        label = {Text("Notification")},
                        selected = false,
                        icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                        onClick = {
                            selectedItem = "Notification"
                            scope.launch {drawerState.close()}
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text("Management", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)

                    NavigationDrawerItem(
                        label = { Text("Profile")},
                        selected = selectedItem == "Profile",
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        onClick = { selectedItem = "Profile"
                                    scope.launch{drawerState.close()}
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Settings") },
                        selected = selectedItem == "Settings",
                        icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
//                        badge = { Text("20") }, // Placeholder
                        onClick = { selectedItem = "Settings"
                            scope.launch{drawerState.close()}
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Help and feedback") },
                        selected = false,
                        icon = { Icon(Icons.AutoMirrored.Outlined.Help, contentDescription = null) },
                        onClick = { /* Handle click */ },
                    )

                    ConstraintLayout(modifier = Modifier.fillMaxWidth().height(50.dp).padding(16.dp)) {

                            val (item1,item2) = createRefs()

                            Text("Theme", modifier = Modifier.wrapContentWidth(Alignment.Start)
                                .constrainAs(item1){
                                    start.linkTo(parent.start)
                                })

                            Switch(checked = theme, onCheckedChange = {
                                theme = it
                            }, modifier = Modifier.wrapContentHeight(Alignment.Bottom)
                                .constrainAs(item2){
                                    end.linkTo(parent.end)
                                })

                    }

                    Spacer(Modifier.height(12.dp))

                }
            }
        },  gesturesEnabled = true,
        drawerState = drawerState
    )

    {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selectedItem) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            when (selectedItem) {

                "Home" -> LoginPage(
                    modifier = Modifier.padding(innerPadding)
                )

                "Settings" -> SettingScreen(modifier = Modifier.padding(innerPadding))

                "Profile" -> ProfileScreen(modifier = Modifier.padding(innerPadding))

                "Notification" -> NotificationScreen(modifier = Modifier.padding(innerPadding))
            }
//            content(innerPadding)
        }
    }
}

@Composable
fun HomeScreen(modifier : Modifier) {
    DetailedDrawerExample { paddingValues ->
        LoginPage(modifier = Modifier.padding(paddingValues))
    }
}