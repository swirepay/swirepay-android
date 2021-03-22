package com.swirepay.android_sdk.ui.create_account

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.BuildConfig
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.Utility
import com.swirepay.android_sdk.ui.base.BaseActivity
import com.swirepay.android_sdk.ui.create_account.model.Account
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity

class CreateAccountActivity : BaseActivity() {
    val viewModel : ViewModelCreateAccount by lazy {
        ViewModelProvider(this).get(ViewModelCreateAccount::class.java)
    }
    override val param_id: String
        get() = "sp-account-id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadUrl("${BuildConfig.PAYMENT_URL}/connect?key=${Utility.getBase24String(SwirepaySdk.apiKey!!)}")
    }

    //SpAccountId=account-3b91b29324064f319657da8f6738bb04
    override fun onRedirect(url: String?) {
        Log.d("sdk_test", "onRedirect: $url")
        if(result_id != null)
        {
            val intent = Intent().apply {
                putExtra(SwirepaySdk.RESULT, Account(gid = result_id))
                putExtra(SwirepaySdk.STATUS, 1)
            }
            setResult(RESULT_OK , intent)
            finish()
        }
        else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}