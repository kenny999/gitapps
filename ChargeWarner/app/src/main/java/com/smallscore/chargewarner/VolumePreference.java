package com.smallscore.chargewarner;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

public class VolumePreference extends NumberPickerPreferenceAbstract {

    public VolumePreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View onCreateDialogView() {
        mPicker = new NumberPicker(getContext());
        mPicker.setMinValue(minValue);
        mPicker.setMaxValue(Constants.WARNING_MAX_VOLUME);
        mPicker.setValue(mNumber);
        return mPicker;
    }

    @Override
    public CharSequence getSummary() {
        return "" + mNumber + " / "+ Constants.WARNING_MAX_VOLUME;
    }
}