package com.swirepay.android_sdk.checkout.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.expandablelayout.ExpandableLayout
import com.swirepay.android_sdk.R
import com.swirepay.android_sdk.checkout.model._PaymentMethodCard
import com.swirepay.android_sdk.checkout.ui.activity.CheckoutActivity
import com.swirepay.android_sdk.checkout.views.FadedDisableButton
import com.swirepay.android_sdk.checkout.views.SecurityCodeInput

class CustomAdapter(
    val context: Context,
    val mList: List<_PaymentMethodCard>,
    val currency:String,
    val amount: Int,
    val payListener: PayListener
) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    companion object {
        var mPayListener: PayListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_card_item, parent, false)

        return ViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        mPayListener = payListener

        val card = mList[position].paymentCard
        println("card====prefix ${card.currency?.prefix}")
        println("card====name ${card.currency?.name}")
        var drawable: Int = R.drawable.ic_card
        when (card.scheme) {
            "VISA" -> drawable = R.drawable.ic_visa
            "MASTERCARD" -> drawable = R.drawable.ic_master
            "MAESTRO" -> drawable = R.drawable.ic_maestro
            "AMEX" -> drawable = R.drawable.ic_amex
            "RUPAY" -> drawable = R.drawable.ic_rupay
            "DISCOVER" -> drawable = R.drawable.ic_discover
            "DINERS" -> drawable = R.drawable.ic_diners_club
        }
//        when (card.bankName) {
//            "VISA" -> drawable = R.drawable.ic_visa
//            "MASTER" -> drawable = R.drawable.ic_master
//            "MAESTRO" -> drawable = R.drawable.ic_maestro
//            "AMEX" -> drawable = R.drawable.ic_amex
//            "RUPAY" -> drawable = R.drawable.ic_rupay
//            "DISCOVER" -> drawable = R.drawable.ic_discover
//            "DINERS" -> drawable = R.drawable.ic_diners_club
//        }
        holder.cardLogo.setImageResource(drawable)
        holder.cardNumber.text = "XXXX-XXXX-" + card.lastFour

        holder.expandableLayout.parentLayout.setOnClickListener {
            holder.expandableLayout.toggleLayout()
        }

        val amount = String.format(
            "%.2f",
            CheckoutActivity.dec.format(amount / 100.00).toString().toFloat()
        )
        println("currency ==== $currency")

        if(currency == "USD")
        {
            holder.payNow.text =
                "Pay " + context.resources.getString(R.string.dollar) + amount
        }
        else {
            holder.payNow.text =
                "Pay " + context.resources.getString(R.string.Rs) + amount
        }


        holder.payNow.isEnabled = false

        holder.payNow.setOnClickListener {
            if (mPayListener != null) {
                if (holder.securityCode.text.toString() != "")
                    mPayListener?.onPayClick(
                        holder.securityCode.text.toString(),
                        card.gid,
                        mList[position].gid
                    )
//                else if (holder.securityCode.text.toString().length < 3) {
//                    Toast.makeText(context, "Invalid CVC number", Toast.LENGTH_LONG).show()
//                }
                else
                    Toast.makeText(context, "Cvv cannot be empty", Toast.LENGTH_LONG).show()
            }
        }

        holder.securityCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (holder.securityCode.text.toString() != "") {
                    holder.payNow.isEnabled = true;
                }
            }
        })
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val expandableLayout: ExpandableLayout = itemView.findViewById(R.id.cardExpandable)

        val cardLogo: ImageView =
            expandableLayout.parentLayout.findViewById(R.id.cardLogo)
        val cardNumber: TextView =
            expandableLayout.parentLayout.findViewById(R.id.cardNumber)

        val payNow: FadedDisableButton =
            expandableLayout.secondLayout.findViewById(R.id.payNow)
        val securityCode: SecurityCodeInput =
            expandableLayout.secondLayout.findViewById(R.id.editText_securityCode)
    }

    open interface PayListener {
        fun onPayClick(cvv: String, cardGid: String?, gid: String)
    }
}