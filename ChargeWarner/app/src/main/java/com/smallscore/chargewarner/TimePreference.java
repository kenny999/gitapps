package com.smallscore.chargewarner;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.preference.Preference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kenneth on 2015-08-21.
 */
public class TimePreference extends Preference implements TimePickerDialog.OnTimeSetListener{

    private int lastHour=23;
    private int lastMinute=0;
    private static String defaultTime = "23:00";

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimePreference(Context context) {
        super(context);
    }

    public static String getDefaultTime() {
        return defaultTime;
    }

    public static int getHour(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[1]));
    }

    protected void onClick() {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        Activity activity = (Activity) getContext();
        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog t = new TimePickerDialog(activity, this, hour, minute,
                DateFormat.is24HourFormat(activity));
        t.show();

/*        if (callChangeListener(null)) {
            persistString(null);
        }
  */
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        lastHour=hourOfDay;
        lastMinute = minute;
        String time=String.valueOf(lastHour)+":"+String.valueOf(lastMinute);
        if (callChangeListener(time)) {
            persistString(time);
            setSummary(getSummary());
        }
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time=null;

        if (restoreValue) {
            if (defaultValue==null) {
                time=getPersistedString(defaultTime);
            }
            else {
                time=getPersistedString(defaultValue.toString());
            }
        }
        else {
            time=defaultValue.toString();
        }

        lastHour=getHour(time);
        lastMinute=getMinute(time);
    }

    @Override
    public CharSequence getSummary() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, lastHour);
        calendar.set(Calendar.MINUTE, lastMinute);
        return DateFormat.getTimeFormat(getContext()).format(new Date(calendar.getTimeInMillis()));
    }
}
