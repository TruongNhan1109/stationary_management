package com.ttn.stationarymanagement.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.activity.AllocationActivity;
import com.ttn.stationarymanagement.presentation.adapter.GroupBillAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllocationFragment extends BaseFragment {

    @BindView(R.id.fab)
    FloatingActionButton fbAdd;

    @BindView(R.id.lnl_fragment_allocation_notify_empty)
    LinearLayout lnlNotifyEmplty;

    @BindView(R.id.rv_fragment_allocation_list_bill)
    RecyclerView rvListBill;

    private GroupBillAdapter adapterGroupBill;

    public static AllocationFragment newInstance() {
        Bundle args = new Bundle();
        AllocationFragment fragment = new AllocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allocation, container, false) ;
        ButterKnife.bind(this, view);
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setEvents();
    }

    private void setEvents() {

        fbAdd.setOnClickListener(v -> {
            Intent intent = AllocationActivity.getCallingIntent(getContext());
            startActivity(intent);

        });
    }
}
