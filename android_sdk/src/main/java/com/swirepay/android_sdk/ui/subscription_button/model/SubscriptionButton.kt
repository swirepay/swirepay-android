package com.swirepay.android_sdk.ui.subscription_button.model

import android.os.Parcelable
import com.swirepay.android_sdk.model.Currency
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class SubscriptionButton(
    val gid: String,
    val currency: Currency,
    val plan: Plan, val status: String,
    val link: String,
    val account: Account ,
    val coupon : Coupon? ,
    val taxAmount : List<TaxInfo>
) : Parcelable

@Parcelize
data class Account(val gid: String, val name: String, val timezone: String, val largeLogo: String) :
    Parcelable

abstract class BaseEntityResponse{
    val gid : String = ""
    val createdAt : String = ""
    val updatedAt : String = ""
    val deleted : Boolean = false
}

//"taxAmount": [
//{
//    "taxRates": {
//    "gid": "taxrates-05bd9db4868d4bd590505d89d1433a07",
//    "createdAt": "2021-04-05T11:20:08",
//    "updatedAt": "2021-04-05T11:20:08",
//    "displayName": "exclusive ",
//    "description": "exclusive",
//    "jurisdiction": "USA",
//    "percentage": 2.3,
//    "inclusive": false,
//    "deleted": false
//},
//    "tax": 2070
//}
//],
@Parcelize
data class TaxRate(
    val displayName: String,
    val description: String, val jurisdiction: String,
    val percentage: Float, val inclusive: Boolean
) : BaseEntityResponse() ,  Parcelable

// "coupon": {
//      "gid": "coupon-4c4c5e149c884a0daff87f4c4407b95d",
//      "createdAt": "2021-04-06T10:12:05",
//      "updatedAt": "2021-04-06T10:12:05",
//      "validity": "ONCE",
//      "amountOff": 10000,
//      "name": "$100.00 discount",
//      "maximumRedemption": null,
//      "currency": {
//        "id": 1,
//        "name": "USD",
//        "prefix": "$",
//        "toFixed": 2,
//        "countryAlpha2": "US"
//      },
//      "promotionalCodes": null,
//      "couponRedemption": null,
//      "active": true,
//      "valid": null,
//      "deleted": false
//    },

@Parcelize
data class TaxInfo(val taxRates : TaxRate , val tax: Int) : Parcelable

@Parcelize
data class Coupon(val validity : String , val amountOff : Int, val active : Boolean ,
val name : String , val currency: Currency) : BaseEntityResponse() ,  Parcelable