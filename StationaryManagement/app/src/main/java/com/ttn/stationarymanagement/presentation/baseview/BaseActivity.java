package com.ttn.stationarymanagement.presentation.baseview;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyApp", "onActivityStarted: " + this.getLocalClassName());
    }


    protected void setupToolbar(Toolbar toolbar, int idSrcString, String... titles) {

        setSupportActionBar(toolbar);
        if (null != titles && titles.length > 0) {
            getSupportActionBar().setTitle(titles[0]);
        } else {
            getSupportActionBar().setTitle(idSrcString);
        }

     //   toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    /*
    // Add Fragment
     */

    public void addFragment(int containerViewId, Fragment fragment) {
        if(getSupportFragmentManager().isDestroyed()) {
            return;
        }
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.add(containerViewId, fragment);
        transaction.commit();
    }

    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        if(getSupportFragmentManager().isDestroyed()) {
            return;
        }
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.add(containerViewId, fragment, tag);
        transaction.commit();
    }

    protected void addFragmentHaveBack(int containerViewId, Fragment fragment, String tag) {
        if(getSupportFragmentManager().isDestroyed()) {
            return;
        }
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.add(containerViewId, fragment, tag);
        transaction.addToBackStack(tag).commit();
    }

    /*
    // Replace Fragment
     */

    protected void replaceFragment(int containerViewId, Fragment fragment, String TAG) {
        if(getSupportFragmentManager().isDestroyed()) {
            return;
        }
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragment, TAG);
        try {
            transaction.commitAllowingStateLoss();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void replaceFragmentHaveBack(int containerViewId, Fragment fragment, String TAG) {
        if(getSupportFragmentManager().isDestroyed()) {
            return;
        }
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragment, TAG).addToBackStack(TAG);
        try {
            transaction.commitAllowingStateLoss();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    // Remove Fragment
     */

    protected void removeFragment(Fragment fragment){

        if(getSupportFragmentManager().isDestroyed()) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

        try {
            ft.commit();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void removeFragment(int idContentView){

        if(getSupportFragmentManager().isDestroyed()) {
            return;
        }
        if (getSupportFragmentManager().findFragmentById(idContentView) != null) {

            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(idContentView);
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);

            try {
                ft.commit();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            finish();
        }
    }

    protected  Fragment getFragmentByTag(String tag) {

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        return fragment;

    }

}
