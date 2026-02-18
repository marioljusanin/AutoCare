package ba.sum.fsre.autocare.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ba.sum.fsre.autocare.data.Service
import kotlinx.coroutines.flow.Flow


@Dao
abstract class ServiceDao {


    @Insert
    abstract fun addService(serviceEntity: Service)

    @Delete
    abstract fun deleteService(serviceEntity: Service)

    @Update
    abstract fun updateService(serviceEntity: Service)

    @Query("Select *from `service`")
    abstract fun getAllService(): Flow<List<Service>>

    @Query("Select *from `service` where serviceID=:id")
    abstract fun getServiceByID(id:Int): Flow<Service>

}