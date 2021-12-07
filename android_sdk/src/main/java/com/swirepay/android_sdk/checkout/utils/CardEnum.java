package com.swirepay.android_sdk.checkout.utils;

import androidx.annotation.DrawableRes;

import com.swirepay.android_sdk.R;

enum CardEnum {

    AmericanExpress("American Express", R.drawable.ic_amex, "34, 37", "15"),
    //    CardGuard("Card Guard", R.drawable.payment_ic_method, "5392", "16"),
//    ChinaUnionPay("China Union Pay", R.drawable.payment_ic_unionpay, "62", "16-19"),
//    Dankort("Dankort", R.drawable.payment_ic_method, "5019", "16"),
    DinersClub("Diners Club", R.drawable.ic_diners_club, "300-305, 309, 36, 38, 39", "14,16-19"),
    Discover("Discover", R.drawable.ic_discover, "6011, 622126 to 622925, 644, 645, 646, 647, 648, 649, 65", "16,19"),
    //    InstaPayment("Insta Payment", R.drawable.payment_ic_method, "637, 638, 639", "16"),
//    JCB("JCB", R.drawable.payment_ic_method, "3528-3589", "16-19"),
    Maestro("Maestro", R.drawable.ic_maestro, "5018, 5020, 5038, 5893, 6304, 6759, 6761, 6762, 6763", "12-19"),
    MasterCard("Master", R.drawable.ic_master, "51, 52, 53, 54, 55, 222100-272099", "16"),
    //    MIR("Mir", R.drawable.payment_ic_method, "2200 - 2204", "16"),
//    Troy("Troy", R.drawable.payment_ic_method, "979200-979289", "16"),
//    UATP("Universal Air Travel Plan", R.drawable.payment_ic_method, "1", "15"),
//    Verve("Verve", R.drawable.payment_ic_verve, "506099-506198, 650002-650027", "16,19"),
//    VisaElectron("Visa Electron", R.drawable.payment_ic_method, "4026, 417500, 4508, 4844, 4913, 4917", "16"),
    Visa("Visa", R.drawable.ic_visa, "4", "13,16,19"),
    Rupay("Rupay", R.drawable.ic_rupay, "60, 65, 81, 82, 508", "15,16"),
    Empty("Empty", R.drawable.ic_card, "", "16");


    CardEnum(String cardName, @DrawableRes int icon, String startWith, String length) {
        this.cardName = cardName;
        this.icon = icon;
        this.startWith = startWith;
        this.length = length;
    }

    private int icon;
    private String startWith, length, cardName;

    public int getIcon() {
        return this.icon;
    }

    public String getStartWith() {
        return this.startWith;
    }

    public String getLength() {
        return this.length;
    }

    public String getCardName() {
        return this.cardName;
    }
}
