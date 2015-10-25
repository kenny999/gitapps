package com.smallscore.chargewarner;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.NumberPicker;

/**
 * Created by kenneth on 2015-10-14.
 */
public abstract class NumberPickerPreferenceAbstract extends DialogPreference {

    protected NumberPicker mPicker;
    protected Integer mNumber = 0;
    protected static int minValue = 0;

    public NumberPickerPreferenceAbstract(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberPickerPreferenceAbstract(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // needed when user edits the text field and clicks OK
            mPicker.clearFocus();
            setValue(mPicker.getValue());
        }
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(mNumber) : (Integer) defaultValue);
    }

    public void setValue(int value) {
        if (shouldPersist()) {
            persistInt(value);
        }

        if (value != mNumber) {
            mNumber = value;
            notifyChanged();
            setSummary(getSummary());
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

}
