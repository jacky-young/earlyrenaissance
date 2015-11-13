package com.young.jacky.earlyrenaissance.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by jack on 11/13/15.
 */
public class AppListLoader extends AsyncTaskLoader<List<AppEntry>> {
    final PackageManager pm;

    public AppListLoader(Context ctx) {
        super(ctx);
        pm = getContext().getPackageManager();
    }

    @Override
    public List<AppEntry> loadInBackground() {
        return null;
    }


}
