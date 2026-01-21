package ba.sum.fsre.autocare.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user")
data class User(

    @PrimaryKey(autoGenerate = true)
    val userID: Int = 0,
    val name: String,
    val password: String,
    val email: String,
    val  accCreated: Date,
    val dateOfBirth: Date
)