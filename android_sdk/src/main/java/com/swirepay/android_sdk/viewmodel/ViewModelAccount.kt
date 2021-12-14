package com.swirepay.android_sdk.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.checkout.model.*
import com.swirepay.android_sdk.model.AccountResponse
import com.swirepay.android_sdk.model.ProfileResponse
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelAccount(
) : ViewModel() {
    val liveAccountResponse: MutableLiveData<AccountResponse> = MutableLiveData()
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()

    fun getAccount(accountGid: String) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.getAccount(accountGid, SwirepaySdk.apiKey).execute()
            if (response.isSuccessful && response.body() != null) {
                val accountResponse = response.body()!!.entity
                liveAccountResponse.postValue(accountResponse)
            } else {
                liveErrorMessages.postValue("error code : ${response.code()}")
                Log.d("sdk_test", "upi-createCustomer: ${response.code()}")
            }
        }
}