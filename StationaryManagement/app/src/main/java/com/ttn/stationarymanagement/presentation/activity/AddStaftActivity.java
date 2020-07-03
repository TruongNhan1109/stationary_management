package com.ttn.stationarymanagement.presentation.activity;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.File;
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

public class AddStaftActivity extends BaseActivity {

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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        edtDateOfBirth.setText(simpleDateFormat.format(currentDate));

        getAllData();


    }

    private void getAllData() {

        Observable<List<VaiTro>> getDataRole = Observable.create(r -> {

            try {
                r.onNext(WorkWithDb.getInstance().getAllRole());
                r.onComplete();

            } catch (Exception e) {
              r.onError(e);
            }

        });

        compositeDisposable.add(getDataRole.subscribeOn(Schedulers.newThread()).flatMap(r -> {
            listVaiTro.addAll(r);
           return Observable.just(WorkWithDb.getInstance().getAllDepartment());

        }).observeOn(AndroidSchedulers.mainThread()).subscribe(r ->{

            listPhongBan.addAll(r);

            selectRoleAdapter.notifyDataSetChanged();
            selectDepartmentAdapter.notifyDataSetChanged();
        }));

    }


    private void setEvents() {

        ivPhoto.setOnClickListener(v -> {

            Intent intent = new Intent();
            // Chỉ định kiểu file cần hiển thị
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            //Hiển thị các ứng dụng có thể xử lý ảnh
            startActivityForResult(Intent.createChooser(intent, "Chọn ảnh của ban"), requestSelectPhoto);

        });

        edtDateOfBirth.setOnClickListener(v -> {
            Calendar calendarBirthday = Calendar.getInstance();

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
        });


        btnAdd.setOnClickListener(v -> {

            if (TextUtils.isEmpty(edtNameStaft.getText().toString())) {

                new ShakeAnimator().setDuration(700).setRepeatTimes(0).setTarget(edtNameStaft).start();
                edtNameStaft.setError("Tên nhân viên không được để trống");
                edtNameStaft.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                new ShakeAnimator().setDuration(700).setRepeatTimes(0).setTarget(edtEmail).start();
                edtEmail.setError("Email không được để trống");
                edtEmail.requestFocus();
                return;
            }

            if (!AppUtils.checkValidEmail(edtEmail.getText().toString())) {
                new ShakeAnimator().setDuration(700).setRepeatTimes(0).setTarget(edtEmail).start();
                edtEmail.setError("Sai cấu trúc email");
                edtEmail.requestFocus();
            }


            createNewStaft();



        });



    }

    private void createNewStaft() {

    


    }

    private void setControls() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm nhân viên");

        listVaiTro = new ArrayList();
        compositeDisposable = new CompositeDisposable();

        selectRoleAdapter = new SelectRoleAdapter(this, listVaiTro);
        spnRole.setAdapter(selectRoleAdapter);

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
                CustomToast.showToastError(getApplicationContext(), "Chọn ảnh thất bại", Toast.LENGTH_SHORT);
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
}