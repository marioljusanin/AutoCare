package ba.sum.fsre.autocare.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ba.sum.fsre.autocare.data.Vehicle
import java.util.Date

@Entity(
    tableName = "service",
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
data class Service(

    @PrimaryKey(autoGenerate = true)
    val serviceID: Int = 0,

    val date: Date,
    val description: String,
    val type: String,
    val price: Double,

    val vehicleID: Int        // FK na Vozilo
)