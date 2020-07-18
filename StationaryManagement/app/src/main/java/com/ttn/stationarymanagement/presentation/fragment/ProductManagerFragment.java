package com.ttn.stationarymanagement.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;
import com.ttn.stationarymanagement.presentation.activity.NewProductActivity;
import com.ttn.stationarymanagement.presentation.adapter.GroupProductAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;
import com.ttn.stationarymanagement.presentation.dialog_fragment.ImportProductDialog;
import com.ttn.stationarymanagement.presentation.dialog_fragment.ShowDetailProductDialog;
import com.ttn.stationarymanagement.presentation.model.GroupProductModel;
import com.ttn.stationarymanagement.utils.CustomToast;
import com.ttn.stationarymanagement.utils.GetDataToCommunicate;

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

public class ProductManagerFragment extends BaseFragment {

    @BindView(R.id.rv_fragment_stationary_manager_list_product)
    RecyclerView rvListProducts;

    @BindView(R.id.fab)
    FloatingActionButton fbAdd;

    @BindView(R.id.lnl_fragment_stationary_manager_notify_emplty)
    LinearLayout lnlNotifyEmplty;

    @BindView(R.id.tv_fragment_stationary_manager_total_product)
    TextView tvTotalProduct;

    @BindView(R.id.tv_fragment_stationary_manager_total_price)
    TextView tvTotalPrice;

    private GroupProductAdapter groupProductAdapter;
    private List<GroupProductModel> groupProductModels;     // Danh sách nhóm sản phẩm theo Aplha
    private CompositeDisposable compositeDisposable;

    private int totalProduct = 0;      //  Tổng số sản phẩm
    private double totalPrice = 0;      // Tổng giá trị

    public static ProductManagerFragment newInstance() {
        ProductManagerFragment fragment = new ProductManagerFragment();
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


        // Thêm sản phẩm
        fbAdd.setOnClickListener(v -> {
            Intent intent = NewProductActivity.getCallingIntent(getContext());
            startActivityForResult(intent, NewProductActivity.KEY_ADD_PRODUCT);

            // Animation chuyển cảnh
            getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

        });

        groupProductAdapter.setListener(new GroupProductAdapter.GroupProductApapterListener() {
            @Override
            public void onProductClick(VanPhongPham item) {     // Chỉnh sửa sản phẩm

                Intent intent = NewProductActivity.getCallingIntent(getContext());
                intent.putExtra("PRODUCT_ID", item.getMaVPP());
                startActivityForResult(intent, NewProductActivity.KEY_ADD_PRODUCT);
                // Animation chuyển cảnh
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

            }

            @Override
            public void onDeleteProduct(int positionGroup, VanPhongPham itemDelete) {       // Xóa sản phẩm

                Observable<Boolean> obRemoveProduct = Observable.create(r -> {
                    try {
                        r.onNext(WorkWithDb.getInstance().delete(itemDelete));
                    } catch (Exception e) {
                        r.onError(e);
                    }
                });

                compositeDisposable.add(obRemoveProduct.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aBoolean -> {
                            if (aBoolean) {     // Xóa thành công
                                CustomToast.showToastSuccesstion(getContext(), getResources().getString(R.string.delete_successful), Toast.LENGTH_SHORT);

                                groupProductModels.get(positionGroup).getVanPhongPhamList().remove(itemDelete);

                                if (groupProductModels.get(positionGroup).getVanPhongPhamList().size() > 0) {   // Chỉ xóa sản phẩm
                                    groupProductAdapter.notifyItemChanged(positionGroup);

                                } else {        // Xóa luôn cả nhóm vì hết sản phẩm
                                    groupProductModels.remove(positionGroup);
                                    groupProductAdapter.notifyItemRemoved(positionGroup);
                                    groupProductAdapter.notifyItemRangeChanged(positionGroup, groupProductModels.size());
                                }

                            } else {
                                CustomToast.showToastError(getContext(),  getResources().getString(R.string.delete_failed), Toast.LENGTH_SHORT);
                            }

                        }, throwable -> {
                            CustomToast.showToastError(getContext(),  getResources().getString(R.string.delete_failed), Toast.LENGTH_SHORT);
                        }));
            }

            @Override
            public void onImportProduct(int positionGroup, int positionChild) {     // Nhập hàng

                ImportProductDialog importProductDialog = ImportProductDialog.newInstance();

                importProductDialog.setListener(amount -> {

                    VanPhongPham vanPhongPham = groupProductModels.get(positionGroup).getVanPhongPhamList().get(positionChild);
                    vanPhongPham.setSoLuong(vanPhongPham.getSoLuong() + amount);
                    WorkWithDb.getInstance().update(vanPhongPham);
                    groupProductAdapter.notifyItemChanged(positionGroup);
                    CustomToast.showToastSuccesstion(getContext(), getResources().getString(R.string.upload_success), Toast.LENGTH_SHORT);

                });

                importProductDialog.show(getChildFragmentManager(), "");
            }

            @Override
            public void onItemClick(VanPhongPham item) {        // Hiển thị chi tiết sản phẩm

                ShowDetailProductDialog showDetailProductDialog = ShowDetailProductDialog.newInstance();
                showDetailProductDialog.setProduct(item);   // Truyền dữ liệu sản phẩm
                showDetailProductDialog.show(getChildFragmentManager(), "");
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Lấy danh sách sản phẩm
    private void getDatas() {

        totalProduct = 0;
        totalPrice = 0;

        compositeDisposable.add(
                getAllProduct().subscribeOn(Schedulers.newThread())
                        .flatMap(list -> {

                            Map<String, List<VanPhongPham>> listGroup = new HashMap<>();

                            // Thực hiện gom nhóm thêm Alpha
                            for (VanPhongPham item : list) {

                                String amlpha = item.getTenSP().substring(0, 1);        // Lấy ký tự đầu

                                if (listGroup.get(amlpha) == null) {        // Ký tự chưa khởi tạo dữ liệu

                                    List<VanPhongPham> listItem = new ArrayList<>();
                                    listItem.add(item);
                                    listGroup.put(amlpha, listItem);

                                    // Tính tấn số lượng và đơn giá
                                    totalProduct += item.getSoLuong();
                                    totalPrice += item.getDonGia() * item.getSoLuong();


                                } else {        // Ký tự đã có sản phẩm rồi

                                    List<VanPhongPham> listItem = listGroup.get(amlpha);
                                    listItem.add(item);
                                    listGroup.put(amlpha, listItem);

                                    // Tính tấn số lượng và đơn giá
                                    totalProduct += item.getSoLuong();
                                    totalPrice += item.getDonGia() * item.getSoLuong();
                                }
                            }

                            // Tạo đối tượng gom nhóm các sản phẩm
                            List<GroupProductModel> listGroupProduct = new ArrayList<>();

                            for (Map.Entry<String, List<VanPhongPham>> entry : listGroup.entrySet()) {

                                GroupProductModel groupProductModel = new GroupProductModel();
                                groupProductModel.setTextAlpha(entry.getKey());         // Alpha
                                groupProductModel.setVanPhongPhamList(entry.getValue());    // Danh sách sản phẩm
                                listGroupProduct.add(groupProductModel);
                            }

                            return Observable.just(listGroupProduct);

                        }).observeOn(AndroidSchedulers.mainThread()).subscribe(list -> {

                    if (list.size() > 0) {  // Đã có sản phẩm

                        tvTotalProduct.setText(totalProduct + "");
                        tvTotalPrice.setText(GetDataToCommunicate.changeToPrice(totalPrice) + "");

                        lnlNotifyEmplty.setVisibility(View.GONE);
                        rvListProducts.setVisibility(View.VISIBLE);

                        groupProductModels.clear();
                        groupProductModels.addAll(list);
                        groupProductAdapter.notifyDataSetChanged();

                    } else {        // Thông báo chưa có sản phẩm

                        lnlNotifyEmplty.setVisibility(View.VISIBLE);
                        rvListProducts.setVisibility(View.GONE);

                    }

                }, throwable -> {
                    CustomToast.showToastError(getContext(), getResources().getString(R.string.occurre_error), Toast.LENGTH_SHORT);
                })
        );

    }

    private void setControls() {

        compositeDisposable = new CompositeDisposable();

        // Khởi tạo danh sách và adapter
        groupProductModels = new ArrayList<>();
        groupProductAdapter = new GroupProductAdapter(getContext(), groupProductModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListProducts.setLayoutManager(linearLayoutManager);
        rvListProducts.setAdapter(groupProductAdapter);
    }

    // Ob lấy danh sách sản phẩm
    private Observable<List<VanPhongPham>> getAllProduct() {

        return Observable.create(r -> {
            try {

                List<VanPhongPham> list = WorkWithDb.getInstance().getAllProduct();
                r.onNext(list);
                r.onComplete();

            } catch (Exception e) {
                r.onError(e);
            }

        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        // Nhận kết quả thêm sản phẩm
        if (requestCode == NewProductActivity.KEY_ADD_PRODUCT && requestCode == NewProductActivity.KEY_ADD_PRODUCT) {
            getDatas();
        }

        // Nhận kết quả chỉnh sửa sản phẩm
        if (requestCode == NewProductActivity.KEY_EDIT_PRODUCT && resultCode == NewProductActivity.KEY_EDIT_PRODUCT) {
            getDatas();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
