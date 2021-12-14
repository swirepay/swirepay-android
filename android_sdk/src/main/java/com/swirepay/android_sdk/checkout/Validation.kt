package com.swirepay.android_sdk

import androidx.annotation.StringRes

sealed class Validation {

    /**
     * Field is valid and can be accepted.
     */
    object Valid : Validation()
    /**
     * Field is not valid.
     */
    class Invalid(@StringRes val reason: Int, val showErrorWhileEditing: Boolean) : Validation() {
        // Java doesn't understand optional params
        constructor(reason: Int) : this(reason, false)
    }

    fun isValid(): Boolean {
        return this is Valid
    }
}
