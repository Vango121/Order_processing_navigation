package com.vango.orderprocessing.ui.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vango.orderprocessing.remote.DataRepository
import com.vango.orderprocessing.remote.Medicine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    private var medicineList: List<Medicine> = emptyList()
    val onPrescription = MutableLiveData<Boolean>()
    val productsList = MutableLiveData<List<Medicine>>()
    private lateinit var apiJob: Job

    fun fetchDataFromApi() {
        apiJob = viewModelScope.launch {
            productsList.postValue(listOf())
            Timber.d("fetch data")
            dataRepository.getAllMedicines()?.let {
                medicineList = it
            }
        }
    }

    private fun prepareDataForView(onPrescription: Boolean) {
        if (apiJob.isActive) {
            viewModelScope.launch {
                apiJob.join()
                filterProducts(onPrescription)
            }
        } else {
            filterProducts(onPrescription)
        }
    }

    private fun filterProducts(onPrescription: Boolean) {
        productsList.postValue(medicineList.filter { it.onPrescription == onPrescription })
    }

    fun setPrescriptionFlag(flag: Boolean) {
        onPrescription.postValue(flag)
        prepareDataForView(flag)
    }

    fun prescriptionFilterOnClick() {
        val onPrescriptionFilter = onPrescription.value
        onPrescriptionFilter?.let {
            onPrescription.postValue(!it)
            productsList.postValue(listOf())
            prepareDataForView(!it)
        }
    }
}