package com.swirepay.swirepay_sdk_playground

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.swirepay.android_sdk.KeyNotInitializedException
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.CurrencyType
import com.swirepay.swirepay_sdk_playground.databinding.ActivitySubscriptionButtonBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SubscriptionButtonActivity : AppCompatActivity() {

    lateinit var binding: ActivitySubscriptionButtonBinding

    companion object {
        const val REQUEST_CODE_SUBSCRIPTION_BUTTON = 1002
    }

    var currencyType = CurrencyType.INR
    var billingFrequency = "Month"
    var time: Date = Calendar.getInstance().time
    val timeFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    var calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etTime.setText(timeFormat.format(time))
        binding.etTime.setOnClickListener(View.OnClickListener {

            val datePicker = DatePickerDialog(
                this, { view, year, monthOfYear, dayOfMonth ->

                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    binding.etTime.setText(timeFormat.format(calendar.time))

                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.minDate = calendar.timeInMillis
            datePicker.show()
        })

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

        binding.spinnerBillingFrequency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    billingFrequency = resources.getStringArray(R.array.billing_frequency).get(p2);
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        binding.btnSubscriptionButton.setOnClickListener {

            val timeString: String = binding.etTime.text.toString()
            val list = ArrayList<String>()
            val couponText = binding.etCouponGid.text.toString()
            val couponId = if (couponText.isEmpty()) null else couponText
            val totalCount = binding.etTotalCount.text.toString()
            val description = binding.etPlanDescription.text.toString()
            val planName = binding.etName.text.toString()
            val planAmount = binding.etPlanAmount.text.toString()
            val billingPeriod = binding.etBillingPeriod.text.toString()
            val quantity = binding.etPlanQuantity.text.toString()

            val tax1 = binding.etTaxRate1.text.toString()
            val tax1Id = if (tax1.isEmpty()) null else tax1
            if (tax1Id != null)
                list.add(tax1Id)

            val tax2 = binding.etTaxRate2.text.toString()
            val tax2Id = if (tax2.isEmpty()) null else tax2
            if (tax2Id != null)
                list.add(tax2Id)

            val tax3 = binding.etTaxRate3.text.toString()
            val tax3Id = if (tax3.isEmpty()) null else tax3
            if (tax3Id != null)
                list.add(tax3Id)

//            val couponId = "coupon-4c4c5e149c884a0daff87f4c4407b95d"
            val listTaxRates = ArrayList<String>().apply {
//                add("taxrates-05bd9db4868d4bd590505d89d1433a07")
            }
            val time = Calendar.getInstance()
            time.add(Calendar.DATE, 1)

            if (TextUtils.isEmpty(planName)) {
                Toast.makeText(this, "Plan name is mandatory", Toast.LENGTH_SHORT).show()
                return@setOnClickListener;
            }

            if (TextUtils.isEmpty(planAmount)) {
                Toast.makeText(this, "Plan amount is mandatory", Toast.LENGTH_SHORT).show()
                return@setOnClickListener;
            }

            if (TextUtils.isEmpty(quantity)) {
                Toast.makeText(this, "Plan quantity is mandatory", Toast.LENGTH_SHORT).show()
                return@setOnClickListener;
            }

            if (TextUtils.isEmpty(billingPeriod)) {
                Toast.makeText(this, "Billing period is mandatory", Toast.LENGTH_SHORT).show()
                return@setOnClickListener;
            }

            try {
                SwirepaySdk.createSubscriptionButton(
                    this,
                    REQUEST_CODE_SUBSCRIPTION_BUTTON,
                    planName,
                    planAmount.toInt(),
                    description,
                    currencyType,
                    billingFrequency,
                    billingPeriod.toInt(),
                    timeFormat.parse(timeString),
                    listTaxRates,
                    couponId,
                    quantity.toInt(),
                    totalCount.toInt(),
                    "ACTIVE"
                )
            } catch (e: KeyNotInitializedException) {
                Toast.makeText(this, "Key not initialized", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SUBSCRIPTION_BUTTON) {
            val result = SwirepaySdk.getSubscriptionButton(resultCode, data)
            Log.d("sdk_test", "onActivityResult: $result")
            binding.tvResult.text = result.toString()
            binding.tvResponse.text = result.entity.toString()
        }
    }
}