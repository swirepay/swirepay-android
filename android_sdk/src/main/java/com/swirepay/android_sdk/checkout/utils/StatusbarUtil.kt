package com.swirepay.android_sdk.checkout.utils

import android.graphics.Color
import android.view.Window
import android.view.WindowManager
import com.swirepay.android_sdk.SwirepaySdk

public final class StatusbarUtil {

    companion object {

        fun changeStatusbarColor(window: Window) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.parseColor(SwirepaySdk.STATUSBAR_COLOR)
        }
    }
}