package com.nineworldsdeep.gauntlet.core;

import com.nineworldsdeep.gauntlet.core.IStatusEnabledActivity;

/**
 * Created by brent on 10/8/16.
 */

public abstract class AsyncCommand {

    protected IStatusEnabledActivity statusActivity;
    protected String commandText;

    public AsyncCommand(IStatusEnabledActivity statusActivity, String commandText){
        this.statusActivity = statusActivity;
        this.commandText = commandText;
    }

    @Override
    public String toString(){

        return this.commandText;
    }

    public abstract void executeCommand();
}
