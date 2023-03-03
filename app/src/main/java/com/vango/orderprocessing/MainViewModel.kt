package com.vango.orderprocessing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vango.orderprocessing.auth.network.AuthRepository
import com.vango.orderprocessing.auth.models.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    var livedata: MutableLiveData<AuthResult<Unit>> = MutableLiveData()

    fun signUp(username: String, password: String) {
        if (checkIfCredentialsAreValid(username, password)) {
            viewModelScope.launch {
                val result = repository.signUp(username, password, "CLIENT")
                livedata.postValue(result)
            }
        }
    }

    fun checkIfCredentialsAreValid(username: String, password: String) =
        (username.isNotBlank() && password.isNotBlank() && username.length > 4 && password.length > 4)

    fun signIn(username: String, password: String) {
        if (checkIfCredentialsAreValid(username, password)) {
            viewModelScope.launch {
                val result = repository.signIn(username, password)
                repository.authenticate()
                livedata.postValue(result)
            }
        }
    }

    fun authenticate() {
        viewModelScope.launch {
            val result = repository.authenticate()
            livedata.postValue(result)
        }
    }
}