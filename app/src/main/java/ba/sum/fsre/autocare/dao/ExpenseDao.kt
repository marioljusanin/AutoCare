package ba.sum.fsre.autocare.dao

import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ba.sum.fsre.autocare.data.Expense


@Dao
abstract class ExpenseDao {


    @Insert
    abstract fun addExpense(expenseEntity: Expense)

    @Update
    abstract fun updateExpense(expenseEntity: Expense)

    @Delete
    abstract fun deleteExpense(expenseEntity: Expense)

    @Query("Select *from `expense`")
    abstract fun getAllExpense(): Flow<List<Expense>>

    @Query("Select *from `expense` where expenseID=:id")
    abstract fun getExpenseByID(id:Int): Flow<Expense>



}