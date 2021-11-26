package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Banks(
    val id: Int,
    val cashfreeBankId: Int,
    val cashfreeBankName: String,
    val createdAt: String,
    val updatedAt: String,
    val deleted: String
) : Parcelable