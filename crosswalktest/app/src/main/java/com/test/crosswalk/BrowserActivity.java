package com.test.crosswalk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import org.xwalk.core.XWalkView;

public class BrowserActivity extends AppCompatActivity
{

    private XWalkView mBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.browser_main);

        mBrowser = (XWalkView) findViewById(R.id.browser);
        mBrowser.load("https://www.facebook.com/", null);

        Button close = (Button) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

//    @Override
//    protected void onDestroy()
//    {
//        if (mBrowser != null)
//        {
//            mBrowser.clearCache(true);
//            mBrowser.pauseTimers();
//
//            mBrowser.stopLoading();
//            mBrowser.getNavigationHistory().clear();
//
//            mBrowser.onDestroy();
//        }
//
//        super.onDestroy();
//    }

}
