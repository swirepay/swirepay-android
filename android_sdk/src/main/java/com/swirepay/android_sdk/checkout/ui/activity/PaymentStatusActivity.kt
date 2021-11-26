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
import com.swirepay.android_sdk.R
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.databinding.ActivityPaymentStatusBinding

import com.swirepay.android_sdk.checkout.utils.StatusbarUtil


class PaymentStatusActivity : AppCompatActivity() {

    lateinit var binding: ActivityPaymentStatusBinding
    var amount: Int = 0
    lateinit var paymentStatus: String

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
        paymentStatus = intent.getStringExtra(SwirepaySdk.PAYMENT_STATUS)!!

        if (paymentStatus == "true") {
            supportActionBar?.title = "Payment Successful"
            binding.successLayout.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                finish()
//                val complete = CheckoutActivity()
//                complete.onComplete(true)

                val msg = Message()
                msg.obj = true
                msg.what = 0

                CheckoutActivity.handler.sendMessage(msg);

            }, 1500)

        } else {
            supportActionBar?.title = "Payment Failed"
            binding.failureLayout.visibility = View.VISIBLE
        }
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor(SwirepaySdk.TOOLBAR_COLOR)))
        toolbar.setTitleTextColor(Color.parseColor(SwirepaySdk.TOOLBAR_ITEM))

        binding.tryAgain.setOnClickListener {
            finish()
        }
        binding.txtCancel.setOnClickListener {
            finish()
            val complete = CheckoutActivity()
            complete.onComplete(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.amount_menu, menu)

        val item = menu!!.findItem(R.id.amount)
        val s = SpannableString(getString(R.string.Rs) + " " + (amount / 100))
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