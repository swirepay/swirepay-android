package com.swirepay.android_sdk.checkout.model

import com.swirepay.android_sdk.Validation

data class FieldState<T> (
    val value: T,
    val validation: Validation
)
