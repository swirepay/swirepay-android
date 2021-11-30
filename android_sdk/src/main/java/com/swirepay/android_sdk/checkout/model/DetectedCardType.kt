package com.swirepay.android_sdk.checkout.model

import com.swirepay.android_sdk.checkout.CardType


data class DetectedCardType(
    val cardType: CardType,
    val isReliable: Boolean,
    val enableLuhnCheck: Boolean,
    val cvcPolicy: Brand.FieldPolicy,
    val expiryDatePolicy: Brand.FieldPolicy,
    val isSupported: Boolean,
    val isSelected: Boolean = false
)
