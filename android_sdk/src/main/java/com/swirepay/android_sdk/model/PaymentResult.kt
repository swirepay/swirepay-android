package com.swirepay.android_sdk.model

import com.swirepay.android_sdk.SwirepaySdk

class PaymentResult(val status : SwirepaySdk.PaymentStatus , var entity : PaymentLink?  = null, var reason : String = "")