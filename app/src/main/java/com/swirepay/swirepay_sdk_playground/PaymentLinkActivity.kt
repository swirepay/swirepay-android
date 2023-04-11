package com.swirepay.swirepay_sdk_playground

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.swirepay.android_sdk.KeyNotInitializedException
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.CurrencyType
import com.swirepay.android_sdk.model.NotificationType
import com.swirepay.android_sdk.model.PaymentMethodType
import com.swirepay.android_sdk.ui.payment_activity.model.CustomerModel
import com.swirepay.swirepay_sdk_playground.databinding.ActivityPaymentLinkBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class PaymentLinkActivity : AppCompatActivity() {

    lateinit var binding: ActivityPaymentLinkBinding

    private val listOfPaymentMethods = ArrayList<String>()
    var currencyType = CurrencyType.INR

    var myCalendar = Calendar.getInstance()
    var expiresAt: String? = null


    var date =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

    val timeFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'00:00:00", Locale.getDefault())

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
                    PaymentMethodType.CARD.toString()
                )
            else
                listOfPaymentMethods.remove(PaymentMethodType.CARD.toString())
        }

        binding.cbUpi.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPaymentMethods.add(PaymentMethodType.UPI.toString())
            else
                listOfPaymentMethods.remove(PaymentMethodType.UPI.toString())
        }

        binding.usBank.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPaymentMethods.add(PaymentMethodType.ACH_LEGACY.toString())
            else
                listOfPaymentMethods.remove(PaymentMethodType.ACH_LEGACY.toString())
        }

        binding.indBank.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPaymentMethods.add(PaymentMethodType.NET_BANKING.toString())
            else
                listOfPaymentMethods.remove(PaymentMethodType.NET_BANKING.toString())
        }



        binding.etDueDate.setOnClickListener {

            DatePickerDialog(
                this, date, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        binding.btnPaymentLink.setOnClickListener {

            val listTypes = listOf(
                NotificationType.Email,
                NotificationType.SMS,
                NotificationType.ALL
            )

            var amount = binding.etAmount.text.toString()


            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val phoneNum = binding.etPhoneNo.text.toString()
            val notificationType = listTypes[binding.spinnerNotificationType.selectedItemPosition]
            var customerGid: String? = null
            var redirectUri: String? = null

            var strCustomerGid: String = binding.etCustomerGid.text.toString()
            var strDueDate: String = binding.etDueDate.text.toString()

            if (!TextUtils.isEmpty(strCustomerGid))
                customerGid = strCustomerGid;

//            if (!TextUtils.isEmpty(strDueDate))
//                redirectUri = "https://redirect.swirepay.com";

            val customer = CustomerModel(
                email,
                name,
                phoneNum,
                "NONE",
                "exempt",
                null
            )

            /*if (amount.equals("")){
//                binding.etAmount.setText(0)
                val isAmountEmpty = 0.toString()
                Log.e("==============A",isAmountEmpty)


            }else {
                amount.toInt()
            }*/

            if (amount.isEmpty()) {
                val isAmountEmpty = 0.toString()
                try {
                    SwirepaySdk.createPaymentLink(
                        this,
                        REQUEST_CODE_PAYMENT_LINK,

                        isAmountEmpty.toInt(),

                        currencyType,
                        listOfPaymentMethods,
                        customer,
                        customerGid,

                        notificationType,
                        expiresAt,
                    )

                } catch (e: KeyNotInitializedException) {
                    Toast.makeText(this, "Key not initialized!", Toast.LENGTH_SHORT).show()
                }
            } else {
                try {
                    SwirepaySdk.createPaymentLink(
                        this,
                        REQUEST_CODE_PAYMENT_LINK,
                        amount.toInt(),
                        currencyType,
                        listOfPaymentMethods,
                        customer,
                        customerGid,

                        notificationType,
                        expiresAt,
                    )
                } catch (e: KeyNotInitializedException) {
                    Toast.makeText(this, "Key not initialized!", Toast.LENGTH_SHORT).show()
                }
            }

//
            }
        }

        private fun updateLabel() {
            expiresAt =  simpleDateFormat.format(myCalendar.time)
            binding.etDueDate.setText(timeFormat.format(myCalendar.time))
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            val paymentResult = SwirepaySdk.getPaymentLink(resultCode, data)
            Log.d("sdk_test", "onActivityResult: $paymentResult")
            binding.tvResult.text = paymentResult.entity.toString()
            binding.tvResponse.text = paymentResult.toString()
        }
    }