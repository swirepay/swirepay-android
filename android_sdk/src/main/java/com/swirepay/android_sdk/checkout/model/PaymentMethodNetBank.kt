package com.swirepay.android_sdk.checkout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PaymentMethodNetBank(
    val netbanking: NetBanking,
    val type: String,
    val customerGid: String
) : Parcelable

@Parcelize
data class NetBanking(val swirepayBankGid: String) : Parcelable

