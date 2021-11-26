package com.swirepay.android_sdk.checkout.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.checkout.model.*
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelCustomer(
) : ViewModel() {
    val liveCustomerResponse: MutableLiveData<CustomerResponse> = MutableLiveData()
    val liveGetCustomerResponse: MutableLiveData<CustomerContent> = MutableLiveData()
    val liveCheckoutResults: MutableLiveData<CustomerResponse> = MutableLiveData()
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()

    fun createCustomer(customer: SPCustomer?) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response = apiClient.createCustomer(customer, SwirepaySdk.apiKey).execute()
            if (response.isSuccessful && response.body() != null) {
                val customerResponse = response.body()!!.entity
                liveCustomerResponse.postValue(customerResponse)
            } else {
                liveErrorMessages.postValue("error code : ${response.code()}")
                Log.d("sdk_test", "upi-createCustomer: ${response.code()}")
            }
        }

    fun getCustomer(name: String, email: String, phoneNumber: String) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.getCustomer(name, email, phoneNumber, SwirepaySdk.apiKey).execute()
            if (response.isSuccessful && response.body() != null) {
                val customerResponse = response.body()!!.entity
                liveGetCustomerResponse.postValue(customerResponse)
            } else {
                liveErrorMessages.postValue("error code : ${response.code()}")
                Log.d("sdk_test", "upi-createCustomer: ${response.code()}")
            }
        }
}