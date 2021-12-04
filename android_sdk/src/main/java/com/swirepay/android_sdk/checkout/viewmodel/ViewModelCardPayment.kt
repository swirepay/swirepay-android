package com.swirepay.android_sdk.checkout.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.checkout.model.*
import com.swirepay.android_sdk.model.OrderInfo
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelCardPayment(
) : ViewModel() {
    val liveCardResponse: MutableLiveData<CardResponse> = MutableLiveData()
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()

    fun updateCVV(cardInfo: CardInfo, cardGid: String) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.updateCVV(cardGid, cardInfo, SwirepaySdk.apiKey!!).execute()
            if (response.isSuccessful && response.body() != null) {
                val cardResponse = response.body()!!.entity
                liveCardResponse.postValue(cardResponse)
            } else {
                liveErrorMessages.postValue("Error code : ${response.code()}")
                Log.d("sdk_test", "upi-payment-session: ${response.message()}")
            }
        }
}