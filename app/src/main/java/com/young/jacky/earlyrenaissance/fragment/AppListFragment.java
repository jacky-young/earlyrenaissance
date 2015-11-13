package com.young.jacky.earlyrenaissance.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.app.ListFragment;

import com.young.jacky.earlyrenaissance.utils.AppEntry;

import java.util.List;

/**
 * Created by jack on 11/13/15.
 */
public class AppListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<AppEntry>> {

    public static final int LOADER_ID = 1;

    @Override
    public Loader<List<AppEntry>> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<AppEntry>> listLoader, List<AppEntry> appEntries) {

    }

    @Override
    public void onLoaderReset(Loader<List<AppEntry>> listLoader) {

    }

}
