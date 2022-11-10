package com.swirepay.android_sdk.ui.payment_activity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.PaymentLink
import com.swirepay.android_sdk.model.PaymentRequest
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import com.swirepay.android_sdk.ui.payment_activity.model.CustomerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class ViewModelPayment(
    private val amount: Int,
    private val currencyCode: String,
    val paymentMethods: List<String>,
    val customer: CustomerModel?,
    val customerGid: String?,
    val notificationType: String,
    val dueDate: String?,
) : ViewModel() {
    val livePaymentLink: MutableLiveData<PaymentLink> = MutableLiveData()
    val livePaymentResults: MutableLiveData<PaymentLink> = MutableLiveData()
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()

    init {
        fetchPaymentLink()
    }

    private fun fetchPaymentLink() = viewModelScope.launch(Dispatchers.IO) {
        val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)
        val apiClients = ApiClient.okHttpClient.interceptors()

        val paymentRequest = PaymentRequest(
            "$amount",
            currencyCode,
            paymentMethods,
            customer,
            customerGid,
            notificationType = notificationType,
            dueDate,
        )
        val response = apiClient.fetchPaymentLink(paymentRequest, SwirepaySdk.apiKey!!).execute()
        if (response.isSuccessful && response.body() != null) {
            val paymentLink = response.body()!!.entity
            Log.d("sdk_test", "fetchPaymentLink: ${response.body()!!.entity.gid}")
            livePaymentLink.postValue(paymentLink)
        } else {

            liveErrorMessages.postValue("error code : ${response.errorBody()!!.string()}")
            Log.d("sdk_test", "fetchPaymentLink: ${response.errorBody()!!.string()}")

        }
    }

    fun checkPaymentStatus() = viewModelScope.launch(Dispatchers.IO) {
        val paymentLinkId = livePaymentLink.value!!.gid

        val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)
        val response = apiClient.checkStatus(paymentLinkId).execute()
        if (response.isSuccessful && response.body() != null) {
            val paymentLink = response.body()!!.entity
            Log.d("sdk_test", "checking status: ${response.body()!!}")
            livePaymentResults.postValue(paymentLink)
        } else {
            liveErrorMessages.postValue("error code : ${response.code()}")
            Log.d("sdk_test", "fetchPaymentLink: ${response.code()}")
        }
    }
}