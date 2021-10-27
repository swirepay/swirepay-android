package com.swirepay.android_sdk.`interface`

import com.pusher.client.channel.PusherEvent
import retrofit2.Call
import java.lang.Exception

interface IPusherCallback {

    fun onEvent(event: PusherEvent?)
    fun onSubscriptionSucceeded(channelName: String?)
    fun <T> onFailure(call: Call<T>, t: Throwable): T
    fun onAuthenticationFailure(message: String?, e: Exception?)
}