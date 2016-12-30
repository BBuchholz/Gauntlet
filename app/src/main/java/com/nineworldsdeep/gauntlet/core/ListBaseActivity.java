package com.nineworldsdeep.gauntlet.core;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public abstract class ListBaseActivity extends DbBaseActivity {

    // http://stackoverflow.com/questions/3014089/maintain-save-restore-scroll-position-when-returning-to-a-listview
    protected static final String LIST_STATE = "listState";
    protected Parcelable mListState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        mListState = getListView().onSaveInstanceState();
        state.putParcelable(LIST_STATE, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mListState = state.getParcelable(LIST_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout();

//        if (mListState != null)
//            getListView().onRestoreInstanceState(mListState);
//        mListState = null;

        restoreInstanceState();
    }

    protected void storeInstanceState(){

        mListState = getListView().onSaveInstanceState();
    }

    protected void restoreInstanceState(){

        if (mListState != null)
            getListView().onRestoreInstanceState(mListState);
        mListState = null;
    }

    protected void refreshLayout(){

        ListView lvItems = getListView();

        // http://stackoverflow.com/a/8276140/670768
        //sync position info
        int index = lvItems.getFirstVisiblePosition();
        View v = lvItems.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();

        //perform adapter operations
        readItems(getListView());
        setupListViewListener(getListView());
        registerForContextMenu(getListView());

        //restore listview postion
        lvItems.setSelectionFromTop(index, top);
    }

    protected abstract ListView getListView();
    protected abstract void readItems(ListView lv);
    protected abstract void setupListViewListener(ListView lv);
}
