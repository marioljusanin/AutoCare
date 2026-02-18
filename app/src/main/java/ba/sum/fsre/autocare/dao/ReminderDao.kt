package ba.sum.fsre.autocare.dao

import androidx.core.view.WindowInsetsCompat
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ba.sum.fsre.autocare.data.Reminder
import kotlinx.coroutines.flow.Flow


@Dao
abstract class ReminderDao {

    @Insert
    abstract fun addReminder(reminderEntity: Reminder)

    @Delete
    abstract fun deleteReminder(reminderEntity: Reminder)

    @Update
    abstract fun updateReminder(reminderEntity: Reminder)

    @Query("Select *from `reminder`")
    abstract fun getAllReminder(): Flow<List<Reminder>>

    @Query("Select *from `reminder` where reminderID=:id")
    abstract fun getReminderByID(id:Int): Flow<Reminder>
}