package com.ttn.stationarymanagement.presentation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.ttn.stationarymanagement.presentation.activity.DetailBillActivity;
import com.ttn.stationarymanagement.presentation.adapter.GroupBillAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;
import com.ttn.stationarymanagement.presentation.dialog_fragment.SearchDialogFragment;
import com.ttn.stationarymanagement.presentation.model.GroupBillModel;
import com.ttn.stationarymanagement.presentation.model.GroupProductModel;
import com.ttn.stationarymanagement.utils.CustomToast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    private List<GroupBillModel> listGroupBill;     // Danh sách phiếu theo nhóm
    private CompositeDisposable compositeDisposable;

    public static AllocationFragment newInstance() {
        AllocationFragment fragment = new AllocationFragment();
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

    // Lấy danh sách các phiếu
    private void getAllBills() {

      compositeDisposable.add(getBills().subscribeOn(Schedulers.newThread()).flatMap(capPhats -> {

            Map<String, List<CapPhat>> listGroup = new HashMap<>();

            // Gom nhóm các phiếu theo ngày cấp
            for(CapPhat bill: capPhats) {

                String date = bill.getNgayCap();        // Tên nhóm

                if (listGroup.get(date) == null) {  // Nhóm chưa có phiếu

                    List<CapPhat> list = new ArrayList<>();
                    list.add(bill);
                    listGroup.put(date, list);

                } else {        // Nhóm đã có phiếu rồi

                    List<CapPhat> list = listGroup.get(date);
                    list.add(bill);
                    listGroup.put(date, list);
                }
            }

            // Khởi tạo danh sách quản lý các bill theo ngày
            List<GroupBillModel> listGroupResult = new ArrayList<>();

            for(Map.Entry<String, List<CapPhat>> entry: listGroup.entrySet()) {

                GroupBillModel groupBillModel = new GroupBillModel();
                groupBillModel.setNameGroup(entry.getKey());     // Ngày
                groupBillModel.setListBills(entry.getValue());  // Danh sách bill

                listGroupResult.add(groupBillModel);
            }

            return Observable.just(listGroupResult);

        }).observeOn(AndroidSchedulers.mainThread()).subscribe(groupBillModels -> {

            listGroupBill.clear();

            if (groupBillModels.size()> 0) {    // Đã có phiếu được cấp

                rvListBill.setVisibility(View.VISIBLE);
                lnlNotifyEmplty.setVisibility(View.GONE);

                listGroupBill.addAll(groupBillModels);
                adapterGroupBill.notifyDataSetChanged();

            } else {    // Thông báo rỗng

                rvListBill.setVisibility(View.GONE);
                lnlNotifyEmplty.setVisibility(View.VISIBLE);
            }

        }));
    }

    // Observable lấy danh sách phiếu
    private Observable<List<CapPhat>> getBills() {
        return Observable.create(r -> {
            List<CapPhat> list = WorkWithDb.getInstance().getAllAllocation();
            r.onNext(list);
            r.onComplete();
        });
    }

    private void setControls() {
        compositeDisposable = new CompositeDisposable();

        // Khởi tạo list và adpater
        listGroupBill = new ArrayList<>();
        adapterGroupBill = new GroupBillAdapter(getContext(), listGroupBill);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListBill.setLayoutManager(linearLayoutManager);
        rvListBill.setAdapter(adapterGroupBill);

    }

    private void setEvents() {

        // Thêm cấp phát
        fbAdd.setOnClickListener(v -> {
            Intent intent = AllocationActivity.getCallingIntent(getContext());
            startActivityForResult(intent, AllocationActivity.REQUEST_ADD_BILL);
            // Animation chuyển cảnh
            getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

        });

        adapterGroupBill.setListener(new GroupBillAdapter.OnGroupBillAdapterListener() {
            @Override
            public void onItemClick(int positionParent, int positionChild) {    // Hiển thị tri tiết bill

                CapPhat mItem = listGroupBill.get(positionParent).getListBills().get(positionChild);

                Intent intent = DetailBillActivity.getCallingIntent(getContext());
                intent.putExtra("ID_BILL", mItem.getMaPhieu());
                startActivity(intent);
                // Animation chuyển cảnh
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

            }

            @Override
            public void onButtonRemoveClick(int positionParent, int positionChild) {    // Khi xóa hóa đơn
                CapPhat mItem = listGroupBill.get(positionParent).getListBills().get(positionChild);

                Observable<Boolean> deleteBill = Observable.create(r -> {
                    r.onNext(WorkWithDb.getInstance().delete(mItem));
                    r.onComplete();
                });

                compositeDisposable.add(deleteBill.subscribeOn(Schedulers.newThread())
                .flatMap(aBoolean -> {  // Xóa hóa đơn thành công

                    if (aBoolean) {
                        // Cập nhật lại số lượng sản phẩm
                        VanPhongPham vanPhongPham = WorkWithDb.getInstance().getProductById(mItem.getMaVPP());

                        if (vanPhongPham != null) {
                            vanPhongPham.setSoLuong(vanPhongPham.getSoLuong() + mItem.getSoLuong());    // Cập nhật lại số lượng

                            return Observable.just(WorkWithDb.getInstance().update(vanPhongPham));

                        } else {
                            return  Observable.just(false);
                        }

                    } else {
                      return  Observable.just(false);
                    }

                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {

                    if (aBoolean) { // Xóa thành công ==> Cập nhật lại hiển thị

                        listGroupBill.get(positionParent).getListBills().remove(positionChild);

                        if (listGroupBill.get(positionParent).getListBills().size() > 0) {
                            adapterGroupBill.notifyItemChanged(positionParent);
                        } else {
                            listGroupBill.remove(positionParent);
                            adapterGroupBill.notifyItemRemoved(positionParent);
                            adapterGroupBill.notifyItemRangeChanged(positionParent, listGroupBill.size());
                        }

                        CustomToast.showToastSuccesstion(getContext(), "Xóa thành công", Toast.LENGTH_SHORT);

                    } else {
                        CustomToast.showToastError(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT);
                    }

                }, throwable -> {
                    CustomToast.showToastError(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT);
                }));

            }
        });
    }

    // Khởi tạo menu item
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_allocation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_seach) { // Tìm kiếm
            SearchDialogFragment searchDialogFragment = SearchDialogFragment.newInstance();
            searchDialogFragment.show(getChildFragmentManager(),  "");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        if (requestCode == AllocationActivity.REQUEST_ADD_BILL && resultCode == Activity.RESULT_OK) {
            getAllBills();
        }

    }
}
