package com.nineworldsdeep.gauntlet.core;

import android.app.Activity;

/**
 * Created by brent on 10/5/16.
 */

public interface IStatusResponsive {

    void updateStatus(String status);
    Activity getAsActivity();
    void onPostExecute();
}
