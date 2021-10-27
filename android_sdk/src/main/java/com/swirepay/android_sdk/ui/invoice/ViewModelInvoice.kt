package com.swirepay.android_sdk.ui.invoice

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.BuildConfig
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.InvoiceResponse
import com.swirepay.android_sdk.model.PaymentLink
import com.swirepay.android_sdk.model.PaymentRequest
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import com.swirepay.android_sdk.ui.payment_activity.model.CustomerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelInvoice(
) : ViewModel() {
    val liveInvoiceResults: MutableLiveData<InvoiceResponse> = MutableLiveData()
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()

    fun checkInvoiceStatus(gid:String) = viewModelScope.launch(Dispatchers.IO) {

        val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)
        val response = apiClient.checkInvoiceStatus(gid).execute()
        if (response.isSuccessful && response.body() != null) {
            val invoiceLink = response.body()!!.entity
            Log.d("sdk_test", "checking status: ${response.body()!!}")
            liveInvoiceResults.postValue(invoiceLink)
        } else {
            liveErrorMessages.postValue("error code : ${response.code()}")
            Log.d("sdk_test", "fetchInvoiceLink: ${response.code()}")
        }
    }
}