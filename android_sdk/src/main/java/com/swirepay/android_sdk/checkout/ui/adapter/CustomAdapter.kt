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
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.expandablelayout.ExpandableLayout
import com.swirepay.android_sdk.R
import com.swirepay.android_sdk.checkout.model.PaymentCard
import com.swirepay.android_sdk.checkout.utils.CardType
import com.swirepay.android_sdk.checkout.views.FadedDisableButton
import com.swirepay.android_sdk.checkout.views.SecurityCodeInput

class CustomAdapter(val context: Context, val mList: List<PaymentCard>, val amount: Int) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_card_item, parent, false)

        return ViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val card = mList[position]
        var drawable: Int = R.drawable.ic_card
        when (card.scheme) {
            "VISA" -> drawable = R.drawable.ic_visa
            "MASTER" -> drawable = R.drawable.ic_master
            "MAESTRO" -> drawable = R.drawable.ic_maestro
            "AMERICAN EXPRESS" -> drawable = R.drawable.ic_amex
        }
        holder.cardLogo.setImageResource(drawable)
        holder.cardNumber.text = "XXXX-...-XXXX-" + card.lastFour

        holder.expandableLayout.parentLayout.setOnClickListener {
            holder.expandableLayout.toggleLayout()
        }

        holder.payNow.text =
            "Pay " + context.resources.getString(R.string.Rs) + (amount / 100).toDouble()
        holder.payNow.isEnabled = false

        holder.securityCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (holder.securityCode.text.toString().isNotEmpty()) {
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
}