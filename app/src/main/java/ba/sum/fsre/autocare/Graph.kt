package ba.sum.fsre.autocare

import android.content.Context
import androidx.room.Room
import ba.sum.fsre.autocare.dao.VehicleDao
import ba.sum.fsre.autocare.data.AppDatabase
import ba.sum.fsre.autocare.repository.ExpenseRepository
import ba.sum.fsre.autocare.repository.ReminderRepository
import ba.sum.fsre.autocare.repository.ServiceRepository
import ba.sum.fsre.autocare.repository.UserRepository
import ba.sum.fsre.autocare.repository.VehicleRepository

object Graph {
    lateinit var database: AppDatabase

    val vehicleRepository by lazy{
        VehicleRepository(vehicleDao = database.vehicleDao())
    }

    val userRepository by lazy{
        UserRepository(userDao = database.userDao())
    }

    val serviceRepositry by lazy{
        ServiceRepository(serviceDao = database.serviceDao())
    }

    val ReminderRepository by lazy{
        ReminderRepository( reminderDao = database.reminderDao())
    }

    val ExpenseRepository by lazy{
        ExpenseRepository(expenseDao = database.expenseDao())
    }


    fun provide(context: Context){
        database = Room.databaseBuilder(context, AppDatabase::class.java, "autocare.db").fallbackToDestructiveMigration()
            .build()
    }

}