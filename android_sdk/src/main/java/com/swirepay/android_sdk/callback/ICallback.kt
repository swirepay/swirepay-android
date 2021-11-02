package com.swirepay.android_sdk.callback

import com.pusher.client.channel.PusherEvent
import retrofit2.Call

interface ICallback {

    fun onEvent(event: PusherEvent?)
    fun onSubscriptionSucceeded(channelName: String?)
    fun <T> onFailure(call: Call<T>, t: Throwable): T
    fun onAuthenticationFailure(message: String?, e: java.lang.Exception?)
}