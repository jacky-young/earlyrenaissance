package com.young.jacky.earlyrenaissance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.young.jacky.earlyrenaissance.R;
import com.young.jacky.earlyrenaissance.utils.AppEntry;

import java.util.List;

/**
 * Created by jack on 11/13/15.
 */
public class AppListAdapter extends ArrayAdapter<AppEntry> {
    private LayoutInflater inflater;

    public AppListAdapter(Context ctx) {
        super(ctx, android.R.layout.simple_list_item_2);
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_icon_text, parent, false);
        } else {
            view = convertView;
        }

        AppEntry data = getItem(position);
        ((ImageView) view.findViewById(R.id.iv_app)).setImageDrawable(data.getIcon());
        ((TextView) view.findViewById(R.id.tv_app)).setText(data.getLabel());

        return view;
    }

    public void setData(List<AppEntry> data) {
        clear();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                add(data.get(i));
            }
        }
    }

}
