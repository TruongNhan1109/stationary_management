package com.ttn.stationarymanagement.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.PhongBan;
import com.ttn.stationarymanagement.presentation.adapter.DepartmentAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;
import com.ttn.stationarymanagement.presentation.dialog_fragment.AddDepartmentDialog;
import com.ttn.stationarymanagement.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DepartmentManagerFragment extends BaseFragment {

    @BindView(R.id.tv_fragment_role_manager_notify_empty)
    TextView tvNotifyEmpty;

    @BindView(R.id.rv_fragment_role_manager_list_role)
    RecyclerView rvListDepartment;

    private List<PhongBan> listDepartment;
    private DepartmentAdapter adapterDepartment;

    private CompositeDisposable compositeDisposable;

    public static DepartmentManagerFragment newInstance() {
        Bundle args = new Bundle();
        DepartmentManagerFragment fragment = new DepartmentManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department_manager, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        setControls();
        getDataAndSetView();
        setEvents();
    }

    private void setEvents() {

        adapterDepartment.setListenter(new DepartmentAdapter.DepartmentAdapterListener() {
            @Override
            public void onItemClick(int position) {

                AddDepartmentDialog addDepartmentDialog = AddDepartmentDialog.newInstance(listDepartment.get(position).getTenPB(), listDepartment.get(position).getGhiChu());
                addDepartmentDialog.setListener(new AddDepartmentDialog.AddDepartmentDilaogListener() {
                    @Override
                    public void onAddSuccesstion() {
                    }

                    @Override
                    public void onUpload(String department, String note) {

                        PhongBan phongBan = listDepartment.get(position);
                        phongBan.setTenPB(department);
                        phongBan.setGhiChu(note);

                        Observable<Boolean> obUpload = Observable.create(r -> {
                            try {
                                r.onNext(WorkWithDb.getInstance().update(phongBan));
                                r.onComplete();

                            } catch (Exception e) {
                                e.printStackTrace();
                               r.onError(e);
                            }
                        });

                        compositeDisposable.add(obUpload.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
                            if (aBoolean) {
                                CustomToast.showToastSuccesstion(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT);
                                adapterDepartment.notifyItemChanged(position);
                            } else {
                                CustomToast.showToastError(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT);
                            }
                        }, throwable -> {
                            CustomToast.showToastError(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT);
                        }));

                    }
                });

                addDepartmentDialog.show(getChildFragmentManager(), AddDepartmentDialog.TAG);
            }

            @Override
            public void onItemRemove(int position) {

                Observable<Boolean> obUpload = Observable.create(r -> {
                    try {
                        r.onNext(WorkWithDb.getInstance().delete(listDepartment.get(position)));
                        r.onComplete();

                    } catch (Exception e) {
                        e.printStackTrace();
                        r.onError(e);
                    }
                });

                compositeDisposable.add(obUpload.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
                    if (aBoolean) {

                        CustomToast.showToastSuccesstion(getContext(), "Xóa thành công", Toast.LENGTH_SHORT);
                        listDepartment.remove(position);
                        adapterDepartment.notifyItemRemoved(position);
                        adapterDepartment.notifyItemRangeChanged(position, listDepartment.size());
                    } else {
                        CustomToast.showToastError(getContext(), "Xóa  thất bại", Toast.LENGTH_SHORT);
                    }
                }, throwable -> {
                    CustomToast.showToastError(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT);
                }));
            }
        });


    }

    private void getDataAndSetView() {

        Observable<Boolean> obGetData = Observable.create(r -> {
            try {
                listDepartment.clear();
                listDepartment.addAll(WorkWithDb.getInstance().getAllDepartment());
                r.onNext(true);
                r.onComplete();

            } catch (Exception e) {
                e.printStackTrace();
                r.onError(e);
            }
        });

        compositeDisposable.add(obGetData.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
            if (listDepartment.size() > 0) {
                rvListDepartment.setVisibility(View.VISIBLE);
                tvNotifyEmpty.setVisibility(View.GONE);
                adapterDepartment.notifyDataSetChanged();

            } else {

                rvListDepartment.setVisibility(View.GONE);
                tvNotifyEmpty.setVisibility(View.VISIBLE);

            }
        }));

    }

    private void setControls() {

        compositeDisposable = new CompositeDisposable();

        listDepartment = new ArrayList<>();
        adapterDepartment = new DepartmentAdapter(getContext(), listDepartment);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListDepartment.setLayoutManager(linearLayoutManager);
        rvListDepartment.setAdapter(adapterDepartment);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_department, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_department_manager_add:
                AddDepartmentDialog addDepartmentDialog = AddDepartmentDialog.newInstance("", "");
                addDepartmentDialog.setListener(new AddDepartmentDialog.AddDepartmentDilaogListener() {
                    @Override
                    public void onAddSuccesstion() {
                        getDataAndSetView();
                    }

                    @Override
                    public void onUpload(String department, String note) {
                    }
                });

                addDepartmentDialog.show(getChildFragmentManager(), AddDepartmentDialog.TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
