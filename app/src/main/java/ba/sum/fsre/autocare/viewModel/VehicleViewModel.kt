package ba.sum.fsre.autocare.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.sum.fsre.autocare.Graph
import ba.sum.fsre.autocare.data.Vehicle
import ba.sum.fsre.autocare.repository.VehicleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class VehicleViewModel(
    private val vehicleRepository: VehicleRepository = Graph.vehicleRepository
): ViewModel() {

    var nameVehicleState by mutableStateOf("")
    var modelVehicleState by mutableStateOf("")
    var yearVehicleState by mutableStateOf("")
    var mileageVehicleState by mutableStateOf("")
    var registrationVehicleState by mutableStateOf("")

    fun onNameVehicleChanged(newName: String){
        nameVehicleState = newName
    }

    fun onModelVehicleChanged(newModel: String){
        modelVehicleState = newModel
    }

    fun onYearVehicleChanged(newYear: String){
        yearVehicleState = newYear
    }

    fun onMileageVehicleChanged(newMileage: String){
        mileageVehicleState = newMileage
    }

    fun onRegistrationVehicleChanged(newRegistration: String){
        registrationVehicleState = newRegistration
    }

    lateinit var getAllVehicles: Flow<List<Vehicle>>

    init{
        viewModelScope.launch{
            getAllVehicles = vehicleRepository.getAllVehicle()
        }
    }

    fun addVehicle(vehicle: Vehicle){
        viewModelScope.launch(Dispatchers.IO){
            vehicleRepository.addVehicle(vehicle)
        }
    }

    fun updateVehicle(vehicle: Vehicle){
        viewModelScope.launch(Dispatchers.IO){
            vehicleRepository.updateVehicle(vehicle)
        }
    }

    fun deleteVehicle(vehicle: Vehicle){
        viewModelScope.launch(Dispatchers.IO){
            vehicleRepository.deleteVehicle(vehicle)
        }
    }

    fun getVehicleByID(id:Int): Flow<Vehicle>{
        return vehicleRepository.getVehicleByID(id)
    }


}