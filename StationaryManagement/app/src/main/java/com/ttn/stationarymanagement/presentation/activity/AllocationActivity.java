package com.ttn.stationarymanagement.presentation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.NhanVien;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;
import com.ttn.stationarymanagement.presentation.adapter.SelectProductAdapter;
import com.ttn.stationarymanagement.presentation.adapter.SelectStaftAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AllocationActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spn_activity_allocation_select_product)
    Spinner spnSelectProduct;

    @BindView(R.id.spn_activity_allocation_select_staft)
    Spinner spnSelectStaft;

    private SelectProductAdapter selectProductAdapter;
    private SelectStaftAdapter selectStaftAdapter;

    private List<VanPhongPham> listProducts;
    private List<NhanVien> listStafts;

    private CompositeDisposable compositeDisposable;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, AllocationActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocation);
        ButterKnife.bind(this);

        setControls();

        getDatas();


    }

    private void getDatas() {

        compositeDisposable.add(getAllProduct().subscribeOn(Schedulers.newThread()).flatMap(vanPhongPhams -> {
            listProducts.addAll(vanPhongPhams);
          return  Observable.just(WorkWithDb.getInstance().getAllStaft());
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(nhanViens -> {
            listStafts.addAll(nhanViens);

            if (listProducts.size() < 1 || listStafts.size() < 1) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Thông báo").setMessage("Chưa có nhân viên hoặc sản phẩm để cấp phát!");
                builder.setPositiveButton("Đồng ý",null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                builder.show();

                return;
            }

            selectProductAdapter.notifyDataSetChanged();
            selectStaftAdapter.notifyDataSetChanged();

        }));

    }

    private void setControls() {

        compositeDisposable = new CompositeDisposable();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cấp phát");

        listProducts = new ArrayList<>();
        listStafts = new ArrayList<>();

        selectProductAdapter = new SelectProductAdapter(this, listProducts);
        spnSelectProduct.setAdapter(selectProductAdapter);

        selectStaftAdapter = new SelectStaftAdapter(this, listStafts);
        spnSelectStaft.setAdapter(selectStaftAdapter);

    }

    private Observable<List<VanPhongPham>> getAllProduct() {
        return Observable.create(r -> {

            List<VanPhongPham> list = WorkWithDb.getInstance().getAllProduct();
            r.onNext(list);
            r.onComplete();

        });
    }

    private Observable<List<NhanVien>> getAllStaft() {
        return Observable.create(r -> {
            List<NhanVien> list = WorkWithDb.getInstance().getAllStaft();
            r.onNext(list);
            r.onComplete();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  // Sự kiện nút back
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}