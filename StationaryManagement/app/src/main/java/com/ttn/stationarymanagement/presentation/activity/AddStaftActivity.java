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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.baseview.BaseActivity;
import com.ttn.stationarymanagement.utils.CustomToast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.btn_activity_add_staft_add)
    Button btnAdd;

    private String imageStaft = "";

    private int requestSelectPhoto = 1;

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
        setEvents();

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



    }

    private void setControls() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm nhân viên");

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