package com.nineworldsdeep.gauntlet.core;

import android.app.Activity;
import android.content.Intent;

import com.nineworldsdeep.gauntlet.synergy.v3.SynergyV3MainActivity;

import java.util.HashMap;

/**
 * Created by brent on 8/5/16.
 */
public class NavigateActivityCommand {

    private String mCommandText;
    private Class mActivityClass;
    private Activity mParent;
    private HashMap<String, String> mExtraKeyToValue;

    public NavigateActivityCommand(
            String commandText,
            HashMap<String,String> extraKeyToValue,
            Class activityClass,
            Activity parent) {

        mCommandText = commandText;
        mActivityClass = activityClass;
        mParent = parent;
        mExtraKeyToValue = extraKeyToValue;
    }

    public NavigateActivityCommand(String commandText, Class activityClass, Activity parent){

        this(commandText, null, activityClass, parent);
    }

    public static void navigateTo(Class activityClass, Activity parent){

        navigateTo(null, activityClass, parent);
    }

    public static void navigateTo(HashMap<String, String> extrasKeyToValue, Class activityClass, Activity parent){

        Intent intent = new Intent(parent, activityClass);

        if(extrasKeyToValue != null){

            for(String key : extrasKeyToValue.keySet()){

                intent.putExtra(key, extrasKeyToValue.get(key));
            }
        }

        parent.startActivity(intent);
    }

    public void navigate(){
//asdf; //refactor to use static method pass local variables
//        Intent intent = new Intent(mParent, mActivityClass);
//
//        if(mExtraKeyToValue != null){
//
//            for(String key : mExtraKeyToValue.keySet()){
//
//                intent.putExtra(key, mExtraKeyToValue.get(key));
//            }
//        }
//
//        mParent.startActivity(intent);

        navigateTo(mExtraKeyToValue, mActivityClass, mParent);
    }

    @Override
    public String toString(){

        return mCommandText;
    }
}
