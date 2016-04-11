package com.test.crosswalk;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivityFragment extends Fragment
{

    public MainActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        Button button = (Button) view.findViewById(R.id.launch_browser);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), BrowserActivity.class));
            }
        });
    }

}
