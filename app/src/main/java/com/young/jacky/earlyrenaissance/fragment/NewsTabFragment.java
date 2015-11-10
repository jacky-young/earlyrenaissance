package com.young.jacky.earlyrenaissance.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.young.jacky.earlyrenaissance.R;

/**
 * Created by jack on 11/9/15.
 */
public class NewsTabFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newsLayout = inflater.inflate(R.layout.fragment_news_layout, container, false);
        return newsLayout;
    }
}
