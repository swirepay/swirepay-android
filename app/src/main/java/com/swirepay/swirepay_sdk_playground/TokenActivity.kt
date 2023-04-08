package com.swirepay.swirepay_sdk_playground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.swirepay.android_sdk.KeyNotInitializedException
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.checkout.model.TokenCard
import com.swirepay.android_sdk.checkout.model.TokenRequest
import com.swirepay.android_sdk.databinding.ActivityCreateTokenBinding
import com.swirepay.swirepay_sdk_playground.databinding.ActivityTokenBinding

class TokenActivity : AppCompatActivity() {
    lateinit var binding: ActivityTokenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToken.setOnClickListener {

            val pkKey = binding.etPrivateKey.text.toString()
            val desAccGid = binding.etAccountGid.text.toString()
            val type = binding.etType.text.toString()
            val cvv = binding.etCvv.text.toString()
            val expiryMonth = binding.etExpiryMonth.text.toString()
            val expiryYear = binding.etExpiryYear.text.toString()
            val name = binding.etName.text.toString()
            val number = binding.etNumber.text.toString()
            val scheme = binding.etScheme.text.toString()

            val cardRequest = TokenCard(
                "175",
                "10".toInt(),
                "2028".toInt(),
                "John Wick",
                "4242424242424242",
                "VISA"
            )
            val tokenRequest = TokenRequest(
                "CARD",
                cardRequest
            )

            try {
                SwirepaySdk.createToken(
                    this,
                    PaymentLinkActivity.REQUEST_CODE_PAYMENT_LINK,
                    tokenRequest,
                    "pk_test_7MHkDW1zxGLuHZtp0PPpreOOYRngSViX",
                    "account-366a1508867b4392be68d52d498ae6a4"
                )
            } catch (e: KeyNotInitializedException) {
                Toast.makeText(this, "Key not initialized!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val paymentResult = SwirepaySdk.getToken(resultCode, data)
        Log.d("sdk_test", "onActivityResult: $paymentResult")
        binding.tvResult.text = paymentResult.entity.toString()
        binding.tvResponse.text = paymentResult.toString()
    }
}