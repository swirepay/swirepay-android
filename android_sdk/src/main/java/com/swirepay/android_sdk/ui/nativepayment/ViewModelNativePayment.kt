package com.swirepay.android_sdk.ui.nativepayment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.BuildConfig
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.PaymentLink
import com.swirepay.android_sdk.model.PaymentRequest
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import com.swirepay.android_sdk.ui.payment_activity.model.CustomerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelNativePayment(

) : ViewModel() {
    val livePaymentLink: MutableLiveData<PaymentLink> = MutableLiveData()
    val livePaymentResults: MutableLiveData<PaymentLink> = MutableLiveData()
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()

//    init {
//        fetchPaymentLink(paymentRequest)
//    }

    fun fetchPaymentLink(paymentRequest: PaymentRequest) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.fetchPaymentLink(paymentRequest, SwirepaySdk.apiKey!!).execute()
            if (response.isSuccessful && response.body() != null) {
                val paymentLink = response.body()!!.entity
                Log.d("sdk_test", "fetchPaymentLink: ${response.body()!!.entity.gid}")
                livePaymentLink.postValue(paymentLink)
            } else {
                liveErrorMessages.postValue("error code : ${response.code()}")
                Log.d("sdk_test", "fetchPaymentLink: ${response.code()}")
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