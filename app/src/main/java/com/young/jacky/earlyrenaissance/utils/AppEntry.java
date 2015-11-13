package com.young.jacky.earlyrenaissance.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by jack on 11/13/15.
 */
public class AppEntry {

    private final AppListLoader mLoader;
    private final ApplicationInfo mInfo;
    private final File mAPKFile;
    private String mLabel;
    private Drawable mIcon;
    private Boolean mMounted;

    public AppEntry(AppListLoader loader, ApplicationInfo info) {
        mLoader = loader;
        mInfo = info;
        mAPKFile = new File(info.sourceDir);
    }

    public ApplicationInfo getInfo() {
        return mInfo;
    }

    public String getLabel() {
        return mLabel;
    }

    public Drawable getIcon() {
        if (mIcon == null) {
            if (mAPKFile.exists()) {
                mIcon = mInfo.loadIcon(mLoader.pm);
                return mIcon;
            } else {
                mMounted = false;
            }
        } else if (!mMounted) {
            if (mAPKFile.exists()) {
                mMounted = true;
                mIcon = mInfo.loadIcon(mLoader.pm);
                return mIcon;
            }
        } else {
            return mIcon;
        }
        return mLoader.getContext().getResources().getDrawable(android.R.drawable.sym_def_app_icon);
    }

    @Override
    public String toString() {
        return mLabel;
    }

    public void loadLabel(Context context) {
        if (mLabel == null || !mMounted) {
            if (!mAPKFile.exists()) {
                mMounted = false;
                mLabel = mInfo.packageName;
            } else {
                mMounted = true;
                CharSequence label = mInfo.loadLabel(context.getPackageManager());
                mLabel = label != null ? label.toString() : mInfo.packageName;
            }
        }
    }
}
