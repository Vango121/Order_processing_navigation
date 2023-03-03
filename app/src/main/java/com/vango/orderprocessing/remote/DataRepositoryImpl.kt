package com.vango.orderprocessing.remote

import com.vango.orderprocessing.utils.Const
import com.vango.orderprocessing.auth.EncryptedPrefs
import com.vango.orderprocessing.auth.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val api: DataService,
    private val encryptedPrefs: EncryptedPrefs,
    private val userService: UserService
) : BaseDataSource(), DataRepository {

    override suspend fun getAllMedicines(): List<Medicine>? = withContext(Dispatchers.IO) {
        Timber.e("get all medicines")
        val token = encryptedPrefs.getString(Const.SHARED_PREF_JWT_TOKEN)
        getResult { api.getAllMedicines("Bearer $token") }
    }

    override suspend fun createCart(cartItem: QrObject) {
        val token = encryptedPrefs.getString(Const.SHARED_PREF_JWT_TOKEN)
        getResult { api.createCart("Bearer $token", cartItem) }
    }

    override suspend fun getOrders(): List<OrderId>? = withContext(Dispatchers.IO){
        val token = encryptedPrefs.getString(Const.SHARED_PREF_JWT_TOKEN)
        getResult { api.getOrders("Bearer $token", userService.userId ?: "") }
    }

    override suspend fun getAllOrders(): List<OrderId>? = withContext(Dispatchers.IO) {
        val token = encryptedPrefs.getString(Const.SHARED_PREF_JWT_TOKEN)
        getResult { api.getAllOrders("Bearer $token") }
    }

    override suspend fun changeOrderStatus(orderId: String): Unit = withContext(Dispatchers.IO) {
        val token = encryptedPrefs.getString(Const.SHARED_PREF_JWT_TOKEN)
        getResult { api.changeOrderStatus("Bearer $token", orderId) }
    }

}