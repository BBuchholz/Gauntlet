package com.nineworldsdeep.gauntlet.core;

/**
 * Created by brent on 10/8/16.
 */

public abstract class AsyncCommand {

    protected IStatusActivity statusActivity;
    protected String commandText;

    public AsyncCommand(IStatusActivity statusActivity, String commandText){
        this.statusActivity = statusActivity;
        this.commandText = commandText;
    }

    @Override
    public String toString(){

        return this.commandText;
    }

    public abstract void executeCommand();
}
