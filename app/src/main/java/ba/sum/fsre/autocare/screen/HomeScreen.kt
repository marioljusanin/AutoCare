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
import androidx.compose.material.icons.filled.List
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import ba.sum.fsre.autocare.data.Service
import ba.sum.fsre.autocare.data.Vehicle
import ba.sum.fsre.autocare.viewModel.ServiceViewModel
import ba.sum.fsre.autocare.viewModel.VehicleViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: VehicleViewModel,
    serviceViewModel: ServiceViewModel,
    navController: NavController
) {


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
            val services by serviceViewModel.getAllService.collectAsState(initial = emptyList())
            val nextServiceInfo = remember(services) { computeNextServiceInfo(services) }
            val totalCostsText = remember(services) { computeTotalCostsText(services) }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                StatCard(
                    title = "Total Costs",
                    value = totalCostsText,
                    modifier = Modifier.weight(1f),
                    onClick = if (services.isNotEmpty()) {
                        { navController.navigate("total_costs") }
                    } else {
                        null
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                StatCard(
                    title = "Next Service",
                    value = nextServiceInfo.text,
                    modifier = Modifier.weight(1f),
                    onClick = nextServiceInfo.service?.let { s ->
                        { navController.navigate("edit_service/${s.serviceID}") }
                    }
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
                    VehicleItem(
                        vehicle = vehicle,
                        onVehicleClick = {
                            val id = vehicle.vehicleID
                            android.util.Log.d("NAV", "graph routes = ${navController.graph.nodes}")
                            android.util.Log.d("NAV", "navigate to add_vehicle/${id}")
                            navController.navigate("add_vehicle/${id}")
                        },
                        onViewServicesClick = {
                            navController.navigate("car_services/${vehicle.vehicleID}")
                        }
                    )
                }
            }

        }
    }
    }

private data class NextServiceInfo(val text: String, val service: Service?)

private fun computeNextServiceInfo(services: List<Service>): NextServiceInfo {
    if (services.isEmpty()) return NextServiceInfo(" - ", null)
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val todayMillis = today.timeInMillis
    var minDays: Long? = null
    var nextService: Service? = null
    for (service in services) {
        try {
            val parsed = dateFormat.parse(service.date) ?: continue
            val serviceCal = Calendar.getInstance().apply {
                time = parsed
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val serviceMillis = serviceCal.timeInMillis
            if (serviceMillis >= todayMillis) {
                val days = (serviceMillis - todayMillis) / (24 * 60 * 60 * 1000)
                if (minDays == null || days < minDays) {
                    minDays = days
                    nextService = service
                }
            }
        } catch (_: Exception) {
        }
    }
    val text = when {
        minDays == null -> " - "
        minDays == 0L -> "Today"
        minDays == 1L -> "Tomorrow"
        else -> "$minDays days"
    }
    return NextServiceInfo(text, nextService)
}

private fun computeTotalCostsText(services: List<Service>): String {
    if (services.isEmpty()) return "0"
    var total = 0.0
    for (service in services) {
        // Allow both "100" and "100,50" formats
        val normalized = service.price.replace(',', '.')
        val value = normalized.toDoubleOrNull() ?: continue
        total += value
    }
    if (total == 0.0) return "0"
    // Show without decimals when it's a whole number, otherwise two decimals
    return if (total % 1.0 == 0.0) {
        total.toLong().toString()
    } else {
        String.format(Locale.getDefault(), "%.2f", total)
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        ),
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
    onVehicleClick: () -> Unit,
    onViewServicesClick: () -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onVehicleClick() }
    ){
        Column(Modifier.padding(14.dp)){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Vehicle: ${vehicle.name} ${vehicle.model}",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    val year = vehicle.year
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
                FilledTonalIconButton(
                    onClick = onViewServicesClick
                ) {
                    Icon(Icons.Default.List, contentDescription = "View all services for this car")
                }
            }
        }
    }
}