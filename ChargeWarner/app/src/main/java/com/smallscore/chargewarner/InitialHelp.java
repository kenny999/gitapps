package com.smallscore.chargewarner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

/**
 * Created by kenneth on 2015-08-19.
 */
public class InitialHelp {

    private String INITIAL_HELP_PREFIX = "initial_help_";
    private Activity mActivity;

    public InitialHelp(Activity context) {
        mActivity = context;
    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            pi = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    public void show() {
        PackageInfo versionInfo = getPackageInfo();

        // the key changes every time you increment the version number in the AndroidManifest.xml
        final String initialHelpKey = INITIAL_HELP_PREFIX + versionInfo.versionCode;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        boolean hasBeenShown = prefs.getBoolean(initialHelpKey, false);
        if(hasBeenShown == false){

            String message = mActivity.getString(R.string.help_text_short);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                    .setMessage(message)
                    .setPositiveButton(R.string.ok_button, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Mark this version as read.
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(initialHelpKey, true);
                            editor.commit();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel_button, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mActivity.finish();
                        }

                    });
            builder.create().show();
        }
    }


}
