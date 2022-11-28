package com.swirepay.android_sdk

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Base64
import android.util.Base64.encodeToString
import android.util.Log
import com.swirepay.android_sdk.checkout.model.PaymentMethodResponse
import com.swirepay.android_sdk.model.SuccessResponse
import org.json.JSONObject
import retrofit2.Response


object Utility {
    //const val baseUrl = "https://www.swirepay.com"
    const val baseUrl = "https://redirect.swirepay.com"
    fun getBase24String(data: String): String {
        return encodeToString(data.toByteArray(), Base64.DEFAULT)
    }

    fun <T> getErrorMsg(response: Response<SuccessResponse<T>>): String {
        val jObjError = JSONObject(response.errorBody()!!.string())
        return jObjError.getString("message")
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}