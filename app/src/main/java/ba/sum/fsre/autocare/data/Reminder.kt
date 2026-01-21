package ba.sum.fsre.autocare.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ba.sum.fsre.autocare.data.Vehicle
import java.util.Date

@Entity(
    tableName = "reminder",
    foreignKeys = [
        ForeignKey(
            entity = Vehicle::class,
            parentColumns = ["vehicleID"],
            childColumns = ["vehicleID"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("vehicleID")]
)
data class Reminder(

    @PrimaryKey(autoGenerate = true)
    val reminderID: Int = 0,

    val name: String,
    val Explanation: String,
    val date: Date,
    val done: Boolean,

    val vehicleID: Int        // FK na Vozilo
)