package com.swirepay.swirepay_sdk_playground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.swirepay.android_sdk.KeyNotInitializedException
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.CurrencyType
import com.swirepay.android_sdk.model.NotificationType
import com.swirepay.android_sdk.model.PaymentMethodType
import com.swirepay.swirepay_sdk_playground.databinding.ActivityPaymentButtonBinding

class PaymentButtonActivity : AppCompatActivity() {

    lateinit var binding: ActivityPaymentButtonBinding

    companion object {
        const val REQUEST_CODE_PAYMENT_BUTTON = 1005
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreatePaymentButton.setOnClickListener {

            val list = listOf(
                PaymentMethodType.CARD
            )
            val listCurrencies = listOf(
                CurrencyType.INR,
                CurrencyType.USD
            )
            val listTypes = listOf(
                NotificationType.Email,
                NotificationType.SMS,
                NotificationType.ALL
            )
            val amount = binding.etAmount.text.toString()
            val description = binding.etDescription.text.toString()
            val notificationType = listTypes[binding.spinnerNotificationType.selectedItemPosition]
            val currencyType = listCurrencies[binding.spinnerCurrencyType.selectedItemPosition]

            try {
                if (TextUtils.isEmpty(amount)) {
                    Toast.makeText(
                        this,
                        "amount key cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    SwirepaySdk.createPaymentButton(
                        this,
                        REQUEST_CODE_PAYMENT_BUTTON,
                        amount.toInt(),
                        description,
                        currencyType,
                        notificationType,
                        list
                    )
                }
            }catch (e: KeyNotInitializedException) {
                Toast.makeText(this, "Key not initialized!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PAYMENT_BUTTON) {
            val result = SwirepaySdk.getPaymentButton(resultCode, data)
            Log.d("sdk_test", "onActivityResult: $result")
            binding.tvResult.text = result.toString()
            binding.tvResponse.text = result.entity.toString()
        }
    }
}