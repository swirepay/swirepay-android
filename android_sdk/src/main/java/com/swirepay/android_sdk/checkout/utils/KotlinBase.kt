
package com.swirepay.android_sdk.checkout.utils


object KotlinBase {
    private val tag = LogUtil.getTag()

    @JvmStatic
    fun log() {
        Logger.v(tag, "Running Kotlin")
    }
}
