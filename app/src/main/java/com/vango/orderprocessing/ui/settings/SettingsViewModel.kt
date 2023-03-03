package com.vango.orderprocessing.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vango.orderprocessing.auth.EncryptedPrefs
import com.vango.orderprocessing.auth.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val encryptedPrefs: EncryptedPrefs,
    val userService: UserService
) : ViewModel() {

    var logOutEvent: MutableLiveData<Boolean> = MutableLiveData()

    fun logOut() {
        encryptedPrefs.clear()
        logOutEvent.postValue(true)
    }
}