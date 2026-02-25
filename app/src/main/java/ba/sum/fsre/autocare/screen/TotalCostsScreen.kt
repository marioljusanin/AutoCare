package ba.sum.fsre.autocare.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ba.sum.fsre.autocare.data.Service
import ba.sum.fsre.autocare.data.Vehicle
import ba.sum.fsre.autocare.viewModel.ServiceViewModel
import java.util.Locale

private data class CostSummary(
    val total: Double
)

private data class TypeCostPerCar(
    val type: String,
    val perCar: Map<Int, Double>,
    val total: Double
)

private fun calculateCostSummary(services: List<Service>): CostSummary {
    var total = 0.0
    for (service in services) {
        val normalized = service.price.replace(',', '.')
        val value = normalized.toDoubleOrNull() ?: continue
        total += value
    }
    return CostSummary(total)
}

private fun buildTypeCostPerCar(services: List<Service>): List<TypeCostPerCar> {
    val byType: MutableMap<String, MutableMap<Int, Double>> = mutableMapOf()
    for (service in services) {
        val normalized = service.price.replace(',', '.')
        val value = normalized.toDoubleOrNull() ?: continue
        val perCar = byType.getOrPut(service.type) { mutableMapOf() }
        perCar[service.vehicleID] = (perCar[service.vehicleID] ?: 0.0) + value
    }
    return byType.map { (type, perCar) ->
        val total = perCar.values.sum()
        TypeCostPerCar(type = type, perCar = perCar, total = total)
    }.sortedBy { it.type }
}

private fun Double.toAmountString(): String {
    if (this == 0.0) return "0"
    return if (this % 1.0 == 0.0) {
        this.toLong().toString()
    } else {
        String.format(Locale.getDefault(), "%.2f", this)
    }
}

@Composable
private fun TypeCostsGraph(
    data: List<TypeCostPerCar>,
    vehicles: List<Vehicle>
) {
    if (data.isEmpty() || vehicles.isEmpty()) return

    // Assign a distinct color per car (cycled if there are many)
    val palette = listOf(
        Color(0xFF1E88E5),
        Color(0xFFD81B60),
        Color(0xFF43A047),
        Color(0xFFFB8C00),
        Color(0xFF8E24AA),
        Color(0xFF00838F)
    )
    val carColors = remember(vehicles) {
        val map = mutableMapOf<Int, Color>()
        vehicles.forEachIndexed { index, vehicle ->
            map[vehicle.vehicleID] = palette[index % palette.size]
        }
        map
    }

    val barMaxHeight = 120.dp
    val maxTotal = data.maxOf { it.total }.coerceAtLeast(1.0)

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barMaxHeight + 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEach { typeData ->
                val heightFraction = (typeData.total / maxTotal).toFloat().coerceIn(0f, 1f)
                val barHeight = (barMaxHeight.value * heightFraction).dp
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Column(
                        modifier = Modifier
                            .width(24.dp)
                            .height(barHeight)
                    ) {
                        val segments = typeData.perCar.entries.sortedByDescending { it.value }
                        segments.forEach { (vehicleId, cost) ->
                            val segFraction =
                                if (typeData.total == 0.0) 0f else (cost / typeData.total).toFloat()
                            val segHeight = (barHeight.value * segFraction).dp
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(segHeight)
                                    .background(
                                        carColors[vehicleId]
                                            ?: MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                    )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = typeData.type,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Legend
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            vehicles.forEach { vehicle ->
                val color = carColors[vehicle.vehicleID] ?: return@forEach
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(12.dp)
                            .height(12.dp)
                            .background(color)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${vehicle.name} ${vehicle.model}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalCostsScreen(
    serviceViewModel: ServiceViewModel,
    navController: NavController
) {
    val services by serviceViewModel.getAllService.collectAsState(initial = emptyList())
    val vehicles by serviceViewModel.getAllVehicle.collectAsState(initial = emptyList())
    var selectedType by remember { mutableStateOf<String?>(null) }
    val mainTypes = listOf("Mali Servis", "Veliki Servis", "Popravak", "Gorivo")
    var selectedVehicleId by remember { mutableStateOf<Int?>(null) }
    val filteredServices = remember(services, selectedType, selectedVehicleId) {
        var current = when (selectedType) {
            null -> services
            "Others" -> services.filter { it.type !in mainTypes }
            else -> services.filter { it.type == selectedType }
        }
        if (selectedVehicleId != null) {
            current = current.filter { it.vehicleID == selectedVehicleId }
        }
        current
    }
    val costSummary = remember(filteredServices) { calculateCostSummary(filteredServices) }
    val typeCostData = remember(filteredServices) { buildTypeCostPerCar(filteredServices) }
    var filterMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Total Costs") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Overall totals
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val typeLabel = when (selectedType) {
                            null -> "All types"
                            "Others" -> "Others only"
                            else -> "\"$selectedType\" only"
                        }
                        val carLabel = selectedVehicleId?.let { id ->
                            vehicles.firstOrNull { it.vehicleID == id }?.let { v ->
                                "${v.name} ${v.model}"
                            } ?: "Selected car"
                        } ?: "All cars"
                        Column {
                            Text(
                                text = "Total costs ($typeLabel, $carLabel):",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = costSummary.total.toAmountString(),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Column {
                            IconButton(onClick = { filterMenuExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Filled.List,
                                    contentDescription = "Filter by service type"
                                )
                            }
                            DropdownMenu(
                                expanded = filterMenuExpanded,
                                onDismissRequest = { filterMenuExpanded = false }
                            ) {
                                // Type section
                                DropdownMenuItem(
                                    text = { Text("Types", style = MaterialTheme.typography.labelSmall) },
                                    enabled = false,
                                    onClick = {}
                                )
                                DropdownMenuItem(
                                    text = { Text("All types") },
                                    onClick = {
                                        selectedType = null
                                        filterMenuExpanded = false
                                    }
                                )
                                mainTypes.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type) },
                                        onClick = {
                                            selectedType = type
                                            filterMenuExpanded = false
                                        }
                                    )
                                }
                                DropdownMenuItem(
                                    text = { Text("Others") },
                                    onClick = {
                                        selectedType = "Others"
                                        filterMenuExpanded = false
                                    }
                                )

                                // Car section
                                DropdownMenuItem(
                                    text = { Text("Cars", style = MaterialTheme.typography.labelSmall) },
                                    enabled = false,
                                    onClick = {}
                                )
                                DropdownMenuItem(
                                    text = { Text("All cars") },
                                    onClick = {
                                        selectedVehicleId = null
                                        filterMenuExpanded = false
                                    }
                                )
                                if (vehicles.isEmpty()) {
                                    DropdownMenuItem(
                                        text = { Text("No cars", style = MaterialTheme.typography.bodySmall) },
                                        enabled = false,
                                        onClick = {}
                                    )
                                } else {
                                    vehicles.forEach { vehicle: Vehicle ->
                                        val labelText = "${vehicle.name} ${vehicle.model} (${vehicle.registration})"
                                        DropdownMenuItem(
                                            text = { Text(labelText) },
                                            onClick = {
                                                selectedVehicleId = vehicle.vehicleID
                                                filterMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Graph of costs per service type, colored by car
            TypeCostsGraph(
                data = typeCostData,
                vehicles = vehicles
            )

            Spacer(modifier = Modifier.height(16.dp))

            // All services list
            val listTitle = when {
                selectedType == null && selectedVehicleId == null -> "All services"
                selectedType == null && selectedVehicleId != null -> {
                    val carName = vehicles.firstOrNull { it.vehicleID == selectedVehicleId }?.let { v ->
                        "${v.name} ${v.model}"
                    } ?: "Selected car"
                    "Services for car: $carName"
                }
                selectedType == "Others" && selectedVehicleId == null -> "Services: Others"
                selectedType == "Others" && selectedVehicleId != null -> {
                    val carName = vehicles.firstOrNull { it.vehicleID == selectedVehicleId }?.let { v ->
                        "${v.name} ${v.model}"
                    } ?: "Selected car"
                    "Services: Others for car: $carName"
                }
                selectedType != null && selectedVehicleId == null -> "Services: $selectedType"
                else -> {
                    val carName = vehicles.firstOrNull { it.vehicleID == selectedVehicleId }?.let { v ->
                        "${v.name} ${v.model}"
                    } ?: "Selected car"
                    "Services: $selectedType for car: $carName"
                }
            }
            Text(
                text = listTitle,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (services.isEmpty()) {
                Text(
                    text = "No services recorded yet",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else if (filteredServices.isEmpty()) {
                Text(
                    text = "No services for selected type",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredServices) { service ->
                        ServiceCostItem(service = service)
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceCostItem(service: Service) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = service.type,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = service.price,
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = service.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Date: ${service.date}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

