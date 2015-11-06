package com.young.jacky.earlyrenaissance.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.young.jacky.earlyrenaissance.R;

import java.util.Arrays;
import java.util.LinkedList;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

public class UltraPullToRefreshDemo extends BaseActivity {

    final String[] mStringList = {"Jacky", "Renaissance 1550"};
    private PtrClassicFrameLayout mPtrFrame;
    private ArrayAdapter<String> adapter;
    private LinkedList<String> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_ultra_pull_to_refresh_demo);

        ListView listView = (ListView) findViewById(R.id.prt_list_view);
        mListData = new LinkedList<String>();
        mListData.addAll(Arrays.asList(MainActivity.demoStrings));
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mListData);
        listView.setAdapter(adapter);

        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.ptr_list_view_frame);

        final StoreHouseHeader header = new StoreHouseHeader(context);
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);
        header.setTextColor(getResources().getColor(R.color.bg_gray_333333));
        header.initWithString(mStringList[0]);
        setTitle(getString(R.string.title_activity_ultra_pull_to_refresh_demo) + " " + mStringList[0]);
        mPtrFrame.addPtrUIHandler(new PtrUIHandler() {
            private int mLoadTime = 0;
            @Override
            public void onUIReset(PtrFrameLayout ptrFrameLayout) {
                mLoadTime++;
                String loadString = mStringList[mLoadTime%mStringList.length];
                header.initWithString(loadString);
            }

            @Override
            public void onUIRefreshPrepare(PtrFrameLayout ptrFrameLayout) {
                String loadString = mStringList[mLoadTime%mStringList.length];
                setTitle(getString(R.string.title_activity_ultra_pull_to_refresh_demo)+" "+loadString);
            }

            @Override
            public void onUIRefreshBegin(PtrFrameLayout ptrFrameLayout) {

            }

            @Override
            public void onUIRefreshComplete(PtrFrameLayout ptrFrameLayout) {

            }

            @Override
            public void onUIPositionChange(PtrFrameLayout ptrFrameLayout, boolean b, byte b2, PtrIndicator ptrIndicator) {

            }
        });

//        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setDurationToCloseHeader(3000);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                updateData();
            }
        });
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh(false);
            }
        }, 100);
    }

    private void updateData() {
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
//                adapter.clear();
                mListData.addAll(Arrays.asList(MainActivity.demoStrings));
                mPtrFrame.refreshComplete();
                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }

}
