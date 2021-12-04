package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//@Parcelize
//class Banks(
//    val id: Int,
//    val cashfreeBankId: Int,
//    val cashfreeBankName: String,
//    val createdAt: String,
//    val updatedAt: String,
//    val deleted: String
//) : Parcelable

@Parcelize
class Banks(
    val gid: String,
    val bankName: String,
    val isTest: Boolean,
    val paymentType: String,
    val category: String
) : Parcelable