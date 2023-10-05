package com.example.voltlinescasestudy

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.voltlinescasestudy.ui.landing.LandingViewModel
import com.example.voltlinescasestudy.ui.theme.VoltLinesCaseStudyTheme
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(){

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {}
        permissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

        setContent {
            VoltLinesCaseStudyTheme {
                val navController =  rememberNavController()
                val sharedViewModel: LandingViewModel = hiltViewModel()
                // A surface container using the 'background' color from the theme
                NavHost(navController = navController, startDestination = "landing") {
                    composable("landing") {
                        LandingView(
                            sharedViewModel,
                            onFabClicked = {
                                navController.navigate("trip_list")
                            }
                        )
                    }
                    composable("trip_list") {
                        TripListView(
                            sharedViewModel,
                            navigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripListView(
    viewModel: LandingViewModel,
    navigateBack: () -> Unit
    ) {

    val trips = viewModel.selectedStation?.trips ?: emptyList()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Trips of Station ${viewModel.selectedStation?.name}")
                },
                navigationIcon = {
                    IconButton(onClick = {navigateBack()}) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },

            )
        }
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = Color.LightGray),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = {
                    items(trips.size) {
                        TripRowItem(
                            tripName = trips[it].bus_name,
                            tripTime = trips[it].time,
                        )
                    }
                }
            )
        }
    }
}
@Composable
fun TripRowItem(
    tripName: String,
    tripTime: String,
) {
    
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ){
        Text(
            text = tripName,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Text(text = tripTime, fontSize = 24.sp)
        FilledTonalButton(onClick = { /*APİ POST REQ*/ }) {
            Text(text = "Book")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingView(
    viewModel: LandingViewModel,
    onFabClicked: () -> Unit
) {
    val state = viewModel.state
    val cameraPositionState = viewModel.cameraPositionState
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }

    val selectedStation = viewModel.selectedStation

    if(state.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else {
            Scaffold (
                modifier = Modifier.fillMaxSize(),
                floatingActionButtonPosition = FabPosition.Center,
                floatingActionButton = {
                    selectedStation?.let {
                        FABButton(
                            onFabClicked = onFabClicked
                        )
                    }
                },
            ) {
                GoogleMap (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    cameraPositionState = cameraPositionState,
                    properties = mapProperties
                ) {
                    viewModel.state.stations.forEach { station ->
                        val coordinates = station.center_coordinates.split(",")
                        val lat = coordinates.first().toDouble()
                        val lon = coordinates.last().toDouble()
                        val position = LatLng(lat,lon)
                        StationMarker(
                            context = LocalContext.current,
                            position = position,
                            title = station.name,
                            isSelected = selectedStation == station,
                            onMarkerClick = { viewModel.onSelectionChange(station) }
                        )
                    }
                }
            }

        }
}

@Composable
fun FABButton(
    onFabClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(all = 18.dp)
            .clip(shape = RoundedCornerShape(24.dp))
            .background(
                color = Color.Blue
            )
            .fillMaxWidth()
            .clickable { onFabClicked() },
        contentAlignment = Alignment.Center
        ) {
        Text(
            text = "List Trips",
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Transparent)
                .padding(
                    vertical = 10.dp, horizontal = 12.dp
                )
        )
    }
}
@Composable
fun StationMarker(
    context: Context,
    position: LatLng,
    title: String,
    isSelected: Boolean,
    onMarkerClick: () -> Unit
) {

    val defaultIcon = bitmapDescriptorFromVector(
        context, R.drawable.point
    )
    val selectedIcon = bitmapDescriptorFromVector(
        context, R.drawable.selected_point
    )

    val icon = if (isSelected) selectedIcon else defaultIcon

    Marker(
        state = MarkerState(position = position),
        title = title,
        icon = icon,
        onClick = {
            onMarkerClick()
            true
        }
    )
}


fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}
