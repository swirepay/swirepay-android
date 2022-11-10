package com.swirepay.android_sdk.ui.nativepayment

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.databinding.ActivityNativePaymentBinding
import com.swirepay.android_sdk.model.CurrencyType
import com.swirepay.android_sdk.model.NotificationType
import com.swirepay.android_sdk.model.PaymentMethodType
import com.swirepay.android_sdk.model.PaymentRequest
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity
import com.swirepay.android_sdk.ui.payment_activity.model.CustomerModel
import java.text.SimpleDateFormat
import androidx.lifecycle.Observer
import java.util.*

class NativePaymentActivity : AppCompatActivity() {

    val viewModel: ViewModelNativePayment by lazy {
        ViewModelProvider(
            this
        ).get(ViewModelNativePayment::class.java)
    }

    lateinit var binding: ActivityNativePaymentBinding

    private val listOfPaymentMethods = arrayListOf<String>()
    var currencyType = CurrencyType.INR

    var myCalendar = Calendar.getInstance()

    var date =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

    val timeFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativePaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardCheckbox.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPaymentMethods.add(
                    PaymentMethodType.CARD.toString()
                )
            else
                listOfPaymentMethods.remove(PaymentMethodType.CARD.toString())
        }

        binding.upiCheckbox.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPaymentMethods.add(PaymentMethodType.CARD.toString())
            else
                listOfPaymentMethods.remove(PaymentMethodType.CARD.toString())
        }

        binding.bankCheckbox.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPaymentMethods.add(PaymentMethodType.ACH.toString())
            else
                listOfPaymentMethods.remove(PaymentMethodType.ACH.toString())
        }
//        binding.bankCheckbox.setOnCheckedChangeListener { _, b ->
//            if (b)
//                listOfPaymentMethods.add(PaymentMethodType.ACH_LEGACY.toString())
//            else
//                listOfPaymentMethods.remove(PaymentMethodType.ACH_LEGACY.toString())
//        }

        binding.dueDate.setOnClickListener {

            DatePickerDialog(
                this, date, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        binding.btnCreate.setOnClickListener {

            val listTypes = listOf(
                NotificationType.Email,
                NotificationType.SMS,
                NotificationType.ALL
            )

            val amount = binding.amount.text.toString()
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val countryCode = binding.countryCode.text.toString()
            val phoneNum = binding.phoneNumber.text.toString()
            val notificationType = listTypes[binding.notifyType.selectedItemPosition]
            var customerGid: String? = null
            var dueDate: String? = null

            var strDueDate: String = binding.dueDate.text.toString()

//            if (!TextUtils.isEmpty(strCustomerGid))
//                customerGid = strCustomerGid;

            if (TextUtils.isEmpty(amount)) {
                binding.amountTil.error = "Enter amount!"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(name)) {
                binding.amount.error = "Enter name!"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(email)) {
                binding.amount.error = "Enter email!"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(phoneNum)) {
                binding.amount.error = "Enter phone number!"
                return@setOnClickListener
            }

            if (!binding.cardCheckbox.isChecked || !binding.upiCheckbox.isChecked || !binding.bankCheckbox.isChecked) {
                binding.cardCheckbox.error = "Select any payment methods"
                return@setOnClickListener
            }

            val customer = CustomerModel(
                email,
                name,
                countryCode + phoneNum,
                "NONE",
                "exempt",
                null
            )

            if (!TextUtils.isEmpty(strDueDate))
                dueDate = simpleDateFormat.format(timeFormat.parse(strDueDate));

            val paymentRequest = PaymentRequest(
                "$amount",
                currencyType.toString(),
                listOfPaymentMethods,
                customer,
                customerGid,
                notificationType.toString(),
                dueDate,
            )

            binding.progress.visibility = View.VISIBLE

            viewModel.fetchPaymentLink(paymentRequest)

            viewModel.livePaymentLink.observe(this, {
                Log.d(PaymentActivity.TAG, "onCreate: ${it.link}")
                binding.progress.visibility = View.GONE
            })

            viewModel.liveErrorMessages.observe(this, Observer { message ->
                setResult(RESULT_CANCELED, Intent().apply {
                    viewModel.livePaymentLink.value?.let {
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

    private fun updateLabel() {
        binding.dueDate.setText(timeFormat.format(myCalendar.time))
    }
}