package ba.sum.fsre.autocare.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ba.sum.fsre.autocare.dao.ExpenseDao
import ba.sum.fsre.autocare.dao.ReminderDao
import ba.sum.fsre.autocare.dao.ServiceDao
import ba.sum.fsre.autocare.dao.UserDao
import ba.sum.fsre.autocare.dao.VehicleDao

@Database(
    entities = [
      User::class,
      Vehicle::class,
      Service::class,
      Expense::class,
      Reminder::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {



    abstract fun vehicleDao(): VehicleDao

    abstract fun userDao(): UserDao

    abstract fun serviceDao(): ServiceDao

    abstract fun reminderDao(): ReminderDao

    abstract fun expenseDao(): ExpenseDao

}