package com.swirepay.android_sdk

import com.swirepay.android_sdk.model.PaymentButton
import org.junit.Before
import org.junit.Test
import java.io.FileReader

class PaymentButtonTest : BaseClass() {
    lateinit var paymentButton: PaymentButton

    @Before
    fun init() {
        paymentButton = gson.fromJson(
            FileReader(getResource("json/payment_button.json")),
            PaymentButton::class.java
        )
    }

    @Test
    fun test_payment_button(){
        assert(paymentButton.amount == 1000)
    }

}