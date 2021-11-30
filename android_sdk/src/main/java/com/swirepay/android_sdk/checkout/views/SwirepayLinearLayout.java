package com.swirepay.android_sdk.checkout.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import com.swirepay.android_sdk.Configuration;
import com.swirepay.android_sdk.checkout.ComponentView;
import com.swirepay.android_sdk.checkout.OutputData;
import com.swirepay.android_sdk.checkout.ViewableComponent;

import java.util.Locale;

public abstract class SwirepayLinearLayout<
        OutputDataT extends OutputData,
        ConfigurationT extends Configuration,
        ComponentStateT,
        ComponentT extends ViewableComponent<OutputDataT, ConfigurationT, ComponentStateT>>
        extends LinearLayout implements ComponentView<OutputDataT, ComponentT> {

    private ComponentT mComponent;

    @NonNull
    protected Context mLocalizedContext;

    public SwirepayLinearLayout(@NonNull Context context) {
        super(context);
    }

    public SwirepayLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwirepayLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVisibility(isInEditMode() ? VISIBLE : GONE);
    }

    @Override
    public void attach(@NonNull ComponentT component, @NonNull LifecycleOwner lifecycleOwner) {
        mComponent = component;

        onComponentAttached();
        initLocalization(mComponent.getConfiguration().getShopperLocale());
        initView();
        initLocalizedStrings(mLocalizedContext);
        setVisibility(VISIBLE);
        mComponent.sendAnalyticsEvent(getContext());
        this.observeComponentChanges(lifecycleOwner);
    }

    @NonNull
    protected ComponentT getComponent() {
        if (mComponent == null) {
            throw new RuntimeException("Should not get Component before it's attached");
        }
        return mComponent;
    }

    private void initLocalization(@NonNull Locale shopperLocale) {
        // We need to get the strings from the styles instead of the strings.xml because merchants can override them.
        final android.content.res.Configuration configuration = getContext().getResources().getConfiguration();
        final android.content.res.Configuration newConfig = new android.content.res.Configuration(configuration);
        newConfig.setLocale(shopperLocale);
        mLocalizedContext = getContext().createConfigurationContext(newConfig);
    }

    /**
     * Set the view Strings based on the localized context.
     * @param localizedContext A configuration context with the Locale from the Component Configuration.
     */
    protected abstract void initLocalizedStrings(@NonNull Context localizedContext);

    /**
     * This function will be called after the component got attached and the view got initialized.
     * It's better to Observer on live data objects here.
     */
    protected abstract void observeComponentChanges(@NonNull LifecycleOwner lifecycleOwner);
}
