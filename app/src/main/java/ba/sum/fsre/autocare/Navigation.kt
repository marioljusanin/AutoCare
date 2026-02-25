package ba.sum.fsre.autocare

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import ba.sum.fsre.autocare.viewModel.VehicleViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ba.sum.fsre.autocare.screen.AddServiceScreen
import ba.sum.fsre.autocare.screen.AddVehicleScreen
import ba.sum.fsre.autocare.screen.CarServicesScreen
import ba.sum.fsre.autocare.screen.EditServiceScreen
import ba.sum.fsre.autocare.screen.HomeScreen
import ba.sum.fsre.autocare.screen.Screen
import ba.sum.fsre.autocare.viewModel.ServiceViewModel
import java.util.Map.entry


@Composable
fun Navigation(
    viewModel: VehicleViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    serviceViewModel: ServiceViewModel = viewModel()
){
    NavHost(
        navController = navController,
        startDestination = "home_screen"
    ){
        composable("home_screen"){
            HomeScreen(viewModel, serviceViewModel, navController)
        }
        composable("add_service"){
            AddServiceScreen(navController, serviceViewModel)
        }
        composable("add_vehicle/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                }
            )
            ){ entry->
            val id = if(entry.arguments != null) entry.arguments!!.getInt("id") else 0
            AddVehicleScreen(id, viewModel, navController)
        }
        composable(
            "car_services/{vehicleId}",
            arguments = listOf(
                navArgument("vehicleId") {
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                }
            )
        ) { entry ->
            val vehicleId = entry.arguments?.getInt("vehicleId") ?: 0
            CarServicesScreen(
                vehicleId = vehicleId,
                vehicleViewModel = viewModel,
                serviceViewModel = serviceViewModel,
                navController = navController
            )
        }
        composable(
            "edit_service/{serviceId}",
            arguments = listOf(
                navArgument("serviceId") {
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                }
            )
        ) { entry ->
            val serviceId = entry.arguments?.getInt("serviceId") ?: 0
            EditServiceScreen(
                serviceId = serviceId,
                navController = navController,
                viewModel = serviceViewModel
            )
        }
    }
}







