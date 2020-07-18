package com.ttn.stationarymanagement.presentation.dialog_fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.rey.material.widget.ProgressView;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.CapPhat;
import com.ttn.stationarymanagement.data.local.model.NhanVien;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;
import com.ttn.stationarymanagement.presentation.activity.DetailBillActivity;
import com.ttn.stationarymanagement.presentation.adapter.GroupBillAdapter;
import com.ttn.stationarymanagement.presentation.baseview.FullScreenDialog;
import com.ttn.stationarymanagement.presentation.model.GroupBillModel;
import com.ttn.stationarymanagement.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableConverter;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchDialogFragment extends FullScreenDialog {

    private List<String> listDataSugestion;
    private ArrayAdapter<String> adapterSugestion;
    private CompositeDisposable compositeDisposable;

    @BindView(R.id.atv_dialog_fragment_seach_seach_box)
    AutoCompleteTextView edtSeachBox;

    @BindView(R.id.rv_dialog_fragment_seach_list_result)
    RecyclerView rvListResult;

    @BindView(R.id.btn_dialog_fragment_seach_seach)
    Button btnSeach;

    @BindView(R.id.pv_loading)
    ProgressView pvLoading;

    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    private List<GroupBillModel> listResult;
    private GroupBillAdapter adapterGroupBill;

    public static SearchDialogFragment newInstance() {
        SearchDialogFragment fragment = new SearchDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_seach, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setControls();
        getDataSuggest();
        setEvents();
    }

    private void setEvents() {

        tvCancel.setOnClickListener(v -> dismiss());

        btnSeach.setOnClickListener(v -> {

            // Kiểm tra giá trị tìm kiếm
            if (TextUtils.isEmpty(edtSeachBox.getText().toString())) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getResources().getString(R.string.please_enter_key_to_seach));
                builder.setPositiveButton(getResources().getString(R.string.ok), null);
                builder.show();
                return;
            }

            listResult.clear();
            pvLoading.start();

            String key = edtSeachBox.getText().toString();      // Từ khóa tìm kiếm

            compositeDisposable.add(getAllBillById(key).subscribeOn(Schedulers.newThread())
                    .filter(capPhats -> capPhats != null)
                    .flatMap(capPhats -> {

                        // Tạo kết quả theo mã phiếu
                        if (capPhats.size() > 0) {
                            GroupBillModel groupBillModel = new GroupBillModel();
                            groupBillModel.setNameGroup(getResources().getString(R.string.code_bill) + ": " + key);
                            groupBillModel.setListBills(capPhats);

                            listResult.add(groupBillModel);
                        }

                        return getAllBillByStaftId(key);

                    }).filter(capPhats -> capPhats != null)
                    .flatMap(capPhats -> {

                        // Khởi tạo kết quả theo mã nhân viên
                        if (capPhats.size() > 0) {

                            GroupBillModel groupBillModel = new GroupBillModel();
                            groupBillModel.setNameGroup(getResources().getString(R.string.code_staft) + ": " + key);
                            groupBillModel.setListBills(capPhats);

                            listResult.add(groupBillModel);
                        }

                        return getAllBillByProductId(key);

                    }).filter(capPhats -> capPhats != null)
                    .flatMap(capPhats -> {

                        // Khởi tạo kết quả theo mã sản phẩm
                        if (capPhats.size() > 0) {

                            GroupBillModel groupBillModel = new GroupBillModel();
                            groupBillModel.setNameGroup(getResources().getString(R.string.code_product) +": " + key);
                            groupBillModel.setListBills(capPhats);

                            listResult.add(groupBillModel);
                        }

                        return getAllStaftByName(key);

                    }).filter(nhanViens -> nhanViens != null).flatMap(nhanViens -> {

                        // Khỏi tạo kết quả theo tên nhân viên
                        if (nhanViens.size() > 0) {

                            GroupBillModel groupBillModel = new GroupBillModel();
                            groupBillModel.setNameGroup(getResources().getString(R.string.staft) + ": " + key);
                            List<CapPhat> list = new ArrayList<>();

                            for (NhanVien nv : nhanViens) {

                                List<CapPhat> listBills = WorkWithDb.getInstance().getAllocationByStaftId(nv.getMaNV());
                                if (listBills != null && listBills.size() > 0) {
                                    list.addAll(listBills);
                                }
                            }

                            if (list.size() > 0) {
                                groupBillModel.setListBills(list);
                                listResult.add(groupBillModel);
                            }

                        }

                        return getAllProductByNameProduct(key);

                    }).filter(vanPhongPhams -> vanPhongPhams != null)
                    .flatMap(vanPhongPhams -> {

                        // Khởi tạo kết quả theo tên sản phẩm
                        if (vanPhongPhams.size() > 0) {

                            GroupBillModel groupBillModel = new GroupBillModel();
                            groupBillModel.setNameGroup(getResources().getString(R.string.product) + ": " + key);

                            List<CapPhat> list = new ArrayList<>();

                            for (VanPhongPham vp : vanPhongPhams) {
                                List<CapPhat> listBills = WorkWithDb.getInstance().getAllocationByProductId(vp.getMaVPP() + "");
                                if (listBills != null && listBills.size() > 0) {
                                    list.addAll(listBills);
                                }
                            }

                            if (list.size() > 0) {
                                groupBillModel.setListBills(list);
                                listResult.add(groupBillModel);
                            }
                        }

                        return Observable.just(true);

                    }).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
                        CustomToast.showToastSuccesstion(getContext(), getResources().getString(R.string.search_succesful), Toast.LENGTH_SHORT);
                    }, throwable -> {
                        pvLoading.stop();
                        CustomToast.showToastError(getContext(), getResources().getString(R.string.occurre_error), Toast.LENGTH_SHORT);
                    }, () -> {
                        pvLoading.stop();
                        if (listResult.size() > 0) {
                            adapterGroupBill.notifyDataSetChanged();
                        }
                    }));


        });

        adapterGroupBill.setListener(new GroupBillAdapter.OnGroupBillAdapterListener() {
            @Override
            public void onItemClick(int positionParent, int positionChild) {    // Xem chi tiết một bill

                CapPhat mItem = listResult.get(positionParent).getListBills().get(positionChild);

                Intent intent = DetailBillActivity.getCallingIntent(getContext());
                intent.putExtra("ID_BILL", mItem.getMaPhieu());
                startActivity(intent);


            }

            @Override
            public void onButtonRemoveClick(int positionParent, int positionChild) {        // Xóa sản phẩm

                CapPhat mItem = listResult.get(positionParent).getListBills().get(positionChild);

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

                                listResult.get(positionParent).getListBills().remove(positionChild);

                                if (listResult.get(positionParent).getListBills().size() > 0) {
                                    adapterGroupBill.notifyItemChanged(positionParent);
                                } else {
                                    listResult.remove(positionParent);
                                    adapterGroupBill.notifyItemRemoved(positionParent);
                                    adapterGroupBill.notifyItemRangeChanged(positionParent, listResult.size());
                                }

                                CustomToast.showToastSuccesstion(getContext(), getResources().getString(R.string.delete_successful), Toast.LENGTH_SHORT);

                            } else {
                                CustomToast.showToastError(getContext(), getResources().getString(R.string.delete_failed), Toast.LENGTH_SHORT);
                            }

                        }, throwable -> {
                            CustomToast.showToastError(getContext(), getResources().getString(R.string.delete_failed), Toast.LENGTH_SHORT);
                        }));

            }
        });

    }

    // Lấy các phiếu theo mã phiếu
    private Observable<List<CapPhat>> getAllBillById(String id) {
        return Observable.just(WorkWithDb.getInstance().getAllAlocationByIdBill(id));
    }

    // Lấy các hóa đơn theo mã nhân viên
    private Observable<List<CapPhat>> getAllBillByStaftId(String key) {
        return Observable.just(WorkWithDb.getInstance().getAllocationByStaftId(key));
    }

    // Lấy các hóa đơn theo mã sản phẩm
    private Observable<List<CapPhat>> getAllBillByProductId(String key) {
        return Observable.just(WorkWithDb.getInstance().getAllocationByIdProduct(key));
    }

    // Lấy danh sách nhân viên theo tên
    private Observable<List<NhanVien>> getAllStaftByName(String key) {
        return Observable.just(WorkWithDb.getInstance().getAllStaftByName(key));
    }

    // Lấy danh sách sản phẩm theo tên sản phẩm
    private Observable<List<VanPhongPham>> getAllProductByNameProduct(String nameProduct) {
        return Observable.just(WorkWithDb.getInstance().getAllProductByName(nameProduct));
    }

    // Lấy dữ liệu gợi ý
    private void getDataSuggest() {

        listDataSugestion.clear();

        compositeDisposable.add(getAllBill().subscribeOn(Schedulers.newThread())
                .filter(capPhats -> capPhats != null)
                .flatMap(capPhats -> {

                    // Tạo gợi ý theo mã phiếu
                    for (CapPhat item : capPhats) {
                        listDataSugestion.add(item.getMaPhieu() + "");
                    }

                    // Lấy danh sách nhân viên để tạo gợi ý
                    return getAllStaft();

                }).filter(nhanViens -> nhanViens != null)
                .flatMap(nhanViens -> {

                    // Tạo gợi ý theo tên nhân viên
                    for (NhanVien item : nhanViens) {
                        listDataSugestion.add(item.getTenNV());
                    }

                    // Lấy danh sản sản phẩm để tạo gợi ý
                    return getAllProduct();

                }).filter(vanPhongPhams -> vanPhongPhams != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(vanPhongPhams -> {

                    // Tạo gợi ý theo tên sản phẩm
                    for (VanPhongPham item : vanPhongPhams) {
                        listDataSugestion.add(item.getTenSP());
                    }

                }, throwable -> {

                    CustomToast.showToastError(getContext(), getResources().getString(R.string.occurre_error), Toast.LENGTH_SHORT);

                }, () -> {
                    adapterSugestion.notifyDataSetChanged();
                }));
    }

    private void setControls() {

        compositeDisposable = new CompositeDisposable();

        // Khởi tạo list kết quả và adapter
        listResult = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListResult.setLayoutManager(linearLayoutManager);
        adapterGroupBill = new GroupBillAdapter(getContext(), listResult);
        rvListResult.setAdapter(adapterGroupBill);

        // Khởi tạo list các giá trị gợi ý và adpater
        listDataSugestion = new ArrayList<>();
        adapterSugestion = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, listDataSugestion);
        edtSeachBox.setThreshold(1);
        edtSeachBox.setAdapter(adapterSugestion);


    }

    // Observable lấy danh sách phiếu
    private Observable<List<CapPhat>> getAllBill() {
        return Observable.just(WorkWithDb.getInstance().getAllAllocation());
    }

    // Observable lấy danh sách nhân viên
    private Observable<List<NhanVien>> getAllStaft() {
        return Observable.just(WorkWithDb.getInstance().getAllStaft());
    }

    private Observable<List<VanPhongPham>> getAllProduct() {
        return Observable.just(WorkWithDb.getInstance().getAllProduct());
    }
}
