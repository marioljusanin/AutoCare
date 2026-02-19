package ba.sum.fsre.autocare.screen

import AppBarView

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import ba.sum.fsre.autocare.R
import androidx.compose.material3.Button

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ba.sum.fsre.autocare.data.Vehicle
import ba.sum.fsre.autocare.viewModel.VehicleViewModel
import kotlinx.coroutines.launch


@Composable
fun AddVehicleScreen(
    id: Int,
    viewModel: VehicleViewModel,
    navController: NavController


){

    val snackMessage = remember{ mutableStateOf("") }

    val scope = rememberCoroutineScope ()
    val snackbarHostState = remember { SnackbarHostState() }

    if(id != 0){
        val vehicle = viewModel.getVehicleByID(id).collectAsState(Vehicle(0, "", "", "", "", ""))
        viewModel.nameVehicleState = vehicle.value.name
        viewModel.modelVehicleState = vehicle.value.model
        viewModel.yearVehicleState = vehicle.value.year
        viewModel.mileageVehicleState = vehicle.value.mileage
        viewModel.registrationVehicleState = vehicle.value.registration
    }else{
        viewModel.nameVehicleState = ""
        viewModel.modelVehicleState = ""
        viewModel.yearVehicleState = ""
        viewModel.mileageVehicleState = ""
        viewModel.registrationVehicleState = ""

    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AppBarView(
                title = if(id != 0) stringResource(id = R.string.update_vehicle)
                else stringResource(id = R.string.add_vehicle)
            , {navController.navigateUp()})

        }
    ){
      padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).
            verticalScroll(rememberScrollState()).
            padding(16.dp)
        ){
            Spacer(Modifier.height(10.dp))
            AddVehicle("Vehicle name",
                viewModel.nameVehicleState,
                {viewModel.onNameVehicleChanged(it)}
                )

            Spacer(Modifier.height(10.dp))
            AddVehicle("Vehicle model",
            viewModel.modelVehicleState,
                {viewModel.onModelVehicleChanged(it)}
                )

            Spacer(Modifier.height(10.dp))
            AddVehicle("Year of vehicle",
                viewModel.yearVehicleState,
                {viewModel.onYearVehicleChanged(it)}
            )

            Spacer(Modifier.height(10.dp))
            AddVehicle("Mileage",
                viewModel.mileageVehicleState,
                {viewModel.onMileageVehicleChanged(it)}
            )

            Spacer(Modifier.height(10.dp))
            AddVehicle("Registration",
                viewModel.registrationVehicleState,
                {viewModel.onRegistrationVehicleChanged(it)})



            Button(
                onClick = {
                    if(viewModel.nameVehicleState.isNotEmpty()
                        && viewModel.modelVehicleState.isNotEmpty()
                        && viewModel.yearVehicleState.isNotEmpty()
                        && viewModel.mileageVehicleState.isNotEmpty()
                        && viewModel.registrationVehicleState.isNotEmpty())
                    {
                        if(id != 0){
                            viewModel.updateVehicle(
                                Vehicle(
                                    id,
                                    viewModel.nameVehicleState,
                                    viewModel.modelVehicleState,
                                    viewModel.yearVehicleState,
                                    viewModel.mileageVehicleState,
                                    viewModel.registrationVehicleState
                                ))
                                snackMessage.value = "Updates has been saved"


                        }else{
                            //KREIRANJE NOVOG VOZILA
                            viewModel.addVehicle(
                                Vehicle(

                                    name = viewModel.nameVehicleState.trim(),
                                    model = viewModel.modelVehicleState.trim(),
                                    year = viewModel.yearVehicleState.trim(),
                                    mileage = viewModel.mileageVehicleState.trim(),
                                    registration = viewModel.registrationVehicleState.trim(),

                                ))
                            snackMessage.value = "Vehicle has been added"
                        }

                    }
                    else{
                        snackMessage.value = "Entry all fields to add a new vehicle"
                    }
                    scope.launch {
                        snackbarHostState.showSnackbar(snackMessage.value)
                        navController.navigateUp()
                    }
                },

            ){
                Text("SAVE")
            }

        }
    }

}




@Composable
fun AddVehicle(
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


