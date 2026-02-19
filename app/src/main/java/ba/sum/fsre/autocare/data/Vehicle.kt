package ba.sum.fsre.autocare.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vehicle"
   /* foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userID"],
            childColumns = ["userID"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("userID")]*/
)

data class Vehicle(

    @PrimaryKey(autoGenerate = true)
    val vehicleID: Int = 0,

    val name: String,
    val model: String,
    val year: String,
    val mileage: String,
    val registration: String,

    //val userID: Int = 0

)
