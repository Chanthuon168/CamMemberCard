package com.hammersmith.cammembercard;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.hammersmith.cammembercard.adapter.ModelAdapter;
import com.hammersmith.cammembercard.model.Model;

import java.util.ArrayList;

public class CheckBoxActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CheckBox chk_select_all;
    private Button btn_delete_all;
    private TextView edit;
    private boolean isEdit = true;

    private ArrayList<Model> item_list = new ArrayList<>();
    private ModelAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_box);

        initControls();
    }

    private void initControls() {


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        chk_select_all = (CheckBox) findViewById(R.id.chk_select_all);
        btn_delete_all = (Button) findViewById(R.id.btn_delete_all);
        edit = (TextView) findViewById(R.id.edit);
        chk_select_all.setVisibility(View.GONE);
        btn_delete_all.setVisibility(View.GONE);

        item_list.add(new Model("Alpha", false));
        item_list.add(new Model("Beta", false));
        item_list.add(new Model("Cup Cake", false));
        item_list.add(new Model("Donut", false));
        item_list.add(new Model("Eclair", false));
        item_list.add(new Model("Froyo", false));
        item_list.add(new Model("Ginger Bread", false));
        item_list.add(new Model("Honycomb", false));
        item_list.add(new Model("Icecream Sandwhich", false));
        item_list.add(new Model("Jelly Bean", false));
        item_list.add(new Model("Kitkat", false));
        item_list.add(new Model("Lolly Pop", false));
        item_list.add(new Model("Marsh Mallow", false));
        item_list.add(new Model("Nougat", false));


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ModelAdapter(item_list);
        recyclerView.setAdapter(mAdapter);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    chk_select_all.setVisibility(View.VISIBLE);
                    btn_delete_all.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.GONE);
                    isEdit = false;
                } else {
                    chk_select_all.setVisibility(View.GONE);
                    btn_delete_all.setVisibility(View.GONE);
                    edit.setVisibility(View.VISIBLE);
                }
            }
        });

        chk_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chk_select_all.isChecked()) {

                    for (Model model : item_list) {
                        model.setSelected(true);
                    }
                } else {

                    for (Model model : item_list) {
                        model.setSelected(false);
                    }
                }

                mAdapter.notifyDataSetChanged();
            }
        });

        btn_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chk_select_all.isChecked()) {
                    item_list.clear();
                    mAdapter.notifyDataSetChanged();
                    chk_select_all.setChecked(false);
                } else {
                    Snackbar.make(v, "Please click on select all check box, to delete all items.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }
}