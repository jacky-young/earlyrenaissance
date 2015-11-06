package com.young.jacky.earlyrenaissance.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.young.jacky.earlyrenaissance.R;
import com.young.jacky.earlyrenaissance.utils.AppUtils;

public class BaseActivity extends AppCompatActivity {

    protected Context context;

    protected void onCreate(Bundle savedInstanceState, int layoutResID) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);

        context = getApplicationContext();
        AppUtils.init(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.github) {
            AppUtils.urlOpen(context, "https://github.com/jacky-young/earlyrenaissance");
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }
}
