package com.swirepay.android_sdk.ui.token

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.checkout.model.TokenRequest
import com.swirepay.android_sdk.checkout.model.TokenResponse
import com.swirepay.android_sdk.model.PaymentRequest
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelCreateToken() : ViewModel() {
    val liveTokenResponse: MutableLiveData<TokenResponse> = MutableLiveData()
    val livePaymentResults: MutableLiveData<TokenResponse> = MutableLiveData()
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()

    fun createToken(tokenRequest: TokenRequest, pkKey: String, desAccGid: String) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.createToken(tokenRequest, pkKey, desAccGid).execute()
            if (response.isSuccessful && response.body() != null) {
                val tokenResponse = response.body()!!.entity
                Log.d("sdk_test", "fetchTokenResponse: ${response.body()!!.entity.gid}")
                liveTokenResponse.postValue(tokenResponse)
            } else {
                liveErrorMessages.postValue("error code : ${response.code()}")
                Log.d("sdk_test", "fetchTokenResponse: ${response.code()}")
            }
        }
}