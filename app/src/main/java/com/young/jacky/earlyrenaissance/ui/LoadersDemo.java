package com.young.jacky.earlyrenaissance.ui;

import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.young.jacky.earlyrenaissance.R;
import com.young.jacky.earlyrenaissance.fragment.AppListFragment;
import com.young.jacky.earlyrenaissance.utils.AppEntry;
import com.young.jacky.earlyrenaissance.utils.AppUtils;

/**
 * Created by jack on 11/13/15.
 */
public class LoadersDemo extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_loaders_content_layout);

        FragmentManager manager = getFragmentManager();
        if (manager.findFragmentById(R.id.fg_content) == null) {
            AppListFragment list = new AppListFragment();
            manager.beginTransaction().add(R.id.fg_content, list).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_loaders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_language) {

            return true;
        }

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

    private void configureLocale() {
        Loader<AppEntry> loader = getLoaderManager().getLoader(AppListFragment.LOADER_ID);
        if (loader != null) {
            startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
        }
    }
}
