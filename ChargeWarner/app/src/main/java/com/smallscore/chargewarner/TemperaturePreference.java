package com.smallscore.chargewarner;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

public class TemperaturePreference extends NumberPickerPreferenceAbstract {

    public TemperaturePreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TemperaturePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View onCreateDialogView() {
        mPicker = new NumberPicker(getContext());
        mPicker.setMinValue(minValue);
        mPicker.setMaxValue(Constants.HIGH_TEMPERATURE);
        mPicker.setValue(mNumber);
        return mPicker;
    }

    @Override
    public CharSequence getSummary() {
        return super.getContext().getString(R.string.temp_pref_summary) + ": " + mNumber + " " +super.getContext().getString(R.string.degrees_celcius);
    }
}