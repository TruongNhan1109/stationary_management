package com.ttn.stationarymanagement.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.baseview.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingActivity extends BaseActivity {

    @BindView(R.id.iv_loading_splash)
    ImageView ivLoading;

   private int state = 1;

    Timer timer;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, LoadingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_fragment);
        ButterKnife.bind(this);


        TimerTask loading = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ivLoading.setImageLevel(state++);

                        if (state == 6) {
                            timer.cancel();
                            Intent homeActivity = HomeActivity.getCallingIntent(LoadingActivity.this);
                            startActivity(homeActivity);
                            finish();
                        }
                    }
                });

            }
        };

        timer = new Timer();
        timer.schedule(loading, 250, 350);
        
    }
}