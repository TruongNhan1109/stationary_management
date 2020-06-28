package com.ttn.stationarymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ttn.stationarymanagement.presentation.activity.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_hello)
    TextView tvHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Intent intent = HomeActivity.getCallingIntent(this);
        startActivity(intent);

        tvHello.setOnClickListener(v -> {
           // Toast.makeText(this, "Hello world", Toast.LENGTH_SHORT).show();

            Observable.just("One", "Two", "Therre").subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            });

        });
    }
}