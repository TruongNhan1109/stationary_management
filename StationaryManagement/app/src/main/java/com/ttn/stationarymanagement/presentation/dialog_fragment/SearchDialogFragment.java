package com.ttn.stationarymanagement.presentation.dialog_fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.CapPhat;
import com.ttn.stationarymanagement.data.local.model.NhanVien;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;
import com.ttn.stationarymanagement.presentation.adapter.GroupBillAdapter;
import com.ttn.stationarymanagement.presentation.baseview.FullScreenDialog;
import com.ttn.stationarymanagement.presentation.model.GroupBillModel;
import com.ttn.stationarymanagement.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
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
    CircularProgressButton btnSeach;

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

        btnSeach.setOnClickListener(v -> {

            if (TextUtils.isEmpty(edtSeachBox.getText().toString())) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Vui lòng nhập từ khóa cần tìm!");
                builder.setPositiveButton("Đồng ý", null);
                builder.show();

                return;
            }

            btnSeach.startAnimation();
            listResult.clear();

            String key = edtSeachBox.getText().toString();

            compositeDisposable.add(getAllBillById(key).subscribeOn(Schedulers.newThread())
                    .filter(capPhats -> capPhats != null)
                    .flatMap(capPhats -> {

                        if (capPhats.size() > 0) {

                            GroupBillModel groupBillModel = new GroupBillModel();
                            groupBillModel.setNameGroup("Mã phiếu: " + key);
                            groupBillModel.setListBills(capPhats);

                            listResult.add(groupBillModel);
                        }

                        return getAllBillByStaftId(key);

                    }).filter(capPhats -> capPhats != null)
                    .flatMap(capPhats -> {

                        if (capPhats.size() > 0) {

                            GroupBillModel groupBillModel = new GroupBillModel();
                            groupBillModel.setNameGroup("Mã nhân viên: " + key);
                            groupBillModel.setListBills(capPhats);

                            listResult.add(groupBillModel);
                        }

                        return getAllBillByProductId(key);

                    }).filter(capPhats -> capPhats != null)
                    .flatMap(capPhats -> {

                        if (capPhats.size() > 0) {

                            GroupBillModel groupBillModel = new GroupBillModel();
                            groupBillModel.setNameGroup("Mã nhân viên: " + key);
                            groupBillModel.setListBills(capPhats);

                            listResult.add(groupBillModel);
                        }

                        return getAllStaftByName(key);

                    }).filter(nhanViens -> nhanViens != null).flatMap(nhanViens -> {

                        if (nhanViens.size() > 0) {

                            GroupBillModel groupBillModel = new GroupBillModel();
                            groupBillModel.setNameGroup("Nhân viên: " + key);
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

                        if (vanPhongPhams.size() > 0) {

                            GroupBillModel groupBillModel = new GroupBillModel();
                            groupBillModel.setNameGroup("Sản phẩm: " + key);

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
                        btnSeach.stopAnimation();
                        btnSeach.revertAnimation();
                        CustomToast.showToastSuccesstion(getContext(), "Tìm kiếm thành công", Toast.LENGTH_SHORT);
                    }, throwable -> {
                        btnSeach.stopAnimation();
                        btnSeach.revertAnimation();
                    }, () -> {

                        if (listResult.size() > 0) {
                            adapterGroupBill.notifyDataSetChanged();
                        }
                    }));


        });

    }

    private Observable<List<CapPhat>> getAllBillById(String id) {
        return Observable.just(WorkWithDb.getInstance().getAllAlocationByIdBill(id));
    }

    private Observable<List<CapPhat>> getAllBillByStaftId(String key) {
        return Observable.just(WorkWithDb.getInstance().getAllocationByStaftId(key));
    }

    private Observable<List<CapPhat>> getAllBillByProductId(String key) {
        return Observable.just(WorkWithDb.getInstance().getAllocationByIdProduct(key));
    }

    private Observable<List<NhanVien>> getAllStaftByName(String key) {
        return Observable.just(WorkWithDb.getInstance().getAllStaftByName(key));
    }

    private Observable<List<VanPhongPham>> getAllProductByNameProduct(String nameProduct) {
        return Observable.just(WorkWithDb.getInstance().getAllProductByName(nameProduct));
    }

    private void getDataSuggest() {


        listDataSugestion.clear();
        compositeDisposable.add(getAllBill().subscribeOn(Schedulers.newThread())
                .filter(capPhats -> capPhats != null)
                .flatMap(capPhats -> {

                    for (CapPhat item : capPhats) {
                        listDataSugestion.add(item.getMaPhieu() + "");
                    }

                    return getAllStaft();

                }).filter(nhanViens -> nhanViens != null)
                .flatMap(nhanViens -> {

                    for (NhanVien item : nhanViens) {
                        listDataSugestion.add(item.getTenNV());
                    }
                    return getAllProduct();

                }).filter(vanPhongPhams -> vanPhongPhams != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(vanPhongPhams -> {

                    for (VanPhongPham item : vanPhongPhams) {
                        listDataSugestion.add(item.getTenSP());
                    }

                }, throwable -> {

                    CustomToast.showToastError(getContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT);

                }, () -> {
                    adapterSugestion.notifyDataSetChanged();
                }));

    }

    private void setControls() {

        compositeDisposable = new CompositeDisposable();

        listResult = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListResult.setLayoutManager(linearLayoutManager);
        adapterGroupBill = new GroupBillAdapter(getContext(), listResult);
        rvListResult.setAdapter(adapterGroupBill);

        listDataSugestion = new ArrayList<>();
        adapterSugestion = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, listDataSugestion);
        edtSeachBox.setThreshold(1);
        edtSeachBox.setAdapter(adapterSugestion);


    }

    private Observable<List<CapPhat>> getAllBill() {
        return Observable.just(WorkWithDb.getInstance().getAllAllocation());
    }

    private Observable<List<NhanVien>> getAllStaft() {
        return Observable.just(WorkWithDb.getInstance().getAllStaft());
    }

    private Observable<List<VanPhongPham>> getAllProduct() {
        return Observable.just(WorkWithDb.getInstance().getAllProduct());
    }
}
