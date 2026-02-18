package ba.sum.fsre.autocare.repository

import ba.sum.fsre.autocare.dao.ReminderDao
import ba.sum.fsre.autocare.data.Reminder
import kotlinx.coroutines.flow.Flow

class ReminderRepository(private val reminderDao: ReminderDao) {

    suspend fun addReminder(reminder: Reminder){
        reminderDao.addReminder(reminder)
    }

    suspend fun updateReminder(reminder: Reminder){
        reminderDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder){
        reminderDao.deleteReminder(reminder)
    }

    fun getAllReminder(): Flow<List<Reminder>> = reminderDao.getAllReminder()

    fun getReminderByID(id: Int): Flow<Reminder>{
        return reminderDao.getReminderByID(id)
    }
}