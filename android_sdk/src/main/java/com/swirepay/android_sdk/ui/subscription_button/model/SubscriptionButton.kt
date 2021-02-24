package com.swirepay.android_sdk.ui.subscription_button.model

import android.os.Parcelable
import com.swirepay.android_sdk.model.Currency
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class SubscriptionButton (val gid : String  , val currency : Currency , val plan : Plan , val status : String , val link : String , val account : Account) : Parcelable

@Parcelize
data class Account(val gid : String , val name : String , val timezone : String , val largeLogo : String) : Parcelable