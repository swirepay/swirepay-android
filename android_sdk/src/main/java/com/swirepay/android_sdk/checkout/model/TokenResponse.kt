package com.swirepay.android_sdk.checkout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenResponse(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val publicKeyDto: PublicKeyDto,
    val deleted: Boolean
) : Parcelable

@Parcelize
data class PublicKeyDto(
    val gid: String,
    val createdAt: String,
    val updatedAt: String,
    val apiKey: String,
    val pendingExpiration: Boolean,
    val lastUsed: String? = null,
    val deleted: Boolean
) : Parcelable
