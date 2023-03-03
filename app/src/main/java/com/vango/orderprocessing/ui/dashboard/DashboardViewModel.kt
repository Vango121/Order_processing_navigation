package com.vango.orderprocessing.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vango.orderprocessing.remote.DataRepository
import com.vango.orderprocessing.remote.Medicine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    val prescriptionMedicines: MutableLiveData<List<Medicine>> = MutableLiveData(emptyList())
    val nonPrescriptionMedicines: MutableLiveData<List<Medicine>> = MutableLiveData(emptyList())

    fun fetchDataFromApi() {
        viewModelScope.launch {
            prescriptionMedicines.postValue(listOf())
            nonPrescriptionMedicines.postValue(listOf())
            Timber.d("fetch data")
            val medicines = dataRepository.getAllMedicines()
            prescriptionMedicines.postValue(medicines?.filter { it.onPrescription && it.productStock > 0 })
            nonPrescriptionMedicines.postValue(medicines?.filter { !it.onPrescription && it.productStock > 0})
        }
    }


}