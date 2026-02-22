package ba.sum.fsre.autocare.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.sum.fsre.autocare.Graph
import ba.sum.fsre.autocare.data.Reminder
import ba.sum.fsre.autocare.repository.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date

class ReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository
): ViewModel() {


    var nameReminderState by mutableStateOf("")
    var explanationReminderState by mutableStateOf("")
    var dateReminderState by mutableStateOf<Date?>(null)
    var doneReminderState by mutableStateOf<Boolean?>(null)


    fun onNameReminderChanged(name: String){
        nameReminderState = name
    }

    fun onExplanationReminderChanged(explanation: String){
        explanationReminderState = explanation
    }

    fun onDateReminderChanged(date: Date){
        dateReminderState = date
    }

    fun onDoneReminderChanged(done: Boolean){
        doneReminderState = done
    }

    lateinit var getAllReminder: Flow<List<Reminder>>

    init{
        viewModelScope.launch(Dispatchers.IO){
            getAllReminder = reminderRepository.getAllReminder()
        }
    }

    fun addReminder(reminder: Reminder){
        viewModelScope.launch(Dispatchers.IO){
            reminderRepository.addReminder(reminder)
        }
    }

    fun updateReminder(reminder: Reminder){
        viewModelScope.launch(Dispatchers.IO){
            reminderRepository.updateReminder(reminder)
        }
    }

    fun deleteReminder(reminder: Reminder){
        viewModelScope.launch(Dispatchers.IO){
            reminderRepository.deleteReminder(reminder)
        }
    }

    fun getReminderByID(id: Int): Flow<Reminder>{
        return reminderRepository.getReminderByID(id)
    }
}