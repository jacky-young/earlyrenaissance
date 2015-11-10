package com.young.jacky.earlyrenaissance.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.young.jacky.earlyrenaissance.R;
import com.young.jacky.earlyrenaissance.fragment.ContactTabFragment;
import com.young.jacky.earlyrenaissance.fragment.MessageTabFragment;
import com.young.jacky.earlyrenaissance.fragment.NewsTabFragment;
import com.young.jacky.earlyrenaissance.fragment.SettingTabFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jack on 11/9/15.
 */
public class TabHostFragmentDemo extends BaseActivity {
    private Fragment messageFragment;
    private Fragment contactFragment;
    private Fragment newsFragment;
    private Fragment settingFragment;
    private FragmentManager fragmentManager;
    private static int currentIndex;

    @Bind(R.id.message_btn)
    Button messageBtn;
    @Bind(R.id.contact_btn)
    Button contactBtn;
    @Bind(R.id.news_btn)
    Button newsBtn;
    @Bind(R.id.setting_btn)
    Button settingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_tab_host_fragment_layout);

        ButterKnife.bind(this);
        fragmentManager = getFragmentManager();
        setTabSelection(0);
    }

    private void setTabSelection(int index) {
        currentIndex = index;
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                setHighlighted(true);
                if (messageFragment == null){
                    messageFragment = new MessageTabFragment();
                    transaction.add(R.id.fg_content, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                break;
            case 1:
                setHighlighted(true);
                if (contactFragment == null){
                    contactFragment = new ContactTabFragment();
                    transaction.add(R.id.fg_content, contactFragment);
                } else {
                    transaction.show(contactFragment);
                }
                break;
            case 2:
                setHighlighted(true);
                if (newsFragment == null) {
                    newsFragment = new NewsTabFragment();
                    transaction.add(R.id.fg_content, newsFragment);
                } else {
                    transaction.show(newsFragment);
                }
                break;
            case 3:
            default:
                setHighlighted(true);
                if (settingFragment == null) {
                    settingFragment = new SettingTabFragment();
                    transaction.add(R.id.fg_content, settingFragment);
                } else {
                    transaction.show(settingFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void clearSelection() {
        messageBtn.setBackgroundColor(Color.argb(0, 0, 0, 0));
        contactBtn.setBackgroundColor(0x00000000);
        newsBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        settingBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void setHighlighted(boolean highlight) {
        Button view = currentIndex == 0 ? messageBtn : (currentIndex == 1 ? contactBtn :
                (currentIndex == 2 ? newsBtn : settingBtn));
        view.setBackgroundColor(highlight
                                ? getResources().getColor(android.R.color.background_dark)
                                : getResources().getColor(android.R.color.transparent));
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (messageFragment != null){
            transaction.hide(messageFragment);
        }
        if (contactFragment != null){
            transaction.hide(contactFragment);
        }
        if (newsFragment != null){
            transaction.hide(newsFragment);
        }
        if (settingFragment != null){
            transaction.hide(settingFragment);
        }
    }

    @OnClick(R.id.message_btn)
    public void message() {
        setTabSelection(0);
    }

    @OnClick(R.id.contact_btn)
    public void contact() {
        setTabSelection(1);
    }

    @OnClick(R.id.news_btn)
    public void news() {
        setTabSelection(2);
    }

    @OnClick(R.id.setting_btn)
    public void setting() {
        setTabSelection(3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
