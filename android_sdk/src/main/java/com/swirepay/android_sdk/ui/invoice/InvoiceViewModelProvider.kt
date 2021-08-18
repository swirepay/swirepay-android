package com.swirepay.android_sdk.ui.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.model.InvoiceRequest

class InvoiceViewModelProvider(
    val invoiceRequest: InvoiceRequest,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModelInvoice(
            invoiceRequest
        ) as T
    }
}