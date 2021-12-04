package com.swirepay.android_sdk.checkout.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.swirepay.android_sdk.R
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.checkout.model.*
import com.swirepay.android_sdk.checkout.ui.adapter.ObjectAdapter
import com.swirepay.android_sdk.checkout.utils.CardType
import com.swirepay.android_sdk.checkout.utils.StatusbarUtil
import com.swirepay.android_sdk.checkout.views.*
import com.swirepay.android_sdk.databinding.ActivityCheckOutBinding
import com.swirepay.android_sdk.model.Banks
import com.swirepay.android_sdk.model.OrderInfo
import android.os.Handler
import android.os.Message
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.swirepay.android_sdk.model.PaymentMethodType
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity
import com.swirepay.android_sdk.viewmodel.ViewModelAccount
import com.swirepay.android_sdk.viewmodel.ViewModelProfile
import java.io.IOException
import java.io.InputStream
import java.net.URLEncoder
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.checkbox.MaterialCheckBox
import com.skydoves.expandablelayout.ExpandableLayout
import com.swirepay.android_sdk.Utility
import com.swirepay.android_sdk.checkout.ui.adapter.CustomAdapter
import com.swirepay.android_sdk.checkout.viewmodel.*


class CheckoutActivity : AppCompatActivity() {

    lateinit var binding: ActivityCheckOutBinding

    lateinit var customerGid: String
    lateinit var cardNumber: CardNumberInput
    lateinit var mobileNumber: SwirepayTextInputEditText
    lateinit var expiryDate: ExpiryDateInput
    lateinit var securityCode: SecurityCodeInput
    lateinit var cardHolder: SwirepayTextInputEditText
    lateinit var payNow: FadedDisableButton
    lateinit var payNowUpi: FadedDisableButton
    lateinit var payNowBank: FadedDisableButton
    lateinit var cardNumberTextInput: TextInputLayout
    lateinit var mobileTextInput: TextInputLayout
    lateinit var cardLogo: RoundCornerImageView
    lateinit var storePaymentMethod: MaterialCheckBox
    lateinit var orderInfo: OrderInfo
    lateinit var customer: SPCustomer
    lateinit var editTextBanks: AppCompatAutoCompleteTextView
    val swirepayBanks = ArrayList<Banks>()
    lateinit var paymentTypes: List<String>
    lateinit var bankId: String
    var isTest: Boolean = false

    val viewModelProfile: ViewModelProfile by lazy {
        ViewModelProvider(this).get(ViewModelProfile::class.java)
    }

    val viewModelAccount: ViewModelAccount by lazy {
        ViewModelProvider(this).get(ViewModelAccount::class.java)
    }

    val viewModelCustomer: ViewModelCustomer by lazy {
        ViewModelProvider(this).get(ViewModelCustomer::class.java)
    }

    val viewModelPaymentSession: ViewModelPaymentSession by lazy {
        ViewModelProvider(
            this
        ).get(ViewModelPaymentSession::class.java)
    }

    val viewModelUPI: ViewModelPaymentUPI by lazy {
        ViewModelProvider(
            this
        ).get(ViewModelPaymentUPI::class.java)
    }

    val viewModelCard: ViewModelCardPayment by lazy {
        ViewModelProvider(
            this
        ).get(ViewModelCardPayment::class.java)
    }

    val viewModelNetBanking: ViewModelNetBanking by lazy {
        ViewModelProvider(
            this
        ).get(ViewModelNetBanking::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        StatusbarUtil.changeStatusbarColor(this.window)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        orderInfo = intent.getParcelableExtra(SwirepaySdk.ORDER_INFO)!!
        customer = intent.getParcelableExtra(SwirepaySdk.PAYMENT_CUSTOMER)!!

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.title = "Order"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor(SwirepaySdk.TOOLBAR_COLOR)))
        toolbar.setTitleTextColor(Color.parseColor(SwirepaySdk.TOOLBAR_ITEM))

        if (!Utility.isOnline(this)) {
            onSnack("Internet connection not available!")
            return
        }

        if (!SwirepaySdk.HIDE_LOGO)
            binding.logo.visibility = View.GONE
        else
            binding.logo.visibility = View.VISIBLE

        binding.cardExpandable.parentLayout.setOnClickListener {
            binding.cardExpandable.toggleLayout()
            collapse(binding.upiExpandable)
            collapse(binding.netBankExpandable)
        }

        binding.upiExpandable.parentLayout.setOnClickListener {
            binding.upiExpandable.toggleLayout()
            collapse(binding.cardExpandable)
            collapse(binding.netBankExpandable)
        }

        binding.netBankExpandable.parentLayout.setOnClickListener {
            binding.netBankExpandable.toggleLayout()
            collapse(binding.upiExpandable)
            collapse(binding.cardExpandable)
        }

        val billingAddress = SPBillingAddress("Street", "Chennai", "TN", "600030", "IN")
        val shippingAddress = SPShippingAddress("Street", "Chennai", "TN", "600030", "IN")
        customer.billingAddress = billingAddress
        customer.shippingAddress = shippingAddress

        binding.progress.visibility = View.VISIBLE

        viewModelProfile.getProfile()
        viewModelProfile.liveProfileResponse.observe(this, {

            isTest = it.test

            viewModelAccount.getAccount(it.accountGid)
            viewModelAccount.liveAccountResponse.observe(this, { it ->
                paymentTypes = it.supportedPaymentTypes

                if (paymentTypes.contains("CREDIT_CARD_NOT_PRESENT") || paymentTypes.contains("DEBIT_CARD_NOT_PRESENT"))
                    binding.cardExpandable.visibility = View.VISIBLE
                else
                    binding.cardExpandable.visibility = View.GONE

                if (paymentTypes.contains("UPI_NOT_PRESENT"))
                    binding.upiExpandable.visibility = View.VISIBLE
                else
                    binding.upiExpandable.visibility = View.GONE

                val data: String = getAssetJsonData(applicationContext)
                val gson = GsonBuilder().create()
                val banks = gson.fromJson(data, Array<Banks>::class.java).toList()

                for (bank in banks) {
                    if (bank.isTest == isTest)
                        if (paymentTypes.contains(bank.paymentType)) {
                            swirepayBanks.add(bank)
                        }
                }

                if (swirepayBanks.size > 0)
                    binding.netBankExpandable.visibility = View.VISIBLE
                else
                    binding.netBankExpandable.visibility = View.GONE
            })
        })

        viewModelCustomer.getCustomer(
            customer.name,
            URLEncoder.encode(customer.email, "UTF-8"),
            URLEncoder.encode(customer.phoneNumber, "UTF-8")
        )

        viewModelCustomer.liveGetCustomerResponse.observe(this, {
            binding.progress.visibility = View.GONE

            if (it.content.isEmpty()) {

                viewModelCustomer.createCustomer(customer)

                viewModelCustomer.liveCustomerResponse.observe(this, { it ->
                    customerGid = it.gid
                })
            } else {

                customerGid = it.content[0].gid

                viewModelPaymentSession.getPaymentMethod(
                    URLEncoder.encode(
                        it.content[0].gid,
                        "UTF-8"
                    )
                )
            }
        })

        viewModelPaymentSession.paymentMethodResults.observe(this, {

            binding.progress.visibility = View.GONE

//            val savedCards = ArrayList<PaymentCard>()
//            val savedUpi = ArrayList<PaymentUpi>()
            val paymentMethodCard = ArrayList<_PaymentMethodCard>()
            val cardsMap = HashMap<PaymentCard, String>()

            for (paymentMethod in it.content) {
                if (paymentMethod.card != null) {
//                    savedCards.add(paymentMethod.card)
                    cardsMap.put(paymentMethod.card, paymentMethod.gid)
                }

//                if (paymentMethod.upi != null)
//                    savedUpi.add(paymentMethod.upi)
            }

//            val distinctCards: List<PaymentCard> = savedCards.distinctBy { it.lastFour }
//            val distinctUpi: List<PaymentUpi> = savedUpi.distinctBy { it.vpa }

            if (cardsMap.isNotEmpty()) {
                binding.savedCardsLayout.visibility = View.VISIBLE
                binding.savedCardsView.layoutManager = LinearLayoutManager(this)

                for ((k, v) in cardsMap) {
                    val section = _PaymentMethodCard(k, v)

                    paymentMethodCard.add(section)
                }

                val adapter = CustomAdapter(
                    this,
                    paymentMethodCard,
                    orderInfo.amount,
                    object : CustomAdapter.PayListener {
                        override fun onPayClick(cvv: String, cardGid: String, gid: String) {

                            updateSecurityCode(cvv, cardGid, gid)
                        }
                    })
                binding.savedCardsView.adapter = adapter
            }

//            if (distinctUpi.isNotEmpty()) {
//                binding.savedUpiLayout.visibility = View.VISIBLE
//                binding.savedUpiView.layoutManager = LinearLayoutManager(this)
//
//                val adapter = UpiCustomAdapter(this, distinctUpi)
//                binding.savedUpiView.adapter = adapter
//            }
        })

        funcCard()

        funcUPI()

        funcBank()

        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                if (msg.obj != null) {
                    createResult(msg.obj as PaymentSessionResponse)

                    if (paymentResult != null) {
                        setResult(RESULT_OK, Intent().apply {
                            putExtra(SwirepaySdk.STATUS, 1)
                            putExtra(SwirepaySdk.RESULT, paymentResult)
                        })
                    }
                } else {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(SwirepaySdk.STATUS, -1)
                        putExtra(
                            PaymentActivity.FAILURE_REASON,
                            "Payment Cancelled"
                        )
                        putExtra(PaymentActivity.PAYMENT_MESSAGE, "Payment Failed")
                    })
                }

                when (msg.what) {
                    0 -> finish()
                }
            }
        }
    }

    private fun updateSecurityCode(cvv: String, cardGid: String, pGid: String) {

        val cardInfo = CardInfo(cvv)

        viewModelCard.updateCVV(cardInfo, cardGid)

        viewModelCard.liveCardResponse.observe(this, {

            val arrayList: ArrayList<PaymentMethodType> = ArrayList()
            arrayList.add(PaymentMethodType.CARD)

            orderInfo.paymentMethodGid = pGid
            orderInfo.paymentMethodType = arrayList

            viewModelPaymentSession.createPaymentSession(orderInfo)
        })
    }

    private fun funcCard() {

        cardNumber =
            binding.cardExpandable.secondLayout.findViewById(R.id.editText_cardNumber)
        cardNumberTextInput =
            binding.cardExpandable.secondLayout.findViewById(R.id.textInputLayout_cardNumber)
//        cardNumber.setAmexCardFormat(true)
        expiryDate =
            binding.cardExpandable.secondLayout.findViewById(R.id.editText_expiryDate)
        securityCode =
            binding.cardExpandable.secondLayout.findViewById(R.id.editText_securityCode)
        cardHolder =
            binding.cardExpandable.secondLayout.findViewById(R.id.editText_cardHolder)
        payNow =
            binding.cardExpandable.secondLayout.findViewById(R.id.payNow)
        cardLogo =
            binding.cardExpandable.secondLayout.findViewById(R.id.cardBrandLogo_imageView_primary)
        storePaymentMethod =
            binding.cardExpandable.secondLayout.findViewById(R.id.storePaymentMethod)
        payNow.text =
            "Pay " + resources.getString(R.string.Rs) + (orderInfo.amount / 100).toDouble()
        payNow.isEnabled = false

        var isSaved = false

        storePaymentMethod.setOnCheckedChangeListener { buttonView, isChecked ->
            isSaved = isChecked
        }

        cardNumber.addTextChangedListener(cardTextWatcher)

        expiryDate.addTextChangedListener(cardTextWatcher)

        securityCode.addTextChangedListener(cardTextWatcher)

        cardHolder.addTextChangedListener(cardTextWatcher)

        payNow.setOnClickListener {

//            if (cardNumber.rawValue.length < 16) {
//                cardNumberTextInput.error = "Invalid card Number"
//                return@setOnClickListener
//            }

            val cvv = securityCode.text.toString()
            val expiryMonth = expiryDate.date.expiryMonth
            val expiryYear = expiryDate.date.expiryYear
            val number = cardNumber.rawValue
            val name = cardHolder.text.toString()

            binding.progress.visibility = View.VISIBLE
            val card = Card(cvv, expiryMonth, expiryYear, name, number)
            val paymentMethod = PaymentMethodCard(card, "CARD", customerGid, isSaved)

            viewModelPaymentSession.createPaymentMethod(paymentMethod)
        }

        viewModelPaymentSession.livePaymentMethod.observe(this, {

            val arrayList: ArrayList<PaymentMethodType> = ArrayList()
            arrayList.add(PaymentMethodType.CARD)

            orderInfo.paymentMethodGid = it.gid
            orderInfo.paymentMethodType = arrayList
            viewModelPaymentSession.createPaymentSession(orderInfo)
        })

        viewModelPaymentSession.livePaymentSession.observe(this, {

            binding.progress.visibility = View.GONE

//            createResult(it)

//            val paymentResult: PaymentResult = it as PaymentResult
//            Log.e("Result", Gson().toJson(paymentResult))

            redirect(it.nextActionUrl)
        })

        viewModelPaymentSession.liveErrorMessages.observe(this, { message ->

            binding.progress.visibility = View.GONE

            onSnack("$message  Failed to load.")
        })
    }

    private fun funcUPI() {

        val phoneNumber = customer.phoneNumber

        mobileNumber =
            binding.upiExpandable.secondLayout.findViewById(R.id.editText_mobile)
        val upiNumber = phoneNumber.substring(3, phoneNumber.length)
        mobileTextInput =
            binding.upiExpandable.secondLayout.findViewById(R.id.textInputLayout_mobile)
        payNowUpi =
            binding.upiExpandable.secondLayout.findViewById(R.id.payNowUpi)
        payNowUpi.text = "Verify & Pay"
        payNowUpi.isEnabled = false

//        mobileNumber.setText(upiNumber)

        val gpay = binding.upiExpandable.secondLayout.findViewById<ImageView>(R.id.gpay)
        val paytm = binding.upiExpandable.secondLayout.findViewById<ImageView>(R.id.paytm)
        val phonePay = binding.upiExpandable.secondLayout.findViewById<ImageView>(R.id.phonepe)
        val amazonPay = binding.upiExpandable.secondLayout.findViewById<ImageView>(R.id.amazonPay)

        gpay.setOnClickListener {
            mobileNumber.setText(upiNumber)
        }

        paytm.setOnClickListener {
            mobileNumber.setText("$upiNumber@paytm")
        }

        phonePay.setOnClickListener {
            mobileNumber.setText("$upiNumber@ybl")
        }

        amazonPay.setOnClickListener {
            mobileNumber.setText("$upiNumber@apl")
        }

        mobileNumber.addTextChangedListener(upiTextWatcher)

        payNowUpi.setOnClickListener {

            binding.progress.visibility = View.VISIBLE

            val upi = Upi(mobileNumber.rawValue)
            val paymentMethod = PaymentMethodUpi(upi, "UPI", customerGid)

            viewModelUPI.createPaymentMethodUPI(paymentMethod)
        }

        viewModelUPI.livePaymentMethodResponse.observe(this, {

            orderInfo.paymentMethodGid = it.gid
            val arrayList: ArrayList<PaymentMethodType> = ArrayList()
            arrayList.add(PaymentMethodType.UPI)
            orderInfo.paymentMethodType = arrayList
            viewModelUPI.createPaymentSession(orderInfo)
        })

        viewModelUPI.livePaymentSessionResponse.observe(this, {

            binding.progress.visibility = View.GONE

//            createResult(it)

//            Might require in future for native
//            val bottomSheetDialog = BottomSheetDialog.newInstance(it.upi.vpa)
//            bottomSheetDialog.show(supportFragmentManager, "UPI_BottomSheet")

            redirect(it.nextActionUrl)
        })

        viewModelUPI.liveErrorMessages.observe(this, { message ->

            binding.progress.visibility = View.GONE

            onSnack("$message  Failed to load.")
        })
    }

    private fun funcBank() {

        editTextBanks = binding.netBankExpandable.secondLayout.findViewById(R.id.editText_bank)

        val adapter = ObjectAdapter(this, swirepayBanks)
        editTextBanks.setAdapter(adapter)
        editTextBanks.threshold = 1
//        editTextBanks.dismissDropDown()

//        if (BuildConfig.DEBUG)
//            viewModelNetBanking.getAllBanks(true)
//        else
//            viewModelNetBanking.getAllBanks(false)
//
//        viewModelNetBanking.livePaymentBanks.observe(this, {
//
//            banks = it
//
//            val adapter = ObjectAdapter(this, banks)
//            editTextBanks.setAdapter(adapter)
//            editTextBanks.threshold = 1
//            editTextBanks.dismissDropDown()
//        })

        payNowBank = binding.netBankExpandable.secondLayout.findViewById(R.id.payNowBank)
        payNowBank.text =
            "Pay " + resources.getString(R.string.Rs) + (orderInfo.amount / 100).toDouble()
        payNowBank.isEnabled = false

        editTextBanks.addTextChangedListener(bankTextWatcher)

        editTextBanks.setOnItemClickListener { _, _, position, _ ->

            val bankStr: String = editTextBanks.adapter.getItem(position).toString()
            for (bank in swirepayBanks) {
                if (bank.bankName == bankStr) {
                    bankId = bank.gid
                }
            }
        }

        payNowBank.setOnClickListener {

            binding.progress.visibility = View.VISIBLE

            val netBanking = NetBanking(bankId)
            val netBank = PaymentMethodNetBank(netBanking, "NET_BANKING", customerGid)

            viewModelNetBanking.createPaymentMethodNetBanking(netBank)
        }

        viewModelNetBanking.liveNetBankingResponse.observe(this, {

            orderInfo.paymentMethodGid = it.gid
            val arrayList: ArrayList<PaymentMethodType> = ArrayList()
            arrayList.add(PaymentMethodType.NET_BANKING)
            orderInfo.paymentMethodType = arrayList
            viewModelNetBanking.createPaymentSession(orderInfo)
        })

        viewModelNetBanking.liveNetBankingSessionResponse.observe(this, {

            binding.progress.visibility = View.GONE

            if (it.status == "REQUIRE_PAYMENT_METHOD") {

                onSnack("Netbanking from bank " + it.paymentMethod.netbanking.bankName + " is not enabled by this merchant")
            } else {

                redirect(it.nextActionUrl)
            }
        })

        viewModelNetBanking.liveErrorMessages.observe(this, { message ->

            binding.progress.visibility = View.GONE

            onSnack("$message  Failed to load.")
        })
    }

    private fun redirect(nextActionUrl: String) {

        startActivity(Intent(this, PaymentActionActivity::class.java).apply {
            putExtra(SwirepaySdk.PAYMENT_METHOD_URL, nextActionUrl)
            putExtra(SwirepaySdk.PAYMENT_AMOUNT, orderInfo.amount)
        })
    }

    private fun createResult(it: PaymentSessionResponse?) {

        if (it != null) {

            var paymentNetbank: _PaymentNetBanking? = null
            var paymentCard: _PaymentCard? = null
            var paymentUpi: _PaymentUpi? = null

            val paymentSession = _PaymentSession(
                it.gid, it.amount, it.currency.name,
                it.authCode, it.paymentDate, it.meta, it.status
            )

            if (it.paymentMethod.card != null) {
                paymentCard = _PaymentCard(
                    it.paymentMethod.card.gid,
                    it.paymentMethod.card.scheme,
                    it.paymentMethod.card.expiryYear,
                    it.paymentMethod.card.expiryMonth,
                    it.paymentMethod.card.lastFour
                )
            }

            if (it.paymentMethod.upi != null) {
                paymentUpi = _PaymentUpi(it.paymentMethod.upi.gid, it.paymentMethod.upi.vpa)
            }

            if (it.paymentMethod.netbanking != null) {
                paymentNetbank = _PaymentNetBanking(
                    it.paymentMethod.netbanking.gid,
                    it.paymentMethod.netbanking.bankName
                )
            }
            val paymentType = _PaymentType(it.paymentMethod.paymentType.category)
            val paymentMethod = _PaymentMethod(it.paymentMethod.gid, paymentType)
            val customer = _Customer(
                it.customer.gid,
                it.customer.name,
                it.customer.email,
                it.customer.phoneNumber
            )

            paymentResult = SPPaymentResult(
                paymentSession,
                paymentMethod,
                paymentCard,
                paymentUpi,
                paymentNetbank,
                customer
            )
        }
    }

    private val cardTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {

        }

        override fun beforeTextChanged(
            s: CharSequence, start: Int,
            count: Int, after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence, start: Int, before: Int, count: Int
        ) {
            cardLogo.setImageResource(CardType.forCardNumber(cardNumber.text.toString()).cardDrawable)

            payNow.isEnabled = cardNumber.text.toString().isNotEmpty() && expiryDate.text.toString()
                .isNotEmpty() &&
                    securityCode.text.toString().isNotEmpty() && cardHolder.text.toString()
                .isNotEmpty()
        }
    }

    private val upiTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {

        }

        override fun beforeTextChanged(
            s: CharSequence, start: Int,
            count: Int, after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence, start: Int, before: Int, count: Int
        ) {

            payNowUpi.isEnabled = mobileNumber.text.toString().isNotEmpty()
        }
    }

    private val bankTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {

        }

        override fun beforeTextChanged(
            s: CharSequence, start: Int,
            count: Int, after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence, start: Int, before: Int, count: Int
        ) {
            payNowBank.isEnabled = editTextBanks.text.toString().isNotEmpty()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            showDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.amount_menu, menu)

        val item = menu!!.findItem(R.id.amount)
        val s = SpannableString(getString(R.string.Rs) + " " + (orderInfo.amount / 100))
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDialog()
            return false
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun showDialog() {

        AlertDialog.Builder(this)
            .setTitle("Payment")
            .setMessage("Are you sure you want to exit payment?")
            .setPositiveButton("Exit",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    finish()
                })
            .setNegativeButton(
                "No",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun getAssetJsonData(context: Context): String {
        var json: String
        try {
            val inputStream: InputStream = context.assets.open("swirepaybanks.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }

    private fun collapse(v: ExpandableLayout) {
        v.collapse()
    }

    companion object {
        lateinit var handler: Handler
        var paymentResult: SPPaymentResult? = null
    }

    private fun onSnack(message: String) {
        val snackbar = Snackbar.make(
            findViewById(R.id.parentView), message,
            3000
        )
//            .setAction("Action", null)
//        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(resources.getColor(R.color.primaryColor))
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 15f
        snackbar.show()
    }
}