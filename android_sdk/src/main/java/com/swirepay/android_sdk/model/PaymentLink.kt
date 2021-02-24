package com.swirepay.android_sdk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigInteger

@Parcelize
data class PaymentLink(val gid: String ,
                  val amount : BigInteger,
                  val description : String,
                  val status : String,
                  val email : String,
                  val phoneNumber : String,
                  val name : String,
                  val deleted : Boolean,
                  val currency: Currency,
                       val link : String
) : Parcelable


@Parcelize
data class Currency(val id : Int , val name : String , val countryAlpha2: String , val toFixed: String) : Parcelable