package com.example.vietnguyen.core.controllers;

import android.widget.AdapterView;

import com.example.vietnguyen.models.MyModel;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.NoteListAdapter;

import java.util.List;

/**
 * Created by vietnguyen on 05/12/2015.
 */
public interface MyFragmentWithListInterface extends AdapterView.OnItemClickListener{

    public int getListViewId();
    public void initAdapter();
}
