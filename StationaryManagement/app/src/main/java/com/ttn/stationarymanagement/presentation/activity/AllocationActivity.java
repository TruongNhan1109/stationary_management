package com.ttn.stationarymanagement.presentation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.baseview.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AllocationActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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


    }

    private void setControls() {

        compositeDisposable = new CompositeDisposable();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cấp phát");
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