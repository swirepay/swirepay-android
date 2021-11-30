package com.swirepay.android_sdk.checkout.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.swirepay.android_sdk.R
import android.os.CountDownTimer
import android.widget.LinearLayout
import android.widget.TextView
import com.swirepay.android_sdk.checkout.ui.activity.CheckoutActivity
import java.util.concurrent.TimeUnit

class BottomSheetDialog : BottomSheetDialogFragment() {

    lateinit var countDownTimer: CountDownTimer

    companion object {
        fun newInstance(vpa: String): BottomSheetDialog =
            BottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putString("VPA", vpa.toString())
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View = inflater.inflate(
            R.layout.bottom_sheet_persistent,
            container, false
        )

        val vpa = arguments?.getString("VPA")

        val cancelLayout: LinearLayout = v.findViewById(R.id.cancelLayout)
        val upiRequest: LinearLayout = v.findViewById(R.id.upiRequest)
        val btnCancel: MaterialButton = v.findViewById(R.id.btnCancel)
        val btnPositive: MaterialButton = v.findViewById(R.id.btnPositive)
        val btnNegative: MaterialButton = v.findViewById(R.id.btnNegative)
        val txtUpi: TextView = v.findViewById(R.id.upi)
        val paymentSes: TextView = v.findViewById(R.id.payment_ses)
        val cancelSes: TextView = v.findViewById(R.id.cancel_ses)

        txtUpi.text = vpa

        btnCancel.setOnClickListener {
            cancelLayout.visibility = View.VISIBLE
            upiRequest.visibility = View.GONE
        }

        btnPositive.setOnClickListener {
            dismiss()
        }

        btnNegative.setOnClickListener {
            cancelLayout.visibility = View.GONE
            upiRequest.visibility = View.VISIBLE
        }

        countDownTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                paymentSes.text = String.format(
                    "%02d Minutes : %02d Seconds",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    millisUntilFinished
                                )
                            )
                )

                cancelSes.text = String.format(
                    "%02d Minutes : %02d Seconds",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    millisUntilFinished
                                )
                            )
                )
            }

            override fun onFinish() {
                dismiss()
//                val complete = CheckoutActivity()
//                complete.onComplete()
            }
        }.start()

        return v
    }
}