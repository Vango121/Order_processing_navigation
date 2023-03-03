package com.vango.orderprocessing.ui.pharmaceutist.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vango.orderprocessing.remote.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardPharmeceutistViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {

    val numberOfProducts: MutableLiveData<Int> = MutableLiveData(0)
    val numberOfOrders: MutableLiveData<Int> = MutableLiveData(0)


    init {
        viewModelScope.launch {
            Timber.d("fetch data")
            val medicines = dataRepository.getAllMedicines()
            numberOfProducts.postValue(medicines?.size)

            val orders = dataRepository.getAllOrders()
            numberOfOrders.postValue(orders?.size)
        }
    }


}