package ba.sum.fsre.autocare.repository

import ba.sum.fsre.autocare.dao.ExpenseDao
import ba.sum.fsre.autocare.data.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository (private val expenseDao: ExpenseDao){

    suspend fun addExpense(expense: Expense){
        expenseDao.addExpense(expense)
    }

    suspend fun updateExpense(expense: Expense){
        expenseDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense){
        expenseDao.deleteExpense(expense)
    }

    fun getAllExpense(): Flow<List<Expense>> = expenseDao.getAllExpense()

    fun getExpenseByID(id: Int): Flow<Expense>{
        return expenseDao.getExpenseByID(id)
    }
}