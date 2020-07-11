package com.ttn.stationarymanagement.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.NhanVien;
import com.ttn.stationarymanagement.presentation.activity.AddStaftActivity;
import com.ttn.stationarymanagement.presentation.adapter.StaftManagerAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StaftManagerFragment extends BaseFragment {

    @BindView(R.id.tv_fragment_staft_manager_notify_empty)
    TextView tvNotifyEmpty;

    @BindView(R.id.rv_fragment_staft_manager_list_staft)
    RecyclerView rvListStaft;

    @BindView(R.id.tv_fragment_staft_manager_total_staft)
    TextView tvTotalStaft;

    private StaftManagerAdapter adapter;
    private List<NhanVien>  listNhanVien;
    private CompositeDisposable compositeDisposable;

    public static StaftManagerFragment newInstance() {
        Bundle args = new Bundle();
        StaftManagerFragment fragment = new StaftManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_staft_manager, container, false);
       ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        initControl();
        getDataAndShow();
        setEvents();


    }

    private void setEvents() {

        adapter.setListener(new StaftManagerAdapter.StaftManagerAdapterListener() {
            @Override
            public void onItemClick(int position) {
                NhanVien nhanVien = listNhanVien.get(position);

                Intent intent = AddStaftActivity.getCallingIntent(getContext());
                intent.putExtra("ID_STAFT", nhanVien.getMaNV());
                startActivityForResult(intent, AddStaftActivity.REQUEST_EDIT_STAFT);

            }

            @Override
            public void onRemoveClick(int position) {

            }
        });


    }

    private void getDataAndShow() {

        Observable<List<NhanVien>> getDataStaft = Observable.create(r -> {

            List<NhanVien> list = WorkWithDb.getInstance().getAllStaft();
            r.onNext(list);
            r.onComplete();

        });

        compositeDisposable.add(getDataStaft.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(nhanViens -> {

                    if (nhanViens.size() > 0) {

                        rvListStaft.setVisibility(View.VISIBLE);
                        tvNotifyEmpty.setVisibility(View.GONE);
                        tvTotalStaft.setVisibility(View.VISIBLE);
                        tvTotalStaft.setText("Danh sách nhân viên (" + nhanViens.size() + ")");
                        listNhanVien.clear();
                        listNhanVien.addAll(nhanViens);
                        adapter.notifyDataSetChanged();

                    } else {
                        tvTotalStaft.setVisibility(View.GONE);
                        rvListStaft.setVisibility(View.GONE);
                        tvNotifyEmpty.setVisibility(View.VISIBLE);
                    }

                }));

    }


    private void initControl() {

        compositeDisposable = new CompositeDisposable();

        listNhanVien = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapter = new StaftManagerAdapter(getContext(), listNhanVien);
        rvListStaft.setLayoutManager(linearLayoutManager);
        rvListStaft.setAdapter(adapter);


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_staft_manager, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.mn_staft_manager_add:

                Intent intent = AddStaftActivity.getCallingIntent(getContext());
                startActivityForResult(intent, AddStaftActivity.REQUEST_ADD_STAFT);

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        if (requestCode == AddStaftActivity.REQUEST_ADD_STAFT && resultCode == Activity.RESULT_OK) {
            getDataAndShow();
        }

        if (requestCode == AddStaftActivity.REQUEST_EDIT_STAFT && resultCode == Activity.RESULT_OK) {
            getDataAndShow();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }
}
