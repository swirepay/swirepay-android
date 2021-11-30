package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*


@kotlinx.parcelize.Parcelize
data class PaymentButton(
    val link: String, val gid: String,
    val createdAt: String, val updatedAt: String,
    val amount: Int,
    val redirectUri: String?,
    val description: String,
    val statementDescriptor: String,
    val expiresAt: String?,
    val status: String,
    val nextActionUrl: String?,
    val currency: Currency,
    val support: Support?,
    val deleted: Boolean
) : Parcelable

//"support": {
//            "gid": "support-d2c6a765f2574502947091abe5c73689",
//            "createdAt": "2020-11-03T10:59:29",
//            "updatedAt": "2020-11-03T10:59:29",
//            "email": "adithyamurali@gmail.com",
//            "phone": "+14104994322",
//            "website": null,
//            "deleted": false
//        },

@Parcelize
class Support(val gid : String ,
    val createdAt : String , val updatedAt : String ,
              val email : String , val phone : String , val website : String? , val deleted : Boolean
) : Parcelable