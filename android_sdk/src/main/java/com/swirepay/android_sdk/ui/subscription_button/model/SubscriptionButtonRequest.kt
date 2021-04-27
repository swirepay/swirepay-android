package com.swirepay.android_sdk.ui.subscription_button.model

import android.os.Parcelable
import com.swirepay.android_sdk.Utility
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class SubscriptionButtonRequest(val currencyCode: String, val description : String, val planAmount : Int, val planBillingFrequency : String,
                                val planBillingPeriod : Int, val planGid : String, val planQuantity : Int, val planStartDate : String, val planTotalPayments : String,
                                val redirectUri : String = Utility.baseUrl, val couponGid : String? = null , val taxRates : List<String>? = null
                                ) : Parcelable