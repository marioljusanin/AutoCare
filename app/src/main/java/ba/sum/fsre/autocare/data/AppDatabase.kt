package ba.sum.fsre.autocare.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
      User::class,
      Vehicle::class,
      Service::class,
      Expense::class,
      Reminder::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // Ovdje kasnije dodaš DAO-e, npr.:
    // abstract fun korisnikDao(): KorisnikDao
}