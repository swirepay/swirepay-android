package com.swirepay.android_sdk.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.checkout.model.*
import com.swirepay.android_sdk.model.ProfileResponse
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelProfile(
) : ViewModel() {
    val liveProfileResponse: MutableLiveData<ProfileResponse> = MutableLiveData()
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()

    fun getProfile() =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.getProfile(SwirepaySdk.apiKey).execute()
            if (response.isSuccessful && response.body() != null) {
                val profileResponse = response.body()!!.entity
                liveProfileResponse.postValue(profileResponse)
            } else {
                liveErrorMessages.postValue("error code : ${response.code()}")
                Log.d("sdk_test", "upi-createCustomer: ${response.code()}")
            }
        }
}