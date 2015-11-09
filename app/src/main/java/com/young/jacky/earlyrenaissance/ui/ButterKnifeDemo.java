package com.young.jacky.earlyrenaissance.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.young.jacky.earlyrenaissance.R;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jack on 11/9/15.
 */
public class ButterKnifeDemo extends BaseActivity {

    @Bind(R.id.tv_bk)
    TextView tvBk;
    @Bind(R.id.et_bk)
    EditText etBk;
    @Bind(R.id.btn_bk)
    Button btnBk;

    @BindString(R.string.title_activity_base) String title;
    @BindDimen(R.dimen.dp_40) int size;
    @BindDrawable(R.drawable.github) Drawable icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_buffer_knife_layout);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_bk)
    public void submit() {
        //
    }

    @OnClick(R.id.et_bk)
    public void setET(EditText editText) {
        editText.setText("HAHA");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
