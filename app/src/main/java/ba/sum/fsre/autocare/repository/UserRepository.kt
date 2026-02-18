package ba.sum.fsre.autocare.repository

import ba.sum.fsre.autocare.dao.UserDao
import kotlinx.coroutines.flow.Flow
import ba.sum.fsre.autocare.data.User

class UserRepository(private val userDao: UserDao) {

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    fun getAllUser(): Flow<List<User>> = userDao.gettAllUser()

    fun getUserByID(id:Int): Flow<User>{
    return userDao.getUserByID(id)
    }
}