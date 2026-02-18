package ba.sum.fsre.autocare.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ba.sum.fsre.autocare.data.User
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao {

    @Insert
    abstract fun addUser(userEntity: User)

    @Delete
    abstract fun deleteUser(userEntity: User)

    @Update
    abstract fun updateUser(userEntity: User)

    @Query("Select *from `user`")
    abstract fun gettAllUser(): Flow<List<User>>

    @Query("Select *from `user` where userID=:id")
    abstract fun getUserByID(id: Int): Flow<User>

}