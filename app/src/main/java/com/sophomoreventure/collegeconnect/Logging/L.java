package com.sophomoreventure.collegeconnect.Logging;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Vikas Kumar on 02-01-2016.
 */
public class L {
    public static void m(String message) {
        Log.d("VIVZ", "" + message);
    }

    public static void t(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
    }
    public static void T(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_LONG).show();
    }

    public static void T(RelativeLayout mRoot, String message) {
        Snackbar.make(mRoot, message, Snackbar.LENGTH_SHORT).show();
    }
}
