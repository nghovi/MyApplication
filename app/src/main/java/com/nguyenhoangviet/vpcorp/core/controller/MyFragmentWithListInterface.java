package com.nguyenhoangviet.vpcorp.core.controller;

import android.widget.AdapterView;

/**
 * Created by vpcorp on 05/12/2015.
 */
public interface MyFragmentWithListInterface extends AdapterView.OnItemClickListener {

    public int getListViewId();
    public void initAdapter();
    public void onEmptyData();
    public void onNotEmptyData();
}
