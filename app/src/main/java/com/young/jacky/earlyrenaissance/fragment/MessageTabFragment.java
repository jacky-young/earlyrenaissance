package com.young.jacky.earlyrenaissance.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.young.jacky.earlyrenaissance.R;

/**
 * Created by jack on 11/9/15.
 */
public class MessageTabFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_message_layout, container, false);
        return messageLayout;
    }
}
