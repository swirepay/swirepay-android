package com.swirepay.android_sdk.ui.payment_button

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.PaymentButton
import com.swirepay.android_sdk.model.PaymentButtonRequest
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import com.swirepay.android_sdk.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelPaymentButton(val paymentButtonRequest: PaymentButtonRequest) :
    ViewModel() {
    val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()
    val liveData: MutableLiveData<PaymentButton> = MutableLiveData()
    val liveResult: MutableLiveData<PaymentButton> = MutableLiveData()

    init {
        fetchPaymentButton()
    }

    fun getPaymentButton(resultId: String)  = viewModelScope.launch(Dispatchers.IO){
        val response = apiClient.getPaymentButton(paymentButtonId = resultId, SwirepaySdk.apiKey!!).execute()
        if (response.isSuccessful && response.body() != null) {
            liveResult.postValue(response.body()!!.entity)
        } else {
            val error = if (response.errorBody() == null) "Unknown" else response.message()
            liveErrorMessages.postValue(error)
            Log.d("sdk_test", "fetchPaymentLink: $error")
        }
    }

    private fun fetchPaymentButton() = viewModelScope.launch(Dispatchers.IO) {
        val response =
            apiClient.createPaymentButtonRequest(paymentButtonRequest, SwirepaySdk.apiKey!!)
                .execute()
        if (response.isSuccessful && response.body() != null) {
            liveData.postValue(response.body()!!.entity)
        } else {
            val error = if (response.errorBody() == null) "Unknown" else response.message()
            liveErrorMessages.postValue(error)
            Log.d("sdk_test", "fetchPaymentLink: $error")
        }
    }
}