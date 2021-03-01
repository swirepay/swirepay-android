package com.swirepay.android_sdk.ui.payment_method

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelPaymentMethod : ViewModel() {
    val liveResult: MutableLiveData<SetupSession> = MutableLiveData()
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()

    fun fetchSetupSession(setupSessionId: String) = viewModelScope.launch(Dispatchers.IO) {
        val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)
        val response = apiClient.getSetupSession(setupSessionId, SwirepaySdk.apiKey!!).execute()
        if (response.isSuccessful && response.body() != null) {
            liveResult.postValue(response.body()!!.entity)
        } else {
            val code = response.code()
            liveErrorMessages.postValue("error_code : $code")
            Log.d("sdk_test", "fetchPaymentLink: ${response.code()}")
        }
    }

}