package ba.sum.fsre.autocare.screen

sealed class Screen(route: String) {

    object HomeScreen: Screen("home_screen")
    object AddVehicleScreen: Screen("add_vehicle")
}