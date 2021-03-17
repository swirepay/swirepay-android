package com.swirepay.android_sdk.ui.payment_method

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class SetupSession(val paymentMethod : PaymentMethod , val customer : Customer , val currency : Currency , val gid : String
, val setupDate : Date  , val status : String , val ssClientSecret : String , val returnUrl : String , val deleted : Boolean , val confirmMethod : String) : Parcelable

@Parcelize
data class PaymentMethod(
    val amountReceived: Int,
    val paymentType: PaymentType,
    val card: Card?,
    val upi: UPI?,
) : Parcelable

@Parcelize
data class Customer(
    private val createdAt: Date,
    val updatedAt: Date,
    var name: String,
    var email: String,
    var phoneNumber: String,
    val gid : String
) :
    Parcelable {
    fun getTime(): String {
        val simpleDateFormat = SimpleDateFormat("MMM dd, HH:mm a", Locale.getDefault())
        return simpleDateFormat.format(createdAt)
    }
}

@Parcelize
data class UPI(val gid: String, private val createdAt: Date, val vpa: String) : Parcelable

@Parcelize
data class PaymentType(val name: String, val category: String) : Parcelable

@Parcelize
data class Currency(val prefix: String) : Parcelable

@Parcelize
data class Card(
    val scheme: String,
    val lastFour: String,
    val expiryMonth: String,
    val expiryYear: String,
    val name: String,
    val gid: String,
) : Parcelable {
    fun cardNumber(): String = "**** $lastFour"

    fun expDate(): String = "${getTwoDigits(expiryMonth)}/${expiryYear.substring(2, 4)}"

    private fun getTwoDigits(param: String): String {
        return if (param.length != 1) param else "0$param"
    }

}

enum class CategoryType {
    CARD,
    UPI
}
