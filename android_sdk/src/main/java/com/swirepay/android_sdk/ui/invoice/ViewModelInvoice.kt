package com.swirepay.android_sdk.ui.invoice

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.*
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelInvoice(
    val invoiceRequest: InvoiceRequest
) : ViewModel() {

    val liveInvoiceLink : MutableLiveData<InvoiceResponse> = MutableLiveData()
    val liveInvoiceResults: MutableLiveData<InvoiceResponse> = MutableLiveData()
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()

    init {
        fetchInvoiceLink()
    }

    private fun fetchInvoiceLink() = viewModelScope.launch(Dispatchers.IO) {
        val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)
        val response = apiClient.fetchInvoiceLink(invoiceRequest, SwirepaySdk.apiKey!!).execute()
        if (response.isSuccessful && response.body() != null) {
            val invoiceLink = response.body()!!.entity
            Log.d("sdk_test", "fetchInvoiceLink: ${response.body()!!.entity.gid}")
            liveInvoiceLink.postValue(invoiceLink)
        } else {
            liveErrorMessages.postValue("error code : ${response.code()}")
            Log.d("sdk_test", "fetchInvoiceLink: ${response.code()}")
        }
    }

    fun checkInvoiceStatus() = viewModelScope.launch(Dispatchers.IO){
        val invoiceLinkId = liveInvoiceLink.value!!.gid

        val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)
        val response = apiClient.checkInvoiceStatus(invoiceLinkId).execute()
        if(response.isSuccessful && response.body() != null){
            val invoiceLink = response.body()!!.entity
            Log.d("sdk_test", "checking status: ${response.body()!!}")
            liveInvoiceResults.postValue(invoiceLink)
        }else{
            liveErrorMessages.postValue("error code : ${response.code()}")
            Log.d("sdk_test", "fetchPaymentLink: ${response.code()}")
        }
    }
}