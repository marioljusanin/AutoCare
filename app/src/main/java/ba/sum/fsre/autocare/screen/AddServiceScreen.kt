package ba.sum.fsre.autocare.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ba.sum.fsre.autocare.data.Service
import ba.sum.fsre.autocare.data.Vehicle
import ba.sum.fsre.autocare.viewModel.ServiceViewModel
import kotlinx.coroutines.launch
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceScreen(
    navController: NavController,
    viewModel: ServiceViewModel
) {
    val vehicles by viewModel.getAllVehicle.collectAsState(initial = emptyList())
    val snackMessage = remember{ mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope ()
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Add Service") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))
            AddService("Enter date(dd.MM.yyyy)",
                viewModel.dateServiceState,
                { viewModel.onDateServiceChanged(it) }
            )
            Spacer(Modifier.height(10.dp))
            AddService("Description",
                viewModel.descriptionServiceState,
                {viewModel.onDescriptionServiceChanged(it)}
            )

            Spacer(Modifier.height(10.dp))
            ServiceTypeDropDown(
                selectedType = viewModel.typeServiceState,
                onTypeSelected = { viewModel.onTypeServiceChanged(it) }
            )

            Spacer(Modifier.height(10.dp))
            AddService("Price",
                viewModel.priceServiceState ,
                {viewModel.onPriceServiceChanged(it)}
            )

            VehicleDropDown(
                vehicles,
                viewModel.selectedVehicleByID,
                { viewModel.onVehicleSelected(it) }
            )
            Spacer(Modifier.height(10.dp))

            val vID = viewModel.selectedVehicleByID
            Button(onClick = {
                if (vID == null) {
                    scope.launch { snackbarHostState.showSnackbar("Select vehicle first") }
                    return@Button
                }
                if (viewModel.typeServiceState.isBlank()) {
                    scope.launch { snackbarHostState.showSnackbar("Select service type") }
                    return@Button
                }

                viewModel.addService(
                    Service(
                        date = viewModel.dateServiceState,
                        description = viewModel.descriptionServiceState,
                        type = viewModel.typeServiceState,
                        price = viewModel.priceServiceState,
                        vehicleID = vID
                    )
                )
                scope.launch{
                    snackbarHostState.showSnackbar("Service has been added")
                    navController.navigateUp()
                }

            }){
                Text("SAVE")
            }


        }
    }
}


@Composable
fun AddService(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
)
{
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = {Text(label)},
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )

}

private val SERVICE_TYPES = listOf(
    "Mali Servis",
    "Veliki Servis",
    "Popravak",
    "Gorivo"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceTypeDropDown(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedType,
            onValueChange = {},
            readOnly = true,
            label = { Text("Service type") },
            placeholder = { Text("Select type") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SERVICE_TYPES.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDropDown(
  vehicles: List<Vehicle>,
  selectedVehicleID: Int?,
  onSelect: (Int)->Unit
){
    var expanded by remember { mutableStateOf(false) }

    val selectedText = vehicles.firstOrNull{ it.vehicleID == selectedVehicleID} ?.let{
        "${it.name} ${it.model} (${it.registration})"} ?: ""


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = !expanded}
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = {Text("Select vehicle")},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            vehicles.forEach {
                vehicle ->
                DropdownMenuItem(
                    text = {Text("${vehicle.name} ${vehicle.model} (${vehicle.registration})")},
                    onClick = {onSelect(vehicle.vehicleID)
                            expanded = false}
                    )
            }
        }
    }

}
