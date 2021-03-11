package com.swirepay.android_sdk.ui.subscription_button.model

import android.os.Parcelable
import com.swirepay.android_sdk.model.Currency
import kotlinx.parcelize.Parcelize

@Parcelize
class Plan(val gid : String , val currency : Currency , val name : String , val amount : Int , val description : String ,  val billingFrequency: String , val billingPeriod : Int ) : Parcelable