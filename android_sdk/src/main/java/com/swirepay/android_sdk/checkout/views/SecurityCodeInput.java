package com.swirepay.android_sdk.checkout.views;

import android.content.Context;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SecurityCodeInput extends CardNumberInput {

    private static final int MAX_LENGTH = 3;

    public SecurityCodeInput(@NonNull Context context) {
        this(context, null);
    }

    public SecurityCodeInput(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecurityCodeInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        enforceMaxInputLength(MAX_LENGTH);
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
}
