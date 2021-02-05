package com.swirepay.android_sdk.ui.payment_activity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.BuildConfig
import com.swirepay.android_sdk.model.PaymentLink
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelPayment(private val amount : Int, private val currencyCode : String) : ViewModel(){
    val livePaymentLink : MutableLiveData<PaymentLink> = MutableLiveData()
    val liveErrorMessages : MutableLiveData<String> = MutableLiveData()
    init {
        fetchPaymentLink()
    }

    private fun fetchPaymentLink() = viewModelScope.launch(Dispatchers.IO){
        val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)
        val paymentRequest = PaymentRequest("$amount" , currencyCode , listOf("CARD"))
        val response = apiClient.fetchPaymentLink(paymentRequest , BuildConfig.X_API_KEY).execute()
        if(response.isSuccessful && response.body() != null){
            val paymentLink = response.body()!!.entity
            Log.d("sdk_test", "fetchPaymentLink: ${response.body()!!}")
            livePaymentLink.postValue(paymentLink)
        }else{
            val error = if (response.errorBody() == null) "Unknown" else response.errorBody().toString()
            liveErrorMessages.postValue(error)
            Log.d("sdk_test", "fetchPaymentLink: $error")
        }
    }
}