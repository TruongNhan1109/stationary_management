package com.ttn.stationarymanagement.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.VaiTro;
import com.ttn.stationarymanagement.presentation.adapter.RoleAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;
import com.ttn.stationarymanagement.presentation.dialog_fragment.AddRoleDialog;
import com.ttn.stationarymanagement.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RoleManagerFragment extends BaseFragment {

    @BindView(R.id.tv_fragment_role_manager_notify_empty)
    TextView tvNotifyEmpty;

    @BindView(R.id.rv_fragment_role_manager_list_role)
    RecyclerView rvListRole;

    @BindView(R.id.spin_kit)
    SpinKitView spinKitView;


   private List<VaiTro> listRole;
   private RoleAdapter adapterRole;
    private CompositeDisposable compositeDisposable;

    public static RoleManagerFragment newInstance() {
        Bundle args = new Bundle();
        RoleManagerFragment fragment = new RoleManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_role_manager, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        setControls();
        getDataAndShowView();
        setEvents();
    }

    private void setEvents() {

        adapterRole.setListener(new RoleAdapter.RoleAdapterListener() {
            @Override
            public void onItemClick(int position) {
                String role = listRole.get(position).getTenVaiTro();

                AddRoleDialog addRoleDialog = AddRoleDialog.newInstance(role);
                addRoleDialog.setListener(new AddRoleDialog.AddRoleDialogListener() {
                    @Override
                    public void onAddSuccesstion() {

                    }

                    @Override
                    public void onUploadSuccesstionn(String change) {

                        VaiTro vaitro = listRole.get(position);
                        vaitro.setTenVaiTro(change);

                        Observable<Boolean> obUpload = Observable.create(r -> {
                            try {

                                r.onNext( WorkWithDb.getInstance().update(vaitro));
                                r.onComplete();

                            } catch (Exception e) {
                                e.printStackTrace();
                                r.onError(e);
                            }

                        });

                        compositeDisposable.add(obUpload.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
                            if (aBoolean) {
                                CustomToast.showToastSuccesstion(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT);
                                adapterRole.notifyItemChanged(position);
                            } else {
                                CustomToast.showToastError(getContext(), "Cập nhật thất bai", Toast.LENGTH_SHORT);
                            }

                        }, throwable -> {
                            CustomToast.showToastError(getContext(), "Cập nhật thất bai", Toast.LENGTH_SHORT);
                        }));
                    }
                });

                addRoleDialog.show(getChildFragmentManager(), AddRoleDialog.TAG);

            }

            @Override
            public void onRemoveButtonClick(int position) {

                Observable<Boolean> obRemove = Observable.create(r -> {
                  try {
                    r.onNext( WorkWithDb.getInstance().delete(listRole.get(position)));
                    r.onComplete();

                  } catch (Exception e) {
                      e.printStackTrace();
                      r.onError(e);
                  }

                });

                compositeDisposable.add(obRemove.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {

                    if (aBoolean) {
                        listRole.remove(position);
                        CustomToast.showToastSuccesstion(getContext(), "Đã xóa", Toast.LENGTH_SHORT);
                        adapterRole.notifyItemRemoved(position);
                        adapterRole.notifyItemRangeChanged(position, listRole.size());
                    } else {
                        CustomToast.showToastError(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT);
                    }
                }, throwable -> {
                    CustomToast.showToastError(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT);
                }));
            }
        });

    }

    private void getDataAndShowView() {

        spinKitView.setVisibility(View.VISIBLE);
        compositeDisposable.add(obGetAllRole().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
                    spinKitView.setVisibility(View.GONE);

                    if (listRole.size() > 0) {

                        tvNotifyEmpty.setVisibility(View.GONE);
                        rvListRole.setVisibility(View.VISIBLE);

                        adapterRole.notifyDataSetChanged();

                    } else {

                        tvNotifyEmpty.setVisibility(View.VISIBLE);
                        rvListRole.setVisibility(View.GONE);

                    }
                }, throwable -> {
                    spinKitView.setVisibility(View.GONE);

                }));

    }

    private Observable<Boolean> obGetAllRole() {
        return Observable.create(r -> {
            try {
                listRole.clear();
                listRole.addAll(WorkWithDb.getInstance().getAllRole());
                r.onNext(true);
                r.onComplete();
            } catch (Exception e) {
                r.onError(e);
            }
        });
    }

    private void setControls() {

        compositeDisposable = new CompositeDisposable();
        listRole = new ArrayList<>();
        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapterRole = new RoleAdapter(getContext(), listRole);
        rvListRole.setLayoutManager(linearLayoutManager);
        rvListRole.setAdapter(adapterRole);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_role, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_role_add:

                AddRoleDialog addRoleDialog = AddRoleDialog.newInstance("");
                addRoleDialog.setListener(new AddRoleDialog.AddRoleDialogListener() {
                    @Override
                    public void onAddSuccesstion() {
                        getDataAndShowView();
                    }

                    @Override
                    public void onUploadSuccesstionn(String change) {

                    }
                });

                addRoleDialog.show(getChildFragmentManager(), AddRoleDialog.TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
