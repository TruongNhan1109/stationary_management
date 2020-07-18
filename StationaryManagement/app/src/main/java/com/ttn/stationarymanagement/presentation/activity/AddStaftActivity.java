package com.ttn.stationarymanagement.presentation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.attention.ShakeAnimator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.NhanVien;
import com.ttn.stationarymanagement.data.local.model.PhongBan;
import com.ttn.stationarymanagement.data.local.model.VaiTro;
import com.ttn.stationarymanagement.presentation.adapter.SelectDepartmentAdapter;
import com.ttn.stationarymanagement.presentation.adapter.SelectRoleAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseActivity;
import com.ttn.stationarymanagement.utils.AppUtils;
import com.ttn.stationarymanagement.utils.CustomToast;
import com.ttn.stationarymanagement.utils.GetDataToCommunicate;

import java.io.File;
import java.text.ParseException;
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
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddStaftActivity extends BaseActivity {

    public static int REQUEST_ADD_STAFT = 1;
    public static int REQUEST_EDIT_STAFT = 2;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_activity_add_staft_photo)
    ImageView ivPhoto;

    @BindView(R.id.edt_activity_add_staft_name)
    EditText edtNameStaft;

    @BindView(R.id.edt_activity_add_staft_date_of_birth)
    TextView edtDateOfBirth;

    @BindView(R.id.edt_activity_add_staft_phone)
    EditText edtPhone;

    @BindView(R.id.edt_activity_add_staft_email)
    EditText edtEmail;

    @BindView(R.id.rdg_activity_add_staft_gender)
    RadioGroup rdoGender;

    @BindView(R.id.edt_activity_add_staft_note)
    EditText edtNote;

    @BindView(R.id.spn_activity_add_staft_role)
    Spinner spnRole;

    @BindView(R.id.spn_activity_add_staft_department)
    Spinner spDepartment;

    @BindView(R.id.btn_activity_add_staft_add)
    Button btnAdd;

    private String imageStaft = "";
    private List<VaiTro> listVaiTro;
    private int requestSelectPhoto = 1;
    SelectRoleAdapter selectRoleAdapter;
    private List<PhongBan> listPhongBan;
    SelectDepartmentAdapter selectDepartmentAdapter;
    private CompositeDisposable compositeDisposable;

    private long idStaftEdit;
    private boolean isUpload = false;
    private NhanVien nhanVienEdit;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, AddStaftActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staft);
        ButterKnife.bind(this);

        setControls();
        getDataAndSetView();
        setEvents();


    }

    private void getDataAndSetView() {

        // Lấy thông tin nhân viên cần cần nhật nếu chức năng cập nhật
        if (getIntent().hasExtra("ID_STAFT")) {
            idStaftEdit = getIntent().getLongExtra("ID_STAFT", 0);
            isUpload = true;
        }

        // Thiết lập ngày sinh ngày hiện tại
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        edtDateOfBirth.setText(simpleDateFormat.format(currentDate));

        // Lấy thông tin nhân viên cần cập nhật
        getAllData();

    }

    // Lấy danh sách vai trò và phòng ban cho spiner
    private void getAllData() {

        // Ob lấy danh sách vai trò
        Observable<List<VaiTro>> getDataRole = Observable.create(r -> {
            try {
                r.onNext(WorkWithDb.getInstance().getAllRole());
                r.onComplete();
            } catch (Exception e) {
                r.onError(e);
            }

        });

        compositeDisposable.add(getDataRole.subscribeOn(Schedulers.newThread())
                .flatMap(r -> {

                    listVaiTro.addAll(r);    // Lưu danh sách vai trò dưới cơ sở dữ liệu

                    // Ob lấy danh sách phòng ban
                    return Observable.just(WorkWithDb.getInstance().getAllDepartment());

                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(r -> {

                    listPhongBan.addAll(r); //  Lưu danh sách phòng ban

                    //  Kiểm tra không cho phép thêm hay sửa nếu danh sách phòng ban hoặc vai trò trống
                    if (listVaiTro.size() < 1 || listPhongBan.size() < 1) {
                        CustomToast.showToastWarning(getApplicationContext(), getResources().getString(R.string.please_setting_role_and_department_in_the_first), Toast.LENGTH_SHORT);
                        finish();
                    }

                    selectRoleAdapter.notifyDataSetChanged();
                    selectDepartmentAdapter.notifyDataSetChanged();

                }, throwable -> {
                    CustomToast.showToastError(getApplicationContext(), getResources().getString(R.string.occurre_error), Toast.LENGTH_SHORT);

                }, () -> {

                    if (isUpload) {     // Lấy thêm thông tin nhân viên cần upload nếu đây là sửa nhân viên
                        getInforStaft();
                    }

                }));

    }


    // Lấy thông tin nhân viên cần chỉnh sửa
    private void getInforStaft() {

        // Ob lấy nhân viên theo id
        Observable<NhanVien> getStaft = Observable.just(WorkWithDb.getInstance().getStaftById(idStaftEdit));

        getStaft.subscribeOn(Schedulers.newThread())
                .filter(nhanVien -> nhanVien != null)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(nhanVien -> {

            nhanVienEdit = nhanVien; // Lưu thông tin nhân viên cần chỉnh sửa

            // Lấy ảnh nhân viên
            if (!TextUtils.isEmpty(nhanVien.getAnh())) {

                this.imageStaft = nhanVien.getAnh();

                Picasso.get().load(new File(imageStaft)).error(R.mipmap.app_icon).fit().centerInside().into(ivPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });

            }

            edtNameStaft.setText(nhanVien.getTenNV());      // Tên nhân viên
            edtDateOfBirth.setText(nhanVien.getNgaySinh());     // Ngày sinh

            if (!TextUtils.isEmpty(nhanVien.getSDT())) {
                edtPhone.setText(nhanVien.getSDT());        // Số điện thoại
            }

            if (!TextUtils.isEmpty(nhanVien.getEmail())) {
                edtEmail.setText(nhanVien.getEmail());      // Email
            }

            // Set giới tính
            if (nhanVien.getGT() == 0) {

                RadioButton rdo = (RadioButton) findViewById(R.id.rdo_male);
                rdo.setChecked(true);


            } else if (nhanVien.getGT() == 1) {

                RadioButton rdo = (RadioButton) findViewById(R.id.rdo_female);
                rdo.setChecked(true);

            } else {

                RadioButton rdo = (RadioButton) findViewById(R.id.rdo_other);
                rdo.setChecked(true);
            }

            // Set vị trí phòng ban
            for (int i = 0; i < listPhongBan.size(); i++) {
                if (listPhongBan.get(i).getMaPB() == nhanVien.getMaPB()) {
                    spDepartment.setSelection(i);
                    break;
                }
            }

            // Set vị trí vai trò
            for (int i = 0; i < listVaiTro.size(); i++) {
                if (listVaiTro.get(i).getMaVT() == nhanVien.getMaVT()) {
                    spnRole.setSelection(i);
                    break;
                }
            }


            if (!TextUtils.isEmpty(nhanVien.getGhiChu())) {
                edtNote.setText(nhanVien.getGhiChu());      // Ghi chú
            }

            btnAdd.setText(getResources().getString(R.string.upload));
            getSupportActionBar().setTitle(getResources().getString(R.string.edit_staft));
        });


    }


    private void setEvents() {

        // Khi chọn ảnh
        ivPhoto.setOnClickListener(v -> {

            Intent intent = new Intent();
            // Chỉ định kiểu file cần hiển thị
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            //Hiển thị các ứng dụng có thể xử lý ảnh
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_photo)), requestSelectPhoto);

        });

        // Khi chọn ngày sinh
        edtDateOfBirth.setOnClickListener(v -> {

            Calendar calendarBirthday = Calendar.getInstance();

            if (!isUpload) {    // Nếu là thêm nhân viên thì lấy ngày hiện tại

                int day = calendarBirthday.get(Calendar.DATE);
                int month = calendarBirthday.get(Calendar.MONTH);
                int year = calendarBirthday.get(Calendar.YEAR);

                // Khởi tạo dialog chọn ngày
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendarBirthday.set(i, i1, i2);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        edtDateOfBirth.setText(simpleDateFormat.format(calendarBirthday.getTime()));
                    }
                }, year, month, day);


                datePickerDialog.show();
            } else {  // Nếu là cập nhật thì lấy ngày sinh nhân viên hiển thị đúng vị trí ngày sinh

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    calendarBirthday.setTime(df.parse(edtDateOfBirth.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int day = calendarBirthday.get(Calendar.DATE);
                int month = calendarBirthday.get(Calendar.MONTH);
                int year = calendarBirthday.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendarBirthday.set(i, i1, i2);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        edtDateOfBirth.setText(simpleDateFormat.format(calendarBirthday.getTime()));
                    }
                }, year, month, day);

                datePickerDialog.show();
            }

        });


        // Khi nhấn nút thêm
        btnAdd.setOnClickListener(v -> {

            // Kiểm tra tên nhân viên
            if (TextUtils.isEmpty(edtNameStaft.getText().toString())) {

                new ShakeAnimator().setDuration(700).setRepeatTimes(0).setTarget(edtNameStaft).start();
                edtNameStaft.setError(getResources().getString(R.string.name_staft_do_not_empty));
                edtNameStaft.requestFocus();
                return;
            }

            // Kiểm tra email
            if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                new ShakeAnimator().setDuration(700).setRepeatTimes(0).setTarget(edtEmail).start();
                edtEmail.setError(getResources().getString(R.string.email_do_not_empty));
                edtEmail.requestFocus();
                return;
            }

            // Kiểm tra cấu trúc email
            if (!AppUtils.checkValidEmail(edtEmail.getText().toString())) {
                new ShakeAnimator().setDuration(700).setRepeatTimes(0).setTarget(edtEmail).start();
                edtEmail.setError(getResources().getString(R.string.email_format_invalid));
                edtEmail.requestFocus();
                return;
            }

            if (isUpload) {     // Cập nhật thông tin nhân viên
                uploadStaft();
            } else {        // Thêm nhân viên
                createNewStaft();
            }


        });


    }

    // Cập nhật thông tin nhân viên
    private void uploadStaft() {

        nhanVienEdit.setAnh(!TextUtils.isEmpty(imageStaft) ? imageStaft : "");      // Cập nhật ảnh
        nhanVienEdit.setTenNV(edtNameStaft.getText().toString());       // Cập nhật tên
        nhanVienEdit.setNgaySinh(edtDateOfBirth.getText().toString());       //  Cập nhật ngày sinh
        nhanVienEdit.setSDT(GetDataToCommunicate.convertStringToString(edtPhone.getText().toString()));     // Cập nhật số điện thoại
        nhanVienEdit.setEmail(GetDataToCommunicate.convertStringToString(edtEmail.getText().toString()));       // Cập nhật email

        // Cập nhật giới tính
        switch (rdoGender.getCheckedRadioButtonId()) {
            case R.id.rdo_male: //Nam
                nhanVienEdit.setGT(0);
                break;
            case R.id.rdo_female: // Nu
                nhanVienEdit.setGT(1);
                break;
            case R.id.rdo_other:    // Khac
                nhanVienEdit.setGT(2);
                break;

        }

        // Cập nhật vai trò nhân viên
        if (listVaiTro.size() > 0) {
            VaiTro vaiTro = listVaiTro.get(spnRole.getSelectedItemPosition());
            nhanVienEdit.setMaVT(vaiTro.getMaVT());
        }

        // Cập nhật phòng ban
        if (listPhongBan.size() > 0) {
            PhongBan phongBan = listPhongBan.get(spDepartment.getSelectedItemPosition());
            nhanVienEdit.setMaPB(phongBan.getMaPB());
        }

        // Cập nhật ghi chú
        nhanVienEdit.setGhiChu(!TextUtils.isEmpty(edtNote.getText().toString()) ? edtNote.getText().toString() : "");

        // Ob cập nhật thông tin nhân viên
        Observable<Boolean> updateStaft = Observable.just(WorkWithDb.getInstance().update(nhanVienEdit));

        compositeDisposable.add(updateStaft.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {

                    if (aBoolean) {     // Cập nhật thành công
                        CustomToast.showToastSuccesstion(this, getResources().getString(R.string.upload_success), Toast.LENGTH_SHORT);

                        Intent intent = getIntent();
                        setResult(RESULT_OK, intent);
                        finish();

                    } else {
                        CustomToast.showToastError(this, getResources().getString(R.string.upload_failed), Toast.LENGTH_SHORT);
                    }
                }, throwable -> {
                    CustomToast.showToastError(this, getResources().getString(R.string.upload_failed), Toast.LENGTH_SHORT);
                }, () -> {

                }));

    }

    // Tạo mới nhân viên
    private void createNewStaft() {

        NhanVien nhanVien = new NhanVien();

        nhanVien.setAnh(!TextUtils.isEmpty(imageStaft) ? imageStaft : ""); // Ảnh nhân viên
        nhanVien.setTenNV(edtNameStaft.getText().toString());               // Tên nhân viên
        nhanVien.setNgaySinh(edtDateOfBirth.getText().toString());          // Ngày sinh
        nhanVien.setSDT(!TextUtils.isEmpty(edtPhone.getText().toString()) ? edtPhone.getText().toString() : "");    // Số điện thoại
        nhanVien.setEmail(GetDataToCommunicate.convertStringToString(edtEmail.getText().toString()));           // Email
        nhanVien.setNgayTao(GetDataToCommunicate.getCurrentDate());     // Ngày tạo

        // Giới tính
        switch (rdoGender.getCheckedRadioButtonId()) {
            case R.id.rdo_male: //Nam
                nhanVien.setGT(0);
                break;
            case R.id.rdo_female: // Nu
                nhanVien.setGT(1);
                break;
            case R.id.rdo_other:    // Khac
                nhanVien.setGT(2);
                break;
        }


        // Vai trò
        if (listVaiTro.size() > 0) {
            VaiTro vaiTro = listVaiTro.get(spnRole.getSelectedItemPosition());
            nhanVien.setMaVT(vaiTro.getMaVT());
        }

        // Phòng ban
        if (listPhongBan.size() > 0) {
            PhongBan phongBan = listPhongBan.get(spDepartment.getSelectedItemPosition());
            nhanVien.setMaPB(phongBan.getMaPB());
        }

        // Ghi chú
        nhanVien.setGhiChu(!TextUtils.isEmpty(edtNote.getText().toString()) ? edtNote.getText().toString() : "");

        // Ob tạo nhân viên
        Observable<Boolean> createStaft = Observable.create(r -> {
            try {
                r.onNext(WorkWithDb.getInstance().insert(nhanVien));
                r.onComplete();
            } catch (Exception e) {
                r.onError(e);
            }

        });

        compositeDisposable.add(createStaft.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(r -> {
                    if (r) {
                        CustomToast.showToastSuccesstion(getApplicationContext(), getResources().getString(R.string.add_successful), Toast.LENGTH_SHORT);
                        Intent intent = getIntent();
                        setResult(RESULT_OK, intent);
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

        // Khởi tạo toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.add_staft));

        // Khởi tạo danh vai trò và adapter quản lý
        listVaiTro = new ArrayList();
        selectRoleAdapter = new SelectRoleAdapter(this, listVaiTro);
        spnRole.setAdapter(selectRoleAdapter);

        // Khởi tạo danh sách phòng ban và adapter quản lý
        listPhongBan = new ArrayList();
        selectDepartmentAdapter = new SelectDepartmentAdapter(this, listPhongBan);
        spDepartment.setAdapter(selectDepartmentAdapter);

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

                this.imageStaft = imagePath;

                Picasso.get().load(new File(imagePath)).error(R.mipmap.app_icon).fit().centerInside().into(ivPhoto, new Callback() {
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
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}