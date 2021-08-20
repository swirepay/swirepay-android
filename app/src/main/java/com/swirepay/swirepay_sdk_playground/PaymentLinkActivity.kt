package com.swirepay.swirepay_sdk_playground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.swirepay.android_sdk.KeyNotInitializedException
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.CurrencyType
import com.swirepay.android_sdk.model.NotificationType
import com.swirepay.android_sdk.model.PaymentMethodType
import com.swirepay.android_sdk.ui.payment_activity.model.CustomerModel
import com.swirepay.swirepay_sdk_playground.databinding.ActivityPaymentLinkBinding

class PaymentLinkActivity : AppCompatActivity() {

    lateinit var binding: ActivityPaymentLinkBinding

    private val listOfPaymentMethods = ArrayList<PaymentMethodType>()
    var currencyType = CurrencyType.INR

    companion object {
        const val REQUEST_CODE_PAYMENT_LINK = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spinnerCurrencyType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    when (p2) {
                        0 ->
                            currencyType = CurrencyType.INR
                        1 ->
                            currencyType = CurrencyType.USD
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        binding.cbCard.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPaymentMethods.add(
                    PaymentMethodType.CARD
                )
            else
                listOfPaymentMethods.remove(PaymentMethodType.CARD)
        }

        binding.cbUpi.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPaymentMethods.add(PaymentMethodType.CARD)
            else
                listOfPaymentMethods.remove(PaymentMethodType.CARD)
        }

        binding.btnPaymentLink.setOnClickListener {

            val listTypes = listOf(
                NotificationType.Email,
                NotificationType.SMS,
                NotificationType.ALL
            )

            val amount = binding.etAmount.text.toString()
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val phoneNum = binding.etPhoneNo.text.toString()
            val notificationType = listTypes[binding.spinnerNotificationType.selectedItemPosition]
            var customerGid: String = binding.etCustomerGid.text.toString()

            if (TextUtils.isEmpty(customerGid))
                customerGid = ""

            val customer = CustomerModel(
                email,
                name,
                phoneNum,
                "NONE",
                "exempt",
                null
            )

            try {
                SwirepaySdk.createPaymentLink(
                    this,
                    REQUEST_CODE_PAYMENT_LINK,
                    amount.toInt(),
                    currencyType,
                    listOfPaymentMethods,
                    customer,
                    customerGid,
                    notificationType
                )
            } catch (e: KeyNotInitializedException) {
                Toast.makeText(this, "Key not initialized!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val paymentResult = SwirepaySdk.getPaymentLink(resultCode, data)
        Log.d("sdk_test", "onActivityResult: $paymentResult")
        binding.tvResult.text = paymentResult.entity.toString()
        binding.tvResponse.text = paymentResult.toString()
    }
}