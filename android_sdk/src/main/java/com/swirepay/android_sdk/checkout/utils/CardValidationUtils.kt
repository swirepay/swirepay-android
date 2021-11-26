package com.swirepay.android_sdk.checkout.utils

import com.swirepay.android_sdk.checkout.model.FieldState
import com.swirepay.android_sdk.R
import com.swirepay.android_sdk.Validation
import com.swirepay.android_sdk.checkout.model.Brand
import com.swirepay.android_sdk.checkout.CardType
import com.swirepay.android_sdk.checkout.model.DetectedCardType
import com.swirepay.android_sdk.checkout.ExpiryDate
import java.util.Calendar
import java.util.GregorianCalendar

object CardValidationUtils {

    // Luhn Check
    private const val RADIX = 10
    private const val FIVE_DIGIT = 5

    // Card Number
    private const val MINIMUM_CARD_NUMBER_LENGTH = 8
    const val MAXIMUM_CARD_NUMBER_LENGTH = 19

    // Security Code
    private const val GENERAL_CARD_SECURITY_CODE_SIZE = 3
    private const val AMEX_SECURITY_CODE_SIZE = 4

    // Date
    private const val MONTHS_IN_YEAR = 12
    private const val MAXIMUM_YEARS_IN_FUTURE = 30
    private const val MAXIMUM_EXPIRED_MONTHS = 3

    /**
     * Validate card number.
     */
    fun validateCardNumber(number: String, enableLuhnCheck: Boolean, isBrandSupported: Boolean): CardNumberValidation {
        val normalizedNumber = StringUtil.normalize(number)
        val length = normalizedNumber.length
        return when {
            !StringUtil.isDigitsAndSeparatorsOnly(normalizedNumber) -> CardNumberValidation.INVALID_ILLEGAL_CHARACTERS
            length > MAXIMUM_CARD_NUMBER_LENGTH -> CardNumberValidation.INVALID_TOO_LONG
            length < MINIMUM_CARD_NUMBER_LENGTH -> CardNumberValidation.INVALID_TOO_SHORT
            !isBrandSupported -> CardNumberValidation.INVALID_UNSUPPORTED_BRAND
            enableLuhnCheck && !isLuhnChecksumValid(normalizedNumber) -> CardNumberValidation.INVALID_LUHN_CHECK
            else -> CardNumberValidation.VALID
        }
    }

    @Suppress("MagicNumber")
    private fun isLuhnChecksumValid(normalizedNumber: String): Boolean {
        var s1 = 0
        var s2 = 0
        val reverse = StringBuffer(normalizedNumber).reverse().toString()
        for (i in reverse.indices) {
            val digit = Character.digit(reverse[i], RADIX)
            if (i % 2 == 0) {
                s1 += digit
            } else {
                s2 += 2 * digit
                if (digit >= FIVE_DIGIT) {
                    s2 -= 9
                }
            }
        }
        return (s1 + s2) % 10 == 0
    }

    /**
     * Validate Expiry Date.
     */
    fun validateExpiryDate(expiryDate: ExpiryDate): FieldState<ExpiryDate> {
        if (dateExists(expiryDate)) {
            val expiryDateCalendar = getExpiryCalendar(expiryDate)
            val maxFutureCalendar = GregorianCalendar.getInstance()
            maxFutureCalendar.add(Calendar.YEAR, MAXIMUM_YEARS_IN_FUTURE)
            val maxPastCalendar = GregorianCalendar.getInstance()
            maxPastCalendar.add(Calendar.MONTH, -MAXIMUM_EXPIRED_MONTHS)

            // higher than maxPast and lower than maxFuture
            if (expiryDateCalendar >= maxPastCalendar && expiryDateCalendar <= maxFutureCalendar) {
                return FieldState(expiryDate, Validation.Valid)
            }
        }
        return FieldState(expiryDate, Validation.Invalid(R.string.checkout_expiry_date_not_valid))
    }

    /**
     * Validate Security Code.
     */
    fun validateSecurityCode(securityCode: String, cardType: DetectedCardType?): FieldState<String> {
        val normalizedSecurityCode = StringUtil.normalize(securityCode)
        val length = normalizedSecurityCode.length
        val invalidState = Validation.Invalid(R.string.checkout_security_code_not_valid)
        val validation = when {
            !StringUtil.isDigitsAndSeparatorsOnly(normalizedSecurityCode) -> invalidState
            cardType?.cvcPolicy == Brand.FieldPolicy.OPTIONAL && length == 0 -> Validation.Valid
            cardType?.cardType == CardType.AMERICAN_EXPRESS && length == AMEX_SECURITY_CODE_SIZE -> Validation.Valid
            cardType?.cardType != CardType.AMERICAN_EXPRESS && length == GENERAL_CARD_SECURITY_CODE_SIZE -> Validation.Valid
            else -> invalidState
        }
        return FieldState(normalizedSecurityCode, validation)
    }

    private fun dateExists(expiryDate: ExpiryDate): Boolean {
        return (
            expiryDate !== ExpiryDate.EMPTY_DATE &&
                expiryDate.expiryMonth != ExpiryDate.EMPTY_VALUE &&
                expiryDate.expiryYear != ExpiryDate.EMPTY_VALUE &&
                isValidMonth(expiryDate.expiryMonth) &&
                expiryDate.expiryYear > 0
            )
    }

    private fun isValidMonth(month: Int): Boolean {
        return month in 1..MONTHS_IN_YEAR
    }

    private fun getExpiryCalendar(expiryDate: ExpiryDate): Calendar {
        val expiryCalendar = GregorianCalendar.getInstance()
        expiryCalendar.clear()
        // First day of the expiry month. Calendar.MONTH is zero-based.
        expiryCalendar[expiryDate.expiryYear, expiryDate.expiryMonth - 1] = 1
        // Go to next month and remove 1 day to be on the last day of the expiry month.
        expiryCalendar.add(Calendar.MONTH, 1)
        expiryCalendar.add(Calendar.DAY_OF_MONTH, -1)
        return expiryCalendar
    }
}

enum class CardNumberValidation {
    VALID,
    INVALID_ILLEGAL_CHARACTERS,
    INVALID_LUHN_CHECK,
    INVALID_TOO_SHORT,
    INVALID_TOO_LONG,
    INVALID_UNSUPPORTED_BRAND
}