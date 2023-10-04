package com.example.voltlinescasestudy

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
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
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                        LandingView()
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingView(
    viewModel: LandingViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    var isSelected = true
    val cameraPositionState = viewModel.cameraPositionState
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }

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
                    if(isSelected) {
                        FABButton()
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
                            title = station.name
                        )
                    }
                }
            }

        }
}

@Composable
fun FABButton() {
    Box(
        modifier = Modifier
            .padding(all = 16.dp)
            .clip(shape = RoundedCornerShape(24.dp))
            .background(
                color = Color.Blue
            )
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
        ) {
        Text(
            text = "List Trips",
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .background(Color.Transparent)
                .padding(
                    vertical = 8.dp, horizontal = 12.dp
                )
        )
    }
}
@Composable
fun StationMarker(
    context: Context,
    position: LatLng,
    title: String,
    isSelected: Boolean = false
) {

    val defaultIcon = bitmapDescriptorFromVector(
        context, R.drawable.point
    )
    val selectedIcon = bitmapDescriptorFromVector(
        context, R.drawable.selected_point
    )

    val icon by remember {
        mutableStateOf(if (isSelected) selectedIcon else defaultIcon)
    }

    val markerState = remember { mutableStateOf(MarkerState(position = position)) }

    Marker(
        state = MarkerState(position = position),
        title = title,
        icon = icon,
        onClick = {

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
