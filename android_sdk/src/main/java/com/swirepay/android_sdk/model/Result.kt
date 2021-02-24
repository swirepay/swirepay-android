package com.swirepay.android_sdk.model

data class Result<T>(val response : Status, var entity : T?  = null, var reason : String = "", var message : String = "")