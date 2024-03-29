package com.swirepay.android_sdk.checkout.utils

import java.util.*
import java.util.regex.Pattern
import android.text.TextUtils
import com.swirepay.android_sdk.R


enum class CardType(
    regex: String,
    cardDrawable: Int,
    minCardLength: Int,
    maxCardLength: Int,
    securityCodeLength: Int,
    relaxedPrefixPattern: String?
) {
    VISA(
        "^4\\d*",
        R.drawable.ic_visa,
        16, 16,
        3, null
    ),
    MASTERCARD(
        "^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[0-1]|2720)\\d*",
        R.drawable.ic_master,
        16, 16,
        3, null
    ),
    AMEX(
        "^3[47]\\d*",
        R.drawable.ic_amex,
        15, 15,
        4, null
    ),
    MAESTRO(
        "^(5018|5020|5038|5043|5[6-9]|6020|6304|6703|6759|676[1-3])\\d*",
        R.drawable.ic_maestro,
        12, 19,
        3,
        "^6\\d*"
    ),
    DINERS_CLUB(
        "^(36)[0-9]{0,12}\$",
        R.drawable.ic_diners_club,
        14, 14,
        3, null
    ),
    DISCOVER(
        "^(6011[0-9]{0,12}|(644|645|646|647|648|649)[0-9]{0,13}|65[0-9]{0,14})\$",
        R.drawable.ic_discover,
        16, 19,
        3, null
    ),
    RUPAY(
        "^6[0-9]{15}\$",
        R.drawable.ic_rupay,
        12, 19,
        3, null
    ),
    EMPTY(
        "^$",
        R.drawable.ic_card,
        12, 19,
        3, null
    );

    /**
     * @return The regex matching this card type.
     */
    val pattern: Pattern

    /**
     * @return The relaxed prefix regex matching this card type. To be used in determining card type if no pattern matches.
     */
    val relaxedPrefixPattern: Pattern?

    /**
     * @return minimum length of a card for this
     */
    val minCardLength: Int

    /**
     * @return drawable of a card
     */
    val cardDrawable: Int

    /**
     * @return max length of a card for this
     */
    val maxCardLength: Int

    /**
     * @return The length of the current card's security code.
     */
    val securityCodeLength: Int


    /**
     * @return the locations where spaces should be inserted when formatting the card in a user
     * friendly way. Only for display purposes.
     */
    val spaceIndices: IntArray
        get() = if (this == AMEX) AMEX_SPACE_INDICES else DEFAULT_SPACE_INDICES

    /**
     * @param cardNumber The card number to validate.
     * @return `true` if this card number is locally valid.
     */
    fun validate(cardNumber: String): Boolean {
        if (TextUtils.isEmpty(cardNumber)) {
            return false
        } else if (!TextUtils.isDigitsOnly(cardNumber)) {
            return false
        }
        val numberLength = cardNumber.length
        if (numberLength < minCardLength || numberLength > maxCardLength) {
            return false
        } else if (!pattern.matcher(cardNumber)
                .matches() && relaxedPrefixPattern != null && !relaxedPrefixPattern.matcher(
                cardNumber
            ).matches()
        ) {
            return false
        }
        return isLuhnValid(cardNumber)
    }

    companion object {
        private val AMEX_SPACE_INDICES = intArrayOf(4, 10)
        private val DEFAULT_SPACE_INDICES = intArrayOf(4, 8, 12)

        /**
         * Returns the card type matching this account, or [CardType.UNKNOWN]
         * for no match.
         *
         *
         * A partial account type may be given, with the caveat that it may not have enough digits to
         * match.
         */
        fun forCardNumber(cardNumber: String): CardType {
            val patternMatch = forCardNumberPattern(cardNumber)
            if (patternMatch != EMPTY) {
                return patternMatch
            }
            val relaxedPrefixPatternMatch = forCardNumberRelaxedPrefixPattern(cardNumber)
            if (relaxedPrefixPatternMatch != EMPTY) {
                return relaxedPrefixPatternMatch
            }
            return if (!cardNumber.isEmpty()) {
                EMPTY
            } else EMPTY
        }

        fun forCardNumberPattern(cardNumber: String): CardType {
            for (cardType in values()) {
                if (cardType.pattern.matcher(cardNumber).matches()) {
                    return cardType
                }
            }
            return EMPTY
        }

        private fun forCardNumberRelaxedPrefixPattern(cardNumber: String): CardType {
            for (cardTypeRelaxed in values()) {
                if (cardTypeRelaxed.relaxedPrefixPattern != null) {
                    if (cardTypeRelaxed.relaxedPrefixPattern.matcher(cardNumber).matches()) {
                        return cardTypeRelaxed
                    }
                }
            }
            return EMPTY
        }

        /**
         * Performs the Luhn check on the given card number.
         *
         * @param cardNumber a String consisting of numeric digits (only).
         * @return `true` if the sequence passes the checksum
         * @throws IllegalArgumentException if `cardNumber` contained a non-digit (where [ ][Character.isDefined] is `false`).
         * @see [Luhn Algorithm
        ](http://en.wikipedia.org/wiki/Luhn_algorithm) */
        fun isLuhnValid(cardNumber: String?): Boolean {
            val reversed = StringBuffer(cardNumber).reverse().toString()
            val len = reversed.length
            var oddSum = 0
            var evenSum = 0
            for (i in 0 until len) {
                val c = reversed[i]
                require(Character.isDigit(c)) { String.format("Not a digit: '%s'", c) }
                val digit = Character.digit(c, 10)
                if (i % 2 == 0) {
                    oddSum += digit
                } else {
                    evenSum += digit / 5 + 2 * digit % 10
                }
            }
            return (oddSum + evenSum) % 10 == 0
        }
    }

    init {
        pattern = Pattern.compile(regex)
        this.relaxedPrefixPattern =
            if (relaxedPrefixPattern == null) null else Pattern.compile(relaxedPrefixPattern)
        this.minCardLength = minCardLength
        this.cardDrawable = cardDrawable
        this.maxCardLength = maxCardLength
        this.securityCodeLength = securityCodeLength
    }
}