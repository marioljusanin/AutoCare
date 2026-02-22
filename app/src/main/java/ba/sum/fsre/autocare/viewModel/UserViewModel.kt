package ba.sum.fsre.autocare.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.sum.fsre.autocare.Graph
import ba.sum.fsre.autocare.data.User
import ba.sum.fsre.autocare.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date

class UserViewModel(
    private val userRepository: UserRepository = Graph.userRepository
): ViewModel() {

    var nameUserState by mutableStateOf("")
    var passwordUserState by  mutableStateOf("")
    var emailUserState by mutableStateOf("")
    var accCreated by mutableStateOf<Date?>(null)
    var dateOfBirth by mutableStateOf<Date?>(null)

    fun onNameUserChanged(name: String){
        nameUserState = name
    }

    fun onPasswordUserChanged(password: String){
        passwordUserState = password
    }

    fun onEmailUserChanged(email: String){
        emailUserState = email
    }

    fun onAccCreatedChanged(date: Date){
        accCreated = date
    }

    fun onDateOfBirthChanged(date: Date){
        dateOfBirth = date
    }


    lateinit var getAllUser: Flow<List<User>>

    init{
        viewModelScope.launch(Dispatchers.IO){
            getAllUser = userRepository.getAllUser()
        }
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO){
            userRepository.addUser(user)
        }
    }


    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO){
            userRepository.updateUser(user)
        }
    }

    fun deleteUser(user: User){
        viewModelScope.launch(Dispatchers.IO){
            userRepository.deleteUser(user)
        }
    }

    fun getUserByID(id: Int): Flow<User>{
        return userRepository.getUserByID(id)
    }
}