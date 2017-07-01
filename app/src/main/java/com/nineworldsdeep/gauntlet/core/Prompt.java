package com.nineworldsdeep.gauntlet.core;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.sqlite.NwdDb;

/**
 * Created by brent on 7/1/17.
 */

public class Prompt {

    public static void promptSetLocalDeviceDescription(
            final Context context, final IRefreshableUI gui){

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt, null);

        TextView tv = (TextView) promptsView.findViewById(R.id.textView1);
        tv.setText("Enter Local Device Name: ");

        android.app.AlertDialog.Builder alertDialogBuilder =
                new android.app.AlertDialog.Builder(context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                // get list name from userInput and move
                                String deviceName =
                                        userInput.getText().toString();

                                deviceName = deviceName.trim().toLowerCase();

                                if(!Utils.stringIsNullOrWhitespace(deviceName)) {

                                    Configuration.ensureLocalMediaDevice(
                                            context,
                                            NwdDb.getInstance(context),
                                            deviceName);

                                    String msg = "stored device '" + deviceName + "'";
                                    //String msg = deviceName + " entered (test)";
                                    Utils.toast(context, msg);

                                    gui.refreshLayout();

                                }else{

                                    Utils.toast(context, "empty name, ignored.");
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();

                                gui.refreshLayout();
                            }
                        });

        // create alert dialog
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
