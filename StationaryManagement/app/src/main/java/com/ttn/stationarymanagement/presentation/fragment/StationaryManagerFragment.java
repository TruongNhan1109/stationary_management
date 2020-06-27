package com.ttn.stationarymanagement.presentation.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.stationery.VanPhongPham;
import com.ttn.stationarymanagement.presentation.activity.HomeActivity;
import com.ttn.stationarymanagement.presentation.activity.NewProductActivity;
import com.ttn.stationarymanagement.presentation.adapter.GroupProductAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;
import com.ttn.stationarymanagement.presentation.model.GroupProductModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StationaryManagerFragment extends BaseFragment {

    @BindView(R.id.rv_fragment_stationary_manager_list_product)
    RecyclerView rvListProducts;

    @BindView(R.id.fab)
    FloatingActionButton fbAdd;

    private  GroupProductAdapter groupProductAdapter;
    private List<GroupProductModel> groupProductModels;



    public static StationaryManagerFragment newInstance() {
        Bundle args = new Bundle();
        StationaryManagerFragment fragment = new StationaryManagerFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stationary_manager, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        setControls();
        getDatas();
        setEvents();


    }

    private void setEvents() {
        fbAdd.setOnClickListener(v -> {
            Intent intent = NewProductActivity.getCallingIntent(getContext());
            startActivity(intent);

        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_pricegroup_master, menu);
    }

    private void getDatas() {

        VanPhongPham sp1 = new VanPhongPham();
        VanPhongPham sp2 = new VanPhongPham();
        VanPhongPham sp3 = new VanPhongPham();
        VanPhongPham sp4 = new VanPhongPham();

        List<VanPhongPham> list = new ArrayList<>();
        list.add(sp1);
        list.add(sp2);
        list.add(sp3);
        list.add(sp4);

        GroupProductModel group1 = new GroupProductModel("A", list);
        GroupProductModel group2 = new GroupProductModel("B", list);
        GroupProductModel group3 = new GroupProductModel("C", list);

        groupProductModels.add(group1);
        groupProductModels.add(group2);
        groupProductModels.add(group3);

        groupProductAdapter.notifyDataSetChanged();
    }

    private void setControls() {

        groupProductModels = new ArrayList<>();

        groupProductAdapter = new GroupProductAdapter(getContext(), groupProductModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListProducts.setLayoutManager(linearLayoutManager);
        rvListProducts.setAdapter(groupProductAdapter);
    }
}
