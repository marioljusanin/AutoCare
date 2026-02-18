package ba.sum.fsre.autocare.repository

import kotlinx.coroutines.flow.Flow
import ba.sum.fsre.autocare.dao.VehicleDao
import ba.sum.fsre.autocare.data.Vehicle

class VehicleRepository(private val vehicleDao: VehicleDao) {

    suspend fun addVehicle(vehicle: Vehicle) {
        vehicleDao.addVehicle(vehicle)
    }

    fun getAllVehicle(): Flow<List<Vehicle>> = vehicleDao.getAllVehicle()

    fun getVehicleByID(id: Int): Flow<Vehicle>{
        return vehicleDao.getVehicleByID(id)
    }

    suspend fun updateVehicle(vehicle: Vehicle){
        vehicleDao.updateVehicle(vehicle)
    }

    suspend fun deleteVehicle(vehicle: Vehicle){
        vehicleDao.deleteVehicle(vehicle)
    }
}