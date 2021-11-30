package com.swirepay.android_sdk.ui.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.swirepay.android_sdk.BuildConfig
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.PaymentButton
import com.swirepay.android_sdk.model.PaymentButtonRequest
import com.swirepay.android_sdk.model.SuccessResponse
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import com.swirepay.android_sdk.retrofit.DateDeSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

open class BaseViewModel<T , R>(val url : String , val request : R , val cls : Class<T>) : ViewModel() {
    val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)
    val liveErrorMessages : MutableLiveData<String> = MutableLiveData()
    val liveData : MutableLiveData<T> = MutableLiveData()
    val liveResult : MutableLiveData<T> = MutableLiveData()
    val gsonBuilder = GsonBuilder().apply {
        registerTypeAdapter(
            Date::class.java,
            DateDeSerializer()
        )
    }
    val gson = gsonBuilder.create()

    init {
        fetch()
    }

    fun fetch() = viewModelScope.launch(Dispatchers.IO){
        val url = "${BuildConfig.BASE_URL}/v1/$url"
//        val req = gson.toJson(request)
//        val map = gson.fromJson(req , HashMap::class.java)
//        val response = apiClient.create<R>(  url , request , SwirepaySdk.apiKey!!).execute()
//        if (response.isSuccessful && response.body() != null) {
//            val type = TypeToken.getParameterized(SuccessResponse::class.java , cls).type
//            val successResponse: SuccessResponse<T> = gson.fromJson(response.body()!!, type)
//            liveData.postValue(successResponse.entity)
//        } else {
//            val error = if (response.errorBody() == null) "Unknown" else response.message()
//            liveErrorMessages.postValue(error)
//            Log.d("sdk_test", "fetchPaymentLink: $error")
//        }
    }

}