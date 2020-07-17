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

public class StationaryManagerFragment extends BaseFragment {

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

    private  GroupProductAdapter groupProductAdapter;
    private List<GroupProductModel> groupProductModels;
    private CompositeDisposable compositeDisposable;
    private  int totalProduct = 0;
    private double totalPrice = 0;

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
            startActivityForResult(intent, NewProductActivity.KEY_ADD_PRODUCT);

        });

        groupProductAdapter.setListener(new GroupProductAdapter.GroupProductApapterListener() {
            @Override
            public void onProductClick(VanPhongPham item) {     // Edit Product

                Intent intent = NewProductActivity.getCallingIntent(getContext());
                intent.putExtra("PRODUCT_ID", item.getMaVPP());
                startActivityForResult(intent, NewProductActivity.KEY_ADD_PRODUCT);

            }

            @Override
            public void onDeleteProduct(int positionGroup, VanPhongPham itemDelete) {

                Observable<Boolean> obRemoveProduct = Observable.create(r -> {
                    try {
                        r.onNext(WorkWithDb.getInstance().delete(itemDelete));
                    } catch (Exception e) {
                        r.onError(e);
                    }
                });

                compositeDisposable.add(obRemoveProduct.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
                    if (aBoolean) {
                        CustomToast.showToastSuccesstion(getContext(), "Đã xóa sản phẩm", Toast.LENGTH_SHORT);

                        groupProductModels.get(positionGroup).getVanPhongPhamList().remove(itemDelete);

                        if (groupProductModels.get(positionGroup).getVanPhongPhamList().size() > 0) {
                            groupProductAdapter.notifyItemChanged(positionGroup);
                        } else {
                            groupProductModels.remove(positionGroup);
                            groupProductAdapter.notifyItemRemoved(positionGroup);
                            groupProductAdapter.notifyItemRangeChanged(positionGroup, groupProductModels.size());
                        }

                    } else {
                        CustomToast.showToastError(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT);
                    }

                }, throwable -> {
                    CustomToast.showToastError(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT);
                }));
            }

            @Override
            public void onImportProduct(int positionGroup, int positionChild) {
                ImportProductDialog importProductDialog =  ImportProductDialog.newInstance();

                importProductDialog.setListener(amount -> {
                    VanPhongPham vanPhongPham = groupProductModels.get(positionGroup).getVanPhongPhamList().get(positionChild);
                    vanPhongPham.setSoLuong(vanPhongPham.getSoLuong() + amount);
                    WorkWithDb.getInstance().update(vanPhongPham);
                    groupProductAdapter.notifyItemChanged(positionGroup);
                    CustomToast.showToastSuccesstion(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT);
                });

                importProductDialog.show(getChildFragmentManager(), "");
            }

            @Override
            public void onItemClick(VanPhongPham item) {
                ShowDetailProductDialog showDetailProductDialog = ShowDetailProductDialog.newInstance();
                showDetailProductDialog.setProduct(item);
                showDetailProductDialog.show(getChildFragmentManager(), "");
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_pricegroup_master, menu);
    }

    private void getDatas() {

        totalProduct = 0;
        totalPrice = 0;

        compositeDisposable.add(
             getAllProduct().subscribeOn(Schedulers.newThread()).flatMap(list -> {
                 Map<String, List<VanPhongPham>> listGroup = new HashMap<>();

                 for(VanPhongPham item: list) {
                     String amlpha = item.getTenSP().substring(0, 1);

                     if (listGroup.get(amlpha) == null) {
                         List<VanPhongPham> listItem = new ArrayList<>();
                         listItem.add(item);
                         listGroup.put(amlpha, listItem);
                         totalProduct += item.getSoLuong();
                         totalPrice += item.getDonGia() * item.getSoLuong();

                     } else {
                         List<VanPhongPham> listItem = listGroup.get(amlpha);
                         listItem.add(item);
                         listGroup.put(amlpha, listItem);

                         totalProduct += item.getSoLuong();
                         totalPrice += item.getDonGia() * item.getSoLuong();
                     }
                 }

                 List<GroupProductModel> listGroupProduct = new ArrayList<>();

                 for(Map.Entry<String, List<VanPhongPham>> entry: listGroup.entrySet()) {
                     GroupProductModel groupProductModel = new GroupProductModel();
                     groupProductModel.setTextAlpha(entry.getKey());
                     groupProductModel.setVanPhongPhamList(entry.getValue());
                     listGroupProduct.add(groupProductModel);
                 }

                 return Observable.just(listGroupProduct);

             }).observeOn(AndroidSchedulers.mainThread()).subscribe(list -> {

                 if (list.size() > 0) {

                     tvTotalProduct.setText(totalProduct + "");
                     tvTotalPrice.setText(GetDataToCommunicate.changeToPrice(totalPrice) + "");

                     lnlNotifyEmplty.setVisibility(View.GONE);
                     rvListProducts.setVisibility(View.VISIBLE);
                     groupProductModels.clear();
                     groupProductModels.addAll(list);
                     groupProductAdapter.notifyDataSetChanged();

                 } else {
                     lnlNotifyEmplty.setVisibility(View.VISIBLE);
                     rvListProducts.setVisibility(View.GONE);
                 }

             }, throwable -> {
                 CustomToast.showToastError(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT);
             })
        );

    }

    private void setControls() {

        compositeDisposable = new CompositeDisposable();
        groupProductModels = new ArrayList<>();
        groupProductAdapter = new GroupProductAdapter(getContext(), groupProductModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListProducts.setLayoutManager(linearLayoutManager);
        rvListProducts.setAdapter(groupProductAdapter);
    }

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

        if (requestCode == NewProductActivity.KEY_ADD_PRODUCT && requestCode == NewProductActivity.KEY_ADD_PRODUCT) {
            getDatas();
        }

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
