package com.ttn.stationarymanagement.presentation.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.baseview.BaseActivity;

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
    EditText edtDateOfBirth;

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

    }

    private void setControls() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm nhân viên");

    }
}