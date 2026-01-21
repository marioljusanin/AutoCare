package ba.sum.fsre.autocare.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ba.sum.fsre.autocare.data.Service
import ba.sum.fsre.autocare.data.Vehicle
import java.util.Date

@Entity(
    tableName = "expense",
    foreignKeys = [
        ForeignKey(
            entity = Service::class,
            parentColumns = ["serviceID"],
            childColumns = ["serviceID"],
            onDelete = ForeignKey.Companion.CASCADE
        ),
        ForeignKey(
            entity = Vehicle::class,
            parentColumns = ["vehicleID"],
            childColumns = ["vehicleID"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("serviceID"), Index("vehicleID")]
)
data class Expense(

    @PrimaryKey(autoGenerate = true)
    val expenseID: Int = 0,

    val date: Date,
    val type: String,
    val Explanation: String,
    val price: Double,

    val serviceID: Int,       // FK na Servis
    val vehicleID: Int        // FK na Vozilo (općeniti trošak za vozilo)
)