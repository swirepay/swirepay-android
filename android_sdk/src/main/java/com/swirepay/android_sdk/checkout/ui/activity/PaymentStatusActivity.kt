package com.swirepay.android_sdk.checkout.ui.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.R
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.checkout.model.PaymentSessionResponse
import com.swirepay.android_sdk.databinding.ActivityPaymentStatusBinding

import com.swirepay.android_sdk.checkout.utils.StatusbarUtil
import com.swirepay.android_sdk.checkout.viewmodel.ViewModelPaymentSession


class PaymentStatusActivity : AppCompatActivity() {

    lateinit var binding: ActivityPaymentStatusBinding
    var amount: Int = 0
    var paymentSessionGid: String? = null
    var paymentSecret: String? = null

    val viewModelPaymentSession: ViewModelPaymentSession by lazy {
        ViewModelProvider(
            this
        ).get(ViewModelPaymentSession::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        StatusbarUtil.changeStatusbarColor(this.window)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        amount = intent.getIntExtra(SwirepaySdk.PAYMENT_AMOUNT, 0)
        if (intent.getStringExtra(SwirepaySdk.SESSION_GID) != null)
            paymentSessionGid = intent.getStringExtra(SwirepaySdk.SESSION_GID)
        if (intent.getStringExtra(SwirepaySdk.PAYMENT_SECRET) != null)
            paymentSecret = intent.getStringExtra(SwirepaySdk.PAYMENT_SECRET)

        binding.progress.visibility = View.VISIBLE

        viewModelPaymentSession.getPaymentSession(paymentSessionGid, paymentSecret)

        viewModelPaymentSession.getPaymentSessionResponse.observe(this, {

            updateUI(it)
        })

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor(SwirepaySdk.TOOLBAR_COLOR)))
        toolbar.setTitleTextColor(Color.parseColor(SwirepaySdk.TOOLBAR_ITEM))

        binding.tryAgain.setOnClickListener {
            finish()
        }

        binding.txtCancel.setOnClickListener {
            finish()

            val msg = Message()
            msg.obj = null
            msg.what = 0

            CheckoutActivity.handler.sendMessage(msg)
        }
    }

    private fun updateUI(paymentSession: PaymentSessionResponse?) {

        binding.progress.visibility = View.GONE

        if (paymentSession != null)
            if (paymentSession.status == "SUCCEEDED" || paymentSession.status == "SUCCESS") {

                supportActionBar?.title = "Payment Successful"
                binding.successLayout.visibility = View.VISIBLE

                Handler(Looper.getMainLooper()).postDelayed({
                    finish()

                    val msg = Message()
                    msg.obj = paymentSession
                    msg.what = 0

                    CheckoutActivity.handler.sendMessage(msg)

                }, 1500)
            } else {
                supportActionBar?.title = "Payment Failed"
                binding.failureLayout.visibility = View.VISIBLE
                binding.errorDescription.text = paymentSession.errorDescription
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.amount_menu, menu)

        val amount: String =
            String.format("%.2f", CheckoutActivity.dec.format(amount / 100.00).toString().toFloat())

        val item = menu!!.findItem(R.id.amount)
        val s = SpannableString(getString(R.string.Rs) + " " + amount)
        s.setSpan(
            ForegroundColorSpan(Color.parseColor(SwirepaySdk.TOOLBAR_ITEM)),
            0,
            s.length,
            0
        )
        item.title = s

        menu.findItem(android.R.id.home)?.setIcon(R.drawable.ic_close)

        return super.onCreateOptionsMenu(menu)
    }
}