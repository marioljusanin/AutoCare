package ba.sum.fsre.autocare.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import ba.sum.fsre.autocare.data.Vehicle
import ba.sum.fsre.autocare.viewModel.VehicleViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(

    viewModel: VehicleViewModel,
    navController: NavController
){


    Scaffold(
        topBar = {TopAppBar(
            title = {Text("Quick Stats")}
        )}
    ) {
        padding->

        Column(
            modifier = Modifier.fillMaxSize().padding(padding).background(color = White),
            horizontalAlignment = Alignment.CenterHorizontally


        ) {
            //Text("Quick Stats")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                StatCard(
                    title = "Total Costs",
                    value = "100",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                StatCard(
                    title = "Next Service",
                    value = "Sutra",
                    modifier = Modifier.weight(1f)
                )

            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("My Vehicles", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))

                FilledTonalIconButton(onClick = {
                    navController.navigate("add_service")
                }) {
                    Icon(Icons.Default.Build, contentDescription = "Add service/expense")
                }
                IconButton(onClick = {
                    navController.navigate("add_vehicle/0")
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add vehicle")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            val vehiclesList = viewModel.getAllVehicles.collectAsState(listOf())
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(vehiclesList.value) { vehicle ->
                    VehicleItem(vehicle = vehicle) {
                        val id = vehicle.vehicleID
                        android.util.Log.d("NAV", "graph routes = ${navController.graph.nodes}")
                        android.util.Log.d("NAV", "navigate to add_vehicle/${id}")
                        navController.navigate("add_vehicle/${id}")
                    }
                }
            }

        }
    }
    }

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.titleLarge)
        }
    }
}



@Composable
private fun VehicleItem(
    vehicle: Vehicle,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth().clickable{ onClick()}
    ){
        Column(Modifier.padding(14.dp)){
            Text(

                "Vehicle: ${vehicle.name} ${vehicle.model}",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            val year = vehicle.year ?: "-"

            Spacer(Modifier.height(4.dp))

            Text(
                "Year: ${year}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Mileage: ${vehicle.mileage} km",
                style = MaterialTheme.typography.bodySmall
            )

        }
    }
}