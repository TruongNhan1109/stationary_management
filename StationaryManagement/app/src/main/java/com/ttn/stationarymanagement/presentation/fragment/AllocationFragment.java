package com.ttn.stationarymanagement.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.CapPhat;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;
import com.ttn.stationarymanagement.presentation.activity.AllocationActivity;
import com.ttn.stationarymanagement.presentation.adapter.GroupBillAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;
import com.ttn.stationarymanagement.presentation.model.GroupBillModel;
import com.ttn.stationarymanagement.presentation.model.GroupProductModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AllocationFragment extends BaseFragment {

    @BindView(R.id.fab)
    FloatingActionButton fbAdd;

    @BindView(R.id.lnl_fragment_allocation_notify_empty)
    LinearLayout lnlNotifyEmplty;

    @BindView(R.id.rv_fragment_allocation_list_bill)
    RecyclerView rvListBill;

    private GroupBillAdapter adapterGroupBill;
    private List<GroupBillModel> listGroupBill;
    private CompositeDisposable compositeDisposable;

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
        setHasOptionsMenu(true);

        setControls();
        getAllBills();
        setEvents();
    }

    private void getAllBills() {

        getBills().subscribeOn(Schedulers.newThread()).flatMap(capPhats -> {

            Map<String, List<CapPhat>> listGroup = new HashMap<>();
            for(CapPhat bill: capPhats) {
                String date = bill.getNgayCap();
                if (listGroup.get(date) == null) {
                    List<CapPhat> list = new ArrayList<>();
                    list.add(bill);
                    listGroup.put(date, list);

                } else {
                    List<CapPhat> list = listGroup.get(date);
                    list.add(bill);
                    listGroup.put(date, list);
                }
            }

            List<GroupBillModel> listGroupResult = new ArrayList<>();

            for(Map.Entry<String, List<CapPhat>> entry: listGroup.entrySet()) {
                GroupBillModel groupBillModel = new GroupBillModel();
                groupBillModel.setNameGroup(entry.getKey());
                groupBillModel.setListBills(entry.getValue());
                listGroupResult.add(groupBillModel);
            }

            return Observable.just(listGroupResult);

        }).observeOn(AndroidSchedulers.mainThread()).subscribe(groupBillModels -> {
            listGroupBill.clear();

            if (groupBillModels.size()> 0) {
                rvListBill.setVisibility(View.VISIBLE);
                lnlNotifyEmplty.setVisibility(View.GONE);

                listGroupBill.addAll(groupBillModels);
                adapterGroupBill.notifyDataSetChanged();

            } else {

                rvListBill.setVisibility(View.GONE);
                lnlNotifyEmplty.setVisibility(View.VISIBLE);

            }

        });
    }

    private Observable<List<CapPhat>> getBills() {
        return Observable.create(r -> {
            List<CapPhat> list = WorkWithDb.getInstance().getAllAllocation();
            r.onNext(list);
            r.onComplete();
        });
    }

    private void setControls() {
        compositeDisposable = new CompositeDisposable();

        listGroupBill = new ArrayList<>();
        adapterGroupBill = new GroupBillAdapter(getContext(), listGroupBill);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListBill.setLayoutManager(linearLayoutManager);
        rvListBill.setAdapter(adapterGroupBill);

    }

    private void setEvents() {

        fbAdd.setOnClickListener(v -> {
            Intent intent = AllocationActivity.getCallingIntent(getContext());
            startActivity(intent);

        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_allocation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
