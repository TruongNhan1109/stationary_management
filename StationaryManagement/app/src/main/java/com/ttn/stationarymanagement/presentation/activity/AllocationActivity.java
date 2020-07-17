package com.ttn.stationarymanagement.presentation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.attention.ShakeAnimator;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.CapPhat;
import com.ttn.stationarymanagement.data.local.model.NhanVien;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;
import com.ttn.stationarymanagement.presentation.adapter.SelectProductAdapter;
import com.ttn.stationarymanagement.presentation.adapter.SelectStaftAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseActivity;
import com.ttn.stationarymanagement.utils.CustomToast;
import com.ttn.stationarymanagement.utils.GetDataToCommunicate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AllocationActivity extends BaseActivity {

    public static final int REQUEST_ADD_BILL = 1;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spn_activity_allocation_select_product)
    Spinner spnSelectProduct;

    @BindView(R.id.spn_activity_allocation_select_staft)
    Spinner spnSelectStaft;

    @BindView(R.id.tv_activity_allocation_date)
    TextView tvDate;

    @BindView(R.id.tv_activity_allocation_title_amount)
    TextView tvTitleAmount;

    @BindView(R.id.edt_activity_allocation_enter_amount)
    EditText edtAmount;

    @BindView(R.id.edt_activity_allocation_price)
    EditText edtPrice;

    @BindView(R.id.btn_activity_allocation_done)
    Button btnDone;

    @BindView(R.id.edt_activity_allocation_note)
    EditText edtNote;

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
        setEvents();

    }

    private void setEvents() {

        spnSelectProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VanPhongPham vanPhongPham = listProducts.get(position);
                tvTitleAmount.setText("Số lượng cấp (" + vanPhongPham.getSoLuong() + ")");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("vip", "on text changed " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("vip", "after text change");

                if (edtAmount.length() == 0) {
                    edtAmount.setText("0");
                }

                if (edtAmount.getText().length() > 0) {

                    int amount = Integer.parseInt(edtAmount.getText().toString());
                    VanPhongPham vanPhongPham = listProducts.get(spnSelectProduct.getSelectedItemPosition());

                    if (amount > vanPhongPham.getSoLuong()) {
                        new ShakeAnimator().setDuration(700).setRepeatTimes(0).setTarget(edtAmount).start();
                        edtAmount.setText(vanPhongPham.getSoLuong() + "");
                        edtAmount.setError("Số lượng hiện tại không đủ");
                        return;
                    }

                    double price = amount * vanPhongPham.getDonGia();
                    edtPrice.setText(GetDataToCommunicate.changeToPrice(price));

                }

            }
        });

        tvDate.setOnClickListener(v -> {

            Calendar calendarCreate = Calendar.getInstance();

            int day = calendarCreate.get(Calendar.DATE);
            int month = calendarCreate.get(Calendar.MONTH);
            int year = calendarCreate.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    calendarCreate.set(i, i1, i2);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    tvDate.setText(simpleDateFormat.format(calendarCreate.getTime()));
                }
            }, year, month, day);

            datePickerDialog.show();

        });

        btnDone.setOnClickListener(v -> {

            if (TextUtils.isEmpty(edtAmount.getText().toString()) || Integer.parseInt(edtAmount.getText().toString()) == 0) {
                new ShakeAnimator().setDuration(700).setRepeatTimes(0).setTarget(edtAmount).start();
                edtAmount.setError("Số lượng không được để trống hoặc bằng 0");
                edtAmount.requestFocus();
                return;
            }

            createAllocation();

        });

    }

    private void createAllocation() {

        CapPhat phieu = new CapPhat();

        VanPhongPham sanPham = listProducts.get(spnSelectProduct.getSelectedItemPosition());
        phieu.setMaVPP(sanPham.getMaVPP());

        NhanVien nhanVien = listStafts.get(spnSelectStaft.getSelectedItemPosition());
        phieu.setMaNV(nhanVien.getMaNV());

        phieu.setSoLuong(Integer.parseInt(edtAmount.getText().toString()));
        phieu.setTongGia(sanPham.getDonGia() * phieu.getSoLuong());
        phieu.setNgayCap(tvDate.getText().toString());
        phieu.setTrangThai(1);
        phieu.setGhiChu(!TextUtils.isEmpty(edtNote.getText().toString()) ? edtNote.getText().toString() : "");

        Observable<Boolean> createBill = Observable.create(r -> {

            VanPhongPham sanPhamUpload = WorkWithDb.getInstance().getProductById(phieu.getMaVPP());
            sanPhamUpload.setSoLuong(sanPhamUpload.getSoLuong() - phieu.getSoLuong());
            sanPhamUpload.setDaDung(sanPhamUpload.getDaDung() + phieu.getSoLuong());
            r.onNext(WorkWithDb.getInstance().update(sanPhamUpload));
            r.onComplete();

        });

        compositeDisposable.add(createBill.subscribeOn(Schedulers.newThread()).flatMap(aBoolean -> {
            if (aBoolean) {
                return Observable.just(WorkWithDb.getInstance().insert(phieu));
            } else {
                return Observable.just(false);
            }

        }).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
            if (aBoolean) {
                CustomToast.showToastSuccesstion(getApplicationContext(), "Tạo phiếu thành công!", Toast.LENGTH_SHORT);

                Intent intent = getIntent();
                setResult(RESULT_OK, intent);

                finish();
            } else {
                CustomToast.showToastError(getApplicationContext(), "Tạo phiếu thất bại!", Toast.LENGTH_SHORT);
            }

        }, throwable -> {
            CustomToast.showToastError(getApplicationContext(), "Tạo phiếu thất bại!", Toast.LENGTH_SHORT);
        }));

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

            tvTitleAmount.setText("Số lượng cấp (" + listProducts.get(0).getSoLuong() + ")");


        }));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        tvDate.setText(simpleDateFormat.format(date));


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