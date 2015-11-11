package com.young.jacky.earlyrenaissance.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.young.jacky.earlyrenaissance.R;

import java.util.Arrays;
import java.util.LinkedList;


public class MainActivity extends BaseActivity {

    public static final String[] demoStrings = {"UltraPullToRefreshDemo", "ButterKnifeDemo",
                "TabHostFragmentDemo", "SQLiteDBDemo", "Simon Vouet", "Francois Lemoyne", "Edouard Manet"};
    private static final int total = demoStrings.length - 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);

        LinkedList<String> mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(demoStrings));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mListItems);
        ListView listView = (ListView) findViewById(R.id.simple_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == total - 6) {
                    startActivity(UltraPullToRefreshDemo.class);
                } else if (position == total - 5) {
                    startActivity(ButterKnifeDemo.class);
                } else if (position == total - 4) {
                    startActivity(TabHostFragmentDemo.class);
                } else if (position == total - 3) {
                    startActivity(SQLiteDBDemo.class);
                }
            }
        });
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

}
