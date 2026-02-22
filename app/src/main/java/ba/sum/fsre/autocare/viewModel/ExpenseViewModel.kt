package ba.sum.fsre.autocare.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ba.sum.fsre.autocare.Graph
import ba.sum.fsre.autocare.data.Expense
import ba.sum.fsre.autocare.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import androidx.lifecycle.viewModelScope
import ba.sum.fsre.autocare.Graph.expenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class ExpenseViewModel(
    private val expanseRepository: ExpenseRepository = Graph.expenseRepository
): ViewModel() {

    var dateExpenseState by mutableStateOf<Date?>(null)
    var typeExpenseState by mutableStateOf("")
    var explanationExpenseState by mutableStateOf("")
    var priceExpenseState by mutableDoubleStateOf(0.0)

    fun onDateExpenseChanged(date: Date){
        dateExpenseState = date
    }

    fun onTypeExpenseChanged(type: String){
        typeExpenseState = type
    }

    fun onExplanationExpenseChanged(explanation: String){
        explanationExpenseState = explanation
    }

    fun onPriceExpenseChanged(price: Double){
        priceExpenseState = price
    }

    lateinit var getAllExpense: Flow<List<Expense>>

    init{
        viewModelScope.launch{
            getAllExpense = expenseRepository.getAllExpense()
        }
    }

    fun addExpense(expense: Expense){
        viewModelScope.launch(Dispatchers.IO){
            expenseRepository.addExpense(expense)
        }
    }


    fun updateExpense(expense: Expense){
        viewModelScope.launch(Dispatchers.IO){
            expenseRepository.updateExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense){
        viewModelScope.launch(Dispatchers.IO){
            expenseRepository.deleteExpense(expense)
        }
    }

    fun getExpenseByID(id: Int): Flow<Expense>{
        return expenseRepository.getExpenseByID(id)
    }

}