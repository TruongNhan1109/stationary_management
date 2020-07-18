package com.ttn.stationarymanagement.presentation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.attention.ShakeAnimator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;
import com.ttn.stationarymanagement.presentation.baseview.BaseActivity;
import com.ttn.stationarymanagement.presentation.bottom_sheet.SelectPhotoBottomSheet;
import com.ttn.stationarymanagement.utils.CustomToast;
import com.ttn.stationarymanagement.utils.GetDataToCommunicate;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewProductActivity extends BaseActivity {

    public static final int KEY_ADD_PRODUCT = 1;
    public static final int KEY_EDIT_PRODUCT = 2;
    private long productId;
    private VanPhongPham productEdit;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_activity_new_product_photo)
    ImageView ivImageProduct;

    @BindView(R.id.tv_activity_new_product_name_preview)
    TextView tvNamePreview;

    @BindView(R.id.edt_activity_new_product_name_product)
    EditText edtNameProduct;

    @BindView(R.id.edt_activity_new_product_unit_product)
    EditText edtUnit;

    @BindView(R.id.edt_activity_new_product_number)
    EditText edtNumberProduct;

    @BindView(R.id.edt_activity_new_product_price)
    EditText edtPrice;

    @BindView(R.id.edt_activity_new_product_note)
    EditText edtNote;

    @BindView(R.id.btn_activity_new_product_done)
    Button btnDone;

    private int requestSelectPhoto = 1;

    private String imageSelect;

    private CompositeDisposable compositeDisposable;


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, NewProductActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        ButterKnife.bind(this);

        setControls();
        getDatas();
        setEvents();

    }

    private void getDatas() {

        if (getIntent().getExtras() != null) {  // Lấy mã sản phẩm cần sửa nếu là sửa sản phẩm
            this.productId = getIntent().getLongExtra("PRODUCT_ID", 0);
        }

        if (productId != 0) {       // Upload

            // Ob lấy thông tin sản phẩm
            Observable<VanPhongPham> getDataProduct = Observable.create(r -> {
                try {
                    r.onNext(WorkWithDb.getInstance().getProductById(productId));
                    r.onComplete();
                    ;
                } catch (Exception e) {
                    r.onError(e);
                }
            });

            compositeDisposable.add(getDataProduct.observeOn(Schedulers.newThread())
                    .filter(vanPhongPham -> vanPhongPham != null)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(vanPhongPham -> {

                        this.productEdit = vanPhongPham;   // Lưu thông tin sản phẩm cần sửa

                        imageSelect = !TextUtils.isEmpty(productEdit.getAnh()) ? productEdit.getAnh() : "";     // Đường dẫn ảnh đã chọn

                        Picasso.get().load(new File(imageSelect)).error(R.mipmap.app_icon).fit().centerInside().into(ivImageProduct);

                        // Đưa các thông tin nhân viên lên hiển thị
                        tvNamePreview.setText(productEdit.getTenSP());
                        edtNameProduct.setText(productEdit.getTenSP());
                        edtUnit.setText(!TextUtils.isEmpty(productEdit.getDonVi()) ? productEdit.getDonVi() : "");
                        edtNumberProduct.setText(productEdit.getSoLuong() + "");
                        edtPrice.setText(productEdit.getDonGia() + "");
                        edtNote.setText(!TextUtils.isEmpty(productEdit.getGhiChu()) ? productEdit.getGhiChu() : "");
                        edtNumberProduct.setEnabled(false);
                        btnDone.setText(getResources().getString(R.string.upload));


                    }, throwable -> {
                        CustomToast.showToastError(getApplicationContext(), getResources().getString(R.string.can_not_get_info_proudct), Toast.LENGTH_SHORT);
                        finish();
                    }));


        }

    }

    private void setEvents() {

        // Khi nhập tên sản phẩm
        edtNameProduct.setOnEditorActionListener((v, actionId, event) -> {
            tvNamePreview.setText(v.getText().toString());
            return false;
        });

        // Khi nhấn lưu
        btnDone.setOnClickListener(v -> {

            // Kiểm tra tên sản phẩm
            if (TextUtils.isEmpty(edtNameProduct.getText().toString())) {

                new ShakeAnimator().setDuration(700).setRepeatTimes(0).setTarget(edtNameProduct).start();
                edtNameProduct.setError(getResources().getString(R.string.please_enter_name_of_product));
                edtNameProduct.requestFocus();
                return;
            }

            // Kiểm tra số lượng
            if (TextUtils.isEmpty(edtNumberProduct.getText().toString())) {
                new ShakeAnimator().setDuration(700).setRepeatTimes(0).setTarget(edtNumberProduct).start();
                edtNumberProduct.setError(getResources().getString(R.string.please_enter_number));
                edtNumberProduct.requestFocus();
                return;

            }

            // Kiểm tra giá tiền
            if (TextUtils.isEmpty(edtPrice.getText().toString())) {

                new ShakeAnimator().setDuration(700).setRepeatTimes(0).setTarget(edtPrice).start();
                edtPrice.setError(getResources().getString(R.string.enter_price_of_product));
                edtPrice.requestFocus();
                return;

            }

            if (productId != 0) {   // Cập nhật
                uploadProduct();

            } else {    // Tao mới sản phẩm
                createNewProduct();
            }


        });

        ivImageProduct.setOnClickListener(v -> {
            SelectPhotoBottomSheet selectPhotoBottomSheet = SelectPhotoBottomSheet.newInstance();
            selectPhotoBottomSheet.setListener(new SelectPhotoBottomSheet.SelectPhotoDialogListener() {
                @Override
                public void onSelectFromLibrary() {

                    Intent intent = new Intent();

                    // Chỉ định kiểu file cần hiển thị
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    //Hiển thị các ứng dụng có thể xử lý ảnh
                    startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_photo)), requestSelectPhoto);

                }

                @Override
                public void onSelectFromCamera() {

                }
            });

            selectPhotoBottomSheet.show(getSupportFragmentManager(), SelectPhotoBottomSheet.TAG);

        });
    }

    // Lưu thông tin được cập nhật
    private void uploadProduct() {

        productEdit.setAnh(!TextUtils.isEmpty(imageSelect) ? imageSelect : "");
        productEdit.setTenSP(!TextUtils.isEmpty(edtNameProduct.getText().toString()) ? edtNameProduct.getText().toString() : "");
        productEdit.setDonVi(!TextUtils.isEmpty(edtUnit.getText().toString()) ? edtUnit.getText().toString() : "");
        productEdit.setDonGia(!TextUtils.isEmpty(edtPrice.getText().toString()) ? Double.parseDouble(edtPrice.getText().toString()) : 0);
        productEdit.setNgayTD(GetDataToCommunicate.getCurrentDate());
        productEdit.setGhiChu(!TextUtils.isEmpty(edtNote.getText().toString()) ? edtNote.getText().toString() : "");

        compositeDisposable.add(uploadProduct(productEdit)
                .subscribeOn(Schedulers.newThread()).
                        observeOn(AndroidSchedulers.mainThread())

                .subscribe(aBoolean -> {        // Cập nhật thành công
                    if (aBoolean) {
                        CustomToast.showToastSuccesstion(getApplicationContext(), getResources().getString(R.string.upload_success), Toast.LENGTH_SHORT);

                        Intent intent = getIntent();
                        setResult(KEY_EDIT_PRODUCT, intent);
                        finish();

                    } else {
                        CustomToast.showToastError(getApplicationContext(), getResources().getString(R.string.upload_failed), Toast.LENGTH_SHORT);
                    }

                }, throwable -> {
                    CustomToast.showToastError(getApplicationContext(), getResources().getString(R.string.upload_failed), Toast.LENGTH_SHORT);
                }));

    }

    // Tạo mới sản phẩm
    private void createNewProduct() {

        VanPhongPham newProduct = new VanPhongPham();
        newProduct.setAnh(!TextUtils.isEmpty(imageSelect) ? imageSelect : "");
        newProduct.setTenSP(!TextUtils.isEmpty(edtNameProduct.getText().toString()) ? edtNameProduct.getText().toString() : "");
        newProduct.setDonVi(!TextUtils.isEmpty(edtUnit.getText().toString()) ? edtUnit.getText().toString() : "");
        newProduct.setSoLuong(!TextUtils.isEmpty(edtNumberProduct.getText().toString()) ? Integer.parseInt(edtNumberProduct.getText().toString()) : 0);
        newProduct.setDonGia(!TextUtils.isEmpty(edtPrice.getText().toString()) ? Double.parseDouble(edtPrice.getText().toString()) : 0);
        newProduct.setNgayTao(GetDataToCommunicate.getCurrentDate());
        newProduct.setGhiChu(!TextUtils.isEmpty(edtNote.getText().toString()) ? edtNote.getText().toString() : "");

        compositeDisposable.add(createProduct(newProduct).subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {

            if (aBoolean) {
                CustomToast.showToastSuccesstion(getApplicationContext(), getResources().getString(R.string.add_successful), Toast.LENGTH_SHORT);

                Intent intent = getIntent();
                setResult(KEY_ADD_PRODUCT, intent);
                finish();

            } else {
                CustomToast.showToastError(getApplicationContext(), getResources().getString(R.string.add_failed), Toast.LENGTH_SHORT);
            }

        }, throwable -> {
            CustomToast.showToastError(getApplicationContext(), getResources().getString(R.string.add_failed), Toast.LENGTH_SHORT);
        }));


    }

    private void setControls() {
        compositeDisposable = new CompositeDisposable();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.add_product));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        // Nhận kết quả chọn ảnh từ thư viện
        if (requestCode == requestSelectPhoto && resultCode == RESULT_OK) {

            // Lấy địa chỉ hình ảnh
            Uri uri = data.getData();

            // Lưu lại địa chỉ hình ảnh được chọn
            String imagePath = getPath(uri);

            if (!TextUtils.isEmpty(imagePath)) {

                this.imageSelect = imagePath;

                Picasso.get().load(new File(imagePath)).error(R.mipmap.app_icon).fit().centerInside().into(ivImageProduct, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });

            } else {
                CustomToast.showToastError(getApplicationContext(), getResources().getString(R.string.select_photo_fail), Toast.LENGTH_SHORT);
            }

        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
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

    // Ob thêm sản phẩm
    private Observable<Boolean> createProduct(VanPhongPham newProduct) {
        return Observable.create(r -> {
            try {
                r.onNext(WorkWithDb.getInstance().insert(newProduct));
                r.onComplete();

            } catch (Exception e) {
                r.onError(e);
            }
        });

    }


    // Ob cập nhật sản phẩm
    private Observable<Boolean> uploadProduct(VanPhongPham product) {
        return Observable.create(r -> {
            try {
                r.onNext(WorkWithDb.getInstance().update(product));
                r.onComplete();

            } catch (Exception e) {
                r.onError(e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}