package com.smallscore.chargewarner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kenneth on 2015-09-04.
 */
public class Constants {

    public static final int HIGH_TEMPERATURE = 55;
    public static final String DEFAULT_RINGTONE_URI = "DEFAULT_RINGTONE_URI";
    public static final boolean LOG_TOAST = false;
    public static final String GOOGLE_PLAY_URI = "https://play.google.com/store/apps/details?id=";
    public static final String SMS_BODY_EXTRA = "sms_body";
    public static final int WARNING_MAX_VOLUME = 10;
    public static final int CHECK_TEMPERATURE_INTERVAL_SECONDS = 59;
    public static final int CHECK_BATTERY_FULL_INTERVAL_SECONDS = 61;
    public static final String CHARGER_UNPLUGGED_INTENT = "cui";
    public static final String SMS_URI_SCHEME = "sms:";

    public static class Preferences {

        public static final String NUM_SCHEDULED_WARNINGS_PREFERENCE = "NUM_SCHEDULED_WARNINGS_PREFERENCE";
        public final static String WARNING_SCREEN_IS_RUNNING_PREFERENCE = "WARNING_SCREEN_IS_RUNNING_PREFERENCE";
        public final static String TEMPERATURE_WARNING_SCREEN_IS_RUNNING_PREFERENCE = "TEMPERATURE_RUNNING_PREFERENCE";
        public final static String BATTERY_FULL_WARNING_SCREEN_IS_RUNNING_PREFERENCE = "BATTERY_FULL_WARNING_SCREEN_IS_RUNNING_PREFERENCE";
        public static final String TEMPERATURE_PREFERENCE = "temperature";
        public static final String WARNING_VOLUME_PREFERENCE = "warningVolume";
        public static final String PLAY_WHEN_SILENT_PREFERENCE = "playWhenSilent";
        public static final String WARNING_TIME_PREFERENCE = "warningTime";
        public static final String RINGTONE_PREFERENCE = "ringtone";
        public static final String TEMPERATURE_WARNING_ENABLED_PREFERENCE = "temperatureWarningEnabled";
        public static final String BATTERY_FULL_WARNING_ENABLED_PREFERENCE = "batteryFullEnabled";
        public static final String ENABLED_PREFERENCE = "enabled";
        public static final String NUM_WARNINGS_PREFERENCE = "NUM_WARNINGS_PREFERENCE";
        public static String NUM_BATTERY_FULL_WARNINGS_PREFERENCE = "NUM_BATTERY_FULL_WARNINGS_PREFERENCE";
        public static String NUM_TEMP_WARNINGS_PREFERENCE = "NUM_TEMP_WARNINGS_PREFERENCE";

        public static final List<String> WARNING_ENABLED_PREFERENCES = Arrays.asList(Preferences.ENABLED_PREFERENCE,
                Preferences.BATTERY_FULL_WARNING_ENABLED_PREFERENCE, Preferences.TEMPERATURE_WARNING_ENABLED_PREFERENCE);
    }
    public static class Ads {

        public static final int NUM_ADS_BEFORE_SHOWING_BANNER_WARNING_PAGE = 3;
        public static final int NUM_ADS_BEFORE_SHOWING_BANNER_STATISTICS_PAGE = 3;
        public static final int NUM_ADS_BEFORE_SHOWING_BANNER_RECOMMENDATION_PAGE = 3;
        public static final int NUM_ADS_BEFORE_SHOWING_BANNER_MAIN_PAGE = 3;
        public static final int NUM_ADS_BEFORE_SHOWING_INSTERSTITIAL_AD = 5;
        public static final String INTERSTITIAL_TEST_AD = "ca-app-pub-3940256099942544/1033173712";
        public static String KENNETH_DEVICE_ID = "ED2945FACAF215856924BB761567E387";
        public static String MARTINA_DEVICE_ID = "788FEE3C950901E00262FF10DF430562";
        public static String CHRISTINA_DEVICE_ID = "7280FBE4435D7F26C5512420F72241E3";
    }
}
