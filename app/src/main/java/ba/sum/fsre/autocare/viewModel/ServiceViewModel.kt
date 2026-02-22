package ba.sum.fsre.autocare.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.sum.fsre.autocare.Graph
import ba.sum.fsre.autocare.data.Service
import ba.sum.fsre.autocare.data.Vehicle
import ba.sum.fsre.autocare.repository.ServiceRepository
import ba.sum.fsre.autocare.repository.VehicleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date

class ServiceViewModel(
    private val serviceRepository: ServiceRepository = Graph.serviceRepositry,
    private val vehicleRepository: VehicleRepository = Graph.vehicleRepository
): ViewModel() {

    var dateServiceState by mutableStateOf("")
    var descriptionServiceState by mutableStateOf("")
    var typeServiceState by mutableStateOf("")
    var priceServiceState by mutableStateOf("")
    var selectedVehicleByID by mutableStateOf(0)

    fun onDateServiceChanged(date: String){
        dateServiceState = date
    }

    fun onDescriptionServiceChanged(description: String){
        descriptionServiceState = description
    }

    fun onTypeServiceChanged(type: String){
        typeServiceState = type
    }

    fun onPriceServiceChanged(price: String){
        priceServiceState = price
    }

    fun onVehicleSelected(id: Int){
        selectedVehicleByID = id
    }

    lateinit var getAllVehicle: Flow<List<Vehicle>>
    init{
        viewModelScope.launch{
            getAllVehicle= vehicleRepository.getAllVehicle()
        }
    }
    lateinit var getAllService: Flow<List<Service>>

    init{
        viewModelScope.launch{
            getAllService = serviceRepository.getAllService()
        }
    }

    fun addService(service: Service){
        viewModelScope.launch(Dispatchers.IO){
            serviceRepository.addService(service)
        }
    }

    fun updateService(service: Service){
        viewModelScope.launch(Dispatchers.IO){
            serviceRepository.updateService(service)
        }
    }

    fun deleteService(service: Service){
        viewModelScope.launch(Dispatchers.IO){
            serviceRepository.deleteService(service)
        }
    }

    fun getServiceByID(id: Int): Flow<Service>{
        return serviceRepository.getServiceByID(id)
    }
}