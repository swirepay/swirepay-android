package com.swirepay.android_sdk.ui.payment_activity.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class CustomerModel(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val taxId: String,
    val taxStatus: String,
    val taxValue: String?
) : Parcelable {
}