package com.swirepay.android_sdk.checkout.ui.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.skydoves.expandablelayout.ExpandableLayout
import com.swirepay.android_sdk.R
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.checkout.*
import com.swirepay.android_sdk.checkout.interfaces.PaymentCompleteListener
import com.swirepay.android_sdk.checkout.model.*
import com.swirepay.android_sdk.checkout.ui.adapter.ObjectAdapter
import com.swirepay.android_sdk.checkout.utils.CardType
import com.swirepay.android_sdk.checkout.utils.StatusbarUtil
import com.swirepay.android_sdk.checkout.views.*
import com.swirepay.android_sdk.databinding.ActivityCheckOutBinding
import com.swirepay.android_sdk.model.Banks
import com.swirepay.android_sdk.model.PaymentSession
import android.os.Handler
import android.os.Message
import com.swirepay.android_sdk.checkout.viewmodel.ViewModelCustomer
import com.swirepay.android_sdk.checkout.viewmodel.ViewModelNetBanking
import com.swirepay.android_sdk.checkout.viewmodel.ViewModelPaymentSession
import com.swirepay.android_sdk.checkout.viewmodel.ViewModelPaymentUPI


class CheckoutActivity : AppCompatActivity(), PaymentCompleteListener {

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
    lateinit var paymentSession: PaymentSession
    lateinit var customer: SPCustomer
    lateinit var editTextBanks: AutoCompleteTextView
    lateinit var banks: List<Banks>
    var bankId: Int = 0

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

    val viewModelNetBanking: ViewModelNetBanking by lazy {
        ViewModelProvider(
            this
        ).get(ViewModelNetBanking::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        StatusbarUtil.changeStatusbarColor(this.window)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        paymentSession = intent.getParcelableExtra(SwirepaySdk.PAYMENT_SESSION)!!
        customer = intent.getParcelableExtra(SwirepaySdk.PAYMENT_CUSTOMER)!!

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.title = "Order"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor(SwirepaySdk.TOOLBAR_COLOR)))
        toolbar.setTitleTextColor(Color.parseColor(SwirepaySdk.TOOLBAR_ITEM))

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

        binding.progress.visibility = View.VISIBLE
        viewModelCustomer.createCustomer(customer)

        viewModelCustomer.liveCustomerResponse.observe(this, {
            binding.progress.visibility = View.GONE
            customerGid = it.gid
        })

//        viewModelCustomer.getCustomer(
//            SwirepaySdk.upiCustomer.name,
//            URLEncoder.encode(SwirepaySdk.upiCustomer.email, "UTF-8"),
//            URLEncoder.encode(SwirepaySdk.upiCustomer.phoneNumber, "UTF-8")
//        )
//
//        viewModelCustomer.liveGetCustomerResponse.observe(this, {
//            binding.progress.visibility = View.GONE
//
//            viewModelPaymentSession.getPaymentMethod(URLEncoder.encode(it.content[0].gid, "UTF-8"))
//        })
//
//        viewModelPaymentSession.paymentMethodResults.observe(this, {
//
//            binding.progress.visibility = View.GONE
//
//            val savedCards = ArrayList<PaymentCard>()
//            val savedUpi = ArrayList<PaymentUpi>()
//
//            for (paymentMethod in it.content) {
//                if (paymentMethod.card != null)
//                    savedCards.add(paymentMethod.card)
//
//                if (paymentMethod.upi != null)
//                    savedUpi.add(paymentMethod.upi)
//            }
//
//            val distinctCards: List<PaymentCard> = savedCards.distinctBy { it.lastFour }
//            val distinctUpi: List<PaymentUpi> = savedUpi.distinctBy { it.vpa }
//
//            if (distinctCards.isNotEmpty()) {
//                binding.savedCardsLayout.visibility = View.VISIBLE
//                binding.savedCardsView.layoutManager = LinearLayoutManager(this)
//
//                val adapter = CustomAdapter(this, distinctCards, paymentSession.amount)
//                binding.savedCardsView.adapter = adapter
//            }
//
//            if (distinctCards.isNotEmpty()) {
//                binding.savedUpiLayout.visibility = View.VISIBLE
//                binding.savedUpiView.layoutManager = LinearLayoutManager(this)
//
//                val adapter = UpiCustomAdapter(this, distinctUpi)
//                binding.savedUpiView.adapter = adapter
//            }
//        })

        funcCard()

        funcUPI()

        funcBank()

        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                if (msg.obj as Boolean) {
                    if (paymentResult != null) {
                        setResult(RESULT_OK, Intent().apply {
                            putExtra(SwirepaySdk.STATUS, 1)
                            putExtra(SwirepaySdk.RESULT, paymentResult)
                        })
                        Log.d("Result", Gson().toJson(paymentResult))
                    }
                } else {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(SwirepaySdk.RESULT, "Payment Failed")
                    })
                }

                when (msg.what) {
                    0 -> finish()
                }
            }
        }
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
        payNow.text =
            "Pay " + resources.getString(R.string.Rs) + (paymentSession.amount / 100).toDouble()
        payNow.isEnabled = false

        cardNumber.addTextChangedListener(cardTextWatcher)

        expiryDate.addTextChangedListener(cardTextWatcher)

        securityCode.addTextChangedListener(cardTextWatcher)

        cardHolder.addTextChangedListener(cardTextWatcher)

        payNow.setOnClickListener {

            if (cardNumber.rawValue.length < 16) {
                cardNumberTextInput.error = "Invalid card Number"
                return@setOnClickListener
            }

            val cvv = securityCode.text.toString()
            val expiryMonth = expiryDate.date.expiryMonth
            val expiryYear = expiryDate.date.expiryYear
            val number = cardNumber.rawValue
            val name = cardHolder.text.toString()

            binding.progress.visibility = View.VISIBLE
            val card = Card(cvv, expiryMonth, expiryYear, name, number)
            val paymentMethod = PaymentMethodCard(card, "CARD", customerGid)

            viewModelPaymentSession.createPaymentMethod(paymentMethod)
        }

        viewModelPaymentSession.livePaymentMethod.observe(this, {

            paymentSession.paymentMethodGid = it.gid
            viewModelPaymentSession.createPaymentSession(paymentSession)
        })

        viewModelPaymentSession.livePaymentSession.observe(this, {

            binding.progress.visibility = View.GONE

            createResult(it)

            val paymentResult: PaymentResult = it as PaymentResult
            Log.e("Result", Gson().toJson(paymentResult))

            redirect(it.nextActionUrl)
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

        mobileNumber.setText(upiNumber)

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

            paymentSession.paymentMethodGid = it.gid
            viewModelUPI.createPaymentSession(paymentSession)
        })

        viewModelUPI.livePaymentSessionResponse.observe(this, {

            binding.progress.visibility = View.GONE

            createResult(it)

//            Might require in future for native
//            val bottomSheetDialog = BottomSheetDialog.newInstance(it.upi.vpa)
//            bottomSheetDialog.show(supportFragmentManager, "UPI_BottomSheet")

            redirect(it.nextActionUrl)
        })
    }

    private fun funcBank() {

        editTextBanks = binding.netBankExpandable.secondLayout.findViewById(R.id.editText_bank)

        viewModelNetBanking.getAllBanks(true)

        viewModelNetBanking.livePaymentBanks.observe(this, {

            banks = it

            val adapter = ObjectAdapter(this, banks)
            editTextBanks.setAdapter(adapter)
            editTextBanks.threshold = 1
            editTextBanks.dismissDropDown()
        })

        payNowBank = binding.netBankExpandable.secondLayout.findViewById(R.id.payNowBank)
        payNowBank.text =
            "Pay " + resources.getString(R.string.Rs) + (paymentSession.amount / 100).toDouble()
        payNowBank.isEnabled = false

        editTextBanks.addTextChangedListener(bankTextWatcher)

        editTextBanks.setOnItemClickListener { _, _, position, _ ->

            val bankStr: String = editTextBanks.adapter.getItem(position).toString()
            for (bank in banks) {
                if (bank.cashfreeBankName == bankStr)
                    bankId = bank.cashfreeBankId
            }
        }

        payNowBank.setOnClickListener {

            binding.progress.visibility = View.VISIBLE

            val netBanking = NetBanking(bankId.toString())
            val netBank = PaymentMethodNetBank(netBanking, "NET_BANKING", customerGid)

            viewModelNetBanking.createPaymentMethodNetBanking(netBank)
        }

        viewModelNetBanking.liveNetBankingResponse.observe(this, {

            paymentSession.paymentMethodGid = it.gid
            viewModelNetBanking.createPaymentSession(paymentSession)
        })

        viewModelNetBanking.liveNetBankingSessionResponse.observe(this, {

            binding.progress.visibility = View.GONE

            createResult(it)

            redirect(it.nextActionUrl)
        })
    }

    private fun redirect(nextActionUrl: String) {

        startActivity(Intent(this, PaymentActionActivity::class.java).apply {
            putExtra(SwirepaySdk.PAYMENT_METHOD_URL, nextActionUrl)
            putExtra(SwirepaySdk.PAYMENT_AMOUNT, paymentSession.amount)
        })
    }

    private fun createResult(it: PaymentSessionResponse?) {

        if (it != null) {

            var paymentNetbank: _PaymentNetBanking? = null
            var paymentCard: _PaymentCard? = null
            var paymentUpi: _PaymentUpi? = null

            val paymentSession = _PaymentSession(it.gid, it.amount, it.currency.name, it.status)

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

            if (cardNumber.text.toString().isNotEmpty() && expiryDate.text.toString()
                    .isNotEmpty() &&
                securityCode.text.toString().isNotEmpty() && cardHolder.text.toString().isNotEmpty()
            ) {
                payNow.isEnabled = true;
            }
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

            if (mobileNumber.text.toString().isNotEmpty()) {
                payNowUpi.isEnabled = true;
            }
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

            if (editTextBanks.text.toString().isNotEmpty()) {
                payNowBank.isEnabled = true;
            }
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
        val s = SpannableString(getString(R.string.Rs) + " " + (paymentSession.amount / 100))
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

    fun collapse(v: ExpandableLayout) {
        v.collapse()
    }

    override fun onComplete(status: Boolean) {

        finish()
    }

    companion object {
        lateinit var handler: Handler
        var paymentResult: SPPaymentResult? = null
    }
}