package com.nineworldsdeep.gauntlet.core;

import android.app.Activity;
import android.content.Intent;

import com.nineworldsdeep.gauntlet.synergy.v3.SynergyV3MainActivity;

/**
 * Created by brent on 8/5/16.
 */
public class NavigateActivityCommand {

    private String mCommandText;
    private Class mActivityClass;
    private Activity mParent;

    public NavigateActivityCommand(String commandText, Class activityClass, Activity parent) {

        mCommandText = commandText;
        mActivityClass = activityClass;
        mParent = parent;
    }

    public static void navigateTo(Class activityClass, Activity parent){

        parent.startActivity(new Intent(parent, activityClass));
    }

    public void navigate(){

        mParent.startActivity(new Intent(mParent, mActivityClass));
    }

    @Override
    public String toString(){

        return mCommandText;
    }
}
