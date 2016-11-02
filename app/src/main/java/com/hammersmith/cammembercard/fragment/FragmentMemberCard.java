package com.hammersmith.cammembercard.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.adapter.AdapterMemberCard;
import com.hammersmith.cammembercard.model.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chan Thuon on 11/2/2016.
 */
public class FragmentMemberCard extends Fragment {
    private RecyclerView recyclerView;
    private AdapterMemberCard adapterMemberCard;
    private LinearLayoutManager layoutManager;
    private List<Member> members = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_card, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        adapterMemberCard = new AdapterMemberCard(getActivity(), members);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterMemberCard);
        return view;
    }
}
