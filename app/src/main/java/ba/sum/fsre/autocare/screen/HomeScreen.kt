package ba.sum.fsre.autocare.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ba.sum.fsre.autocare.data.DummyVehicle
import ba.sum.fsre.autocare.data.DummyVehicle.vehicle
import ba.sum.fsre.autocare.data.Vehicle




@Composable
fun HomeScreen(
   /* totalCosts: String,
    nextService: String,
    vehicles: List<Vehicle> = emptyList(),

    onAddVehicle: () -> Unit = {},
    odAddServiceOrExpense: () -> Unit = {},
    onVehicleClick: () -> Unit = {}

    */
){




    Column(
        modifier = Modifier.fillMaxSize().background(color = White),
        horizontalAlignment = Alignment.CenterHorizontally


    ){
        Text("Quick Stats")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
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
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
            ){
            Text("My Vehicles", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))

            FilledTonalIconButton(onClick = {}) {
                Icon(Icons.Default.Build, contentDescription = "Add service/expense")
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "Add vehicle")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

       /* if(vehicles.isEmpty()){
            Card(
              modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.surfaceVariant)
            ){
                Column(
                    modifier = Modifier.padding(16.dp)
                ){
                    Text("No vehicles for now")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("You can add vehicles on button + ", style = MaterialTheme.typography.bodySmall)
                }
            }
        }else{
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 10.dp)
            ){

            }
        }

            */

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)){
            items(DummyVehicle.vehicle){
                    vehicle -> VehicleItem (vehicle = vehicle) {

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

@Preview
@Composable
fun HomeScreenPreview(){
    //HomeScreen()
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
               // "${vehicle.make} ${vehicle.mode}",
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