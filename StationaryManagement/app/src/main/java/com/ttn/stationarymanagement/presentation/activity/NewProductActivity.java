package com.ttn.stationarymanagement.presentation.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.baseview.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewProductActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
    }

    private void setControls() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm sản phẩm");
    }


}