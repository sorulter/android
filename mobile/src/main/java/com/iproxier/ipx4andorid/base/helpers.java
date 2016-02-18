package com.iproxier.ipx4andorid.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

public class helpers {
    public static String TAG = "IPX";

    /**
     * Show the package version name.
     * @param context
     * @return String
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            Log.e(TAG, "null package manager is impossible");
            return null;
        }

        try {
            return packageManager.getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "package not found is impossible", e);
            return null;
        }
    }
}
