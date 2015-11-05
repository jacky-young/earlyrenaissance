package com.young.jacky.earlyrenaissance.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.young.jacky.earlyrenaissance.R;
import com.young.jacky.earlyrenaissance.ui.MainActivity;
import com.young.jacky.earlyrenaissance.ui.UltraPullToRefreshDemo;

/**
 * Created by jack on 11/4/15.
 */
public class AppUtils {

    public static void init(final AppCompatActivity activity) {
        initActionBar(activity);
        initUrlLinks(activity);
    }

    private static void initActionBar (final AppCompatActivity activity) {
        if (activity == null) {
            return;
        }
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;
        if (activity instanceof MainActivity) {
            actionBar.setLogo(R.drawable.ic_launcher);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO);
        } else {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
        }
    }

    private static void initUrlLinks (final Activity activity) {
        if (activity == null) {
            return;
        }

        Button link = (Button) activity.findViewById(R.id.links_btn);
        final String[] result = getText(activity);
        if (result == null) {
            return;
        }

        Log.d("DEMO", result[1]);
        Spanned txt = Html.fromHtml(result[1]);
        Log.d("DEMO", txt+"");
        link.setText(txt);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlOpen(activity, result[0]);
            }
        });
    }

    private static String[] getText(Activity activity) {
        if (activity == null) {
            return null;
        }
        int prefixSrcId = R.string.description, contentSrcId;
        String url = null;

        Class<?> cls = activity.getClass();
        if (cls == UltraPullToRefreshDemo.class) {
            contentSrcId = R.string.des_ultra_pull_to_refresh;
            url = "https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh";
        } else {
            prefixSrcId = R.string.prefix;
            contentSrcId = R.string.des_default;
            url = "http://www.trinea.cn";
        }
        String[] result = new String[] {url, getUrlInfo(activity.getString(prefixSrcId),
                url, activity.getString(contentSrcId))};
        return result;
    }

    private static String getUrlInfo(String prefix, String url, String content) {
        return new StringBuilder().append(prefix).append("<a href=\"").append(url).append("\">").append(content)
                .append("</a>").toString();
    }

    private static void urlOpen(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
