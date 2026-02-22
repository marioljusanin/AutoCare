package ba.sum.fsre.autocare.repository

import ba.sum.fsre.autocare.dao.ServiceDao
import ba.sum.fsre.autocare.data.Service
import kotlinx.coroutines.flow.Flow

class ServiceRepository(private val serviceDao: ServiceDao) {

    suspend fun addService(service: Service){
        serviceDao.addService(service)
    }

    suspend fun updateService(service: Service){
        serviceDao.updateService(service)
    }

    suspend fun deleteService(service: Service){
        serviceDao.deleteService(service)
    }

    fun getAllService(): Flow<List<Service>> = serviceDao.getAllService()

    fun getServiceByID(id:Int): Flow<Service>{
        return serviceDao.getServiceByID(id)
    }
}