package ba.sum.fsre.autocare.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ba.sum.fsre.autocare.data.Vehicle
import kotlinx.coroutines.flow.Flow

@Dao
abstract class VehicleDao {

    @Insert
    abstract fun addVehicle(vehicleEntity: Vehicle)

    @Update
    abstract fun updateVehicle(vehicleEntity: Vehicle)

    @Query("Select *from `vehicle`")
    abstract fun getAllVehicle(): Flow<List<Vehicle>>

    @Delete
    abstract fun deleteVehicle(vehicleEntity: Vehicle)

    @Query("Select *from `vehicle` where vehicleID=:id")
    abstract fun getVehicleByID(id:Int): Flow<Vehicle>



}