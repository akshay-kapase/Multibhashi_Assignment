package com.assign.demo.multibhashi_assignment.Network;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Mj on 22-04-2018.
 */

public class NetworkUtils {

    public static boolean GetNetworkStatus(Context ctx){
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}
