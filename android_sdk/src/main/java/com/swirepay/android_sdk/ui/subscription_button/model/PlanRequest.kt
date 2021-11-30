package com.swirepay.android_sdk.ui.subscription_button.model

//{
//    "name": "test4",
//    "amount": 40000,
//    "description": "test4",
//    "currencyCode": "INR",
//    "billingFrequency": "MONTH",
//    "billingPeriod": 1
//}
class PlanRequest(
    val name: String,
    val amount: Int,
    val description: String,
    val currencyCode: String,
    val billingFrequency: String,
    val billingPeriod: Int,
)