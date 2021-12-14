
package com.swirepay.android_sdk.checkout.exception;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.swirepay.android_sdk.checkout.ModelObject;

import org.json.JSONException;

/**
 * Exception thrown when an issue occurs during serialization of a {@link ModelObject}.
 */
public class ModelSerializationException extends CheckoutException {

    private static final long serialVersionUID = -241916181048458214L;

    public ModelSerializationException(@NonNull Class modelClass, @Nullable JSONException cause) {
        super("Unexpected exception while serializing " + modelClass.getSimpleName() + ".", cause);
    }
}
