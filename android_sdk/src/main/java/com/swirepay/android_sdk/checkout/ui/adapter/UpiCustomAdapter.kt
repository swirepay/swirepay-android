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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.expandablelayout.ExpandableLayout
import com.swirepay.android_sdk.R
import com.swirepay.android_sdk.checkout.model.PaymentCard
import com.swirepay.android_sdk.checkout.model.PaymentUpi
import com.swirepay.android_sdk.checkout.utils.CardType
import com.swirepay.android_sdk.checkout.views.FadedDisableButton
import com.swirepay.android_sdk.checkout.views.SecurityCodeInput

class UpiCustomAdapter(val context: Context, val mList: List<PaymentUpi>) :
    RecyclerView.Adapter<UpiCustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_upi_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val upi = mList[position]
        var drawable: Int
        drawable = when {
            upi.vpa.contains("@ybl") -> {
                R.drawable.ic_phonepe
            }
            upi.vpa.contains("@apl") -> {
                R.drawable.ic_amazon_pay
            }
            upi.vpa.contains("@paytm") -> {
                R.drawable.ic_paytm
            }
            upi.vpa.contains("@okaxis") -> {
                R.drawable.ic_gpay
            }
            upi.vpa.contains("@okhdfcbank") -> {
                R.drawable.ic_gpay
            }
            upi.vpa.contains("@okicici") -> {
                R.drawable.ic_gpay
            }
            upi.vpa.contains("@oksbi") -> {
                R.drawable.ic_gpay
            }
            else -> {
                R.drawable.icon_upi
            }
        }

        holder.upiLogo.setImageResource(drawable)
        holder.vpa.text = upi.vpa

        holder.parent.setOnClickListener {

        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val upiLogo: ImageView = view.findViewById(R.id.upiLogo)
        val vpa: TextView = view.findViewById(R.id.vpa)
        val parent: CardView = view.findViewById(R.id.upiParent)
    }
}