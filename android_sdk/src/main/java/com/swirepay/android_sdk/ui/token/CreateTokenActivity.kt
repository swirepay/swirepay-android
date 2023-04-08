package com.swirepay.android_sdk.ui.token

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.checkout.model.TokenCard
import com.swirepay.android_sdk.checkout.model.TokenRequest
import com.swirepay.android_sdk.databinding.ActivityCreateTokenBinding
import com.swirepay.android_sdk.ui.nativepayment.ViewModelNativePayment
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity
import com.swirepay.android_sdk.ui.payment_activity.model.CustomerModel

class CreateTokenActivity : AppCompatActivity() {
    val viewModel: ViewModelCreateToken by lazy {
        ViewModelProvider(
            this
        ).get(ViewModelCreateToken::class.java)
    }

    lateinit var binding: ActivityCreateTokenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTokenBinding.inflate(layoutInflater)
        setContentView(binding.root)

            val pkKey = intent.getStringExtra(SwirepaySdk.PK_KEY)
            val desAccGid = intent.getStringExtra(SwirepaySdk.DES_ACC_GID)
            val tokenRequest = intent.getParcelableExtra<TokenRequest>(SwirepaySdk.TOKEN_REQUEST)


            viewModel.createToken(tokenRequest!!, pkKey!!, desAccGid!!)

            viewModel.liveTokenResponse.observe(this, {
                setResult(RESULT_OK, Intent().apply {
                    putExtra(SwirepaySdk.STATUS, 1)
                    putExtra(SwirepaySdk.RESULT, it)
                })
                finish()
            })

            viewModel.liveErrorMessages.observe(this, Observer { message ->
                setResult(RESULT_CANCELED, Intent().apply {
                    viewModel.liveTokenResponse.value?.let {
                        putExtra(SwirepaySdk.RESULT, it)
                    }
                    putExtra(
                        PaymentActivity.FAILURE_REASON,
                        PaymentActivity.PAYMENT_FAILURE_REASON_API_FAILURE
                    )
                    putExtra(PaymentActivity.PAYMENT_MESSAGE, message)
                })
                finish()
            })
            viewModel.livePaymentResults.observe(this, Observer {
                setResult(RESULT_OK, Intent().apply {
                    putExtra(SwirepaySdk.STATUS, 1)
                    putExtra(SwirepaySdk.RESULT, it)
                })
                finish()
            })
    }
}