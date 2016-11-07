package com.hammersmith.cammembercard.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hammersmith.cammembercard.R;

/**
 * Created by Chan Thuon on 11/7/2016.
 */
public class FragmentCondition extends Fragment {
    private TextView title;

    public FragmentCondition() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_condition, container, false);
        title = (TextView) view.findViewById(R.id.title);
        String first = "Brown Coffee Cambodia's membership card offers up to ";
        String middle = "<font color='#EE0000'>50%</font>";
        String end = " discount! Collect Now!";
        title.setText(Html.fromHtml(first + middle + end));
        return view;
    }
}
