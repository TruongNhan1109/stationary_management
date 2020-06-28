package com.ttn.stationarymanagement.presentation.activity.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationView;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.baseview.BaseActivity;
import com.ttn.stationarymanagement.presentation.fragment.AllocationFragment;
import com.ttn.stationarymanagement.presentation.fragment.DepartmentManagerFragment;
import com.ttn.stationarymanagement.presentation.fragment.HomeScreenFragment;
import com.ttn.stationarymanagement.presentation.fragment.RoleManagerFragment;
import com.ttn.stationarymanagement.presentation.fragment.StaftManagerFragment;
import com.ttn.stationarymanagement.presentation.fragment.StationaryManagerFragment;
import com.ttn.stationarymanagement.presentation.fragment.StatisticFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ttn.stationarymanagement.data.config.Constants.FUNCTION_ALLOCATION;
import static com.ttn.stationarymanagement.data.config.Constants.FUNCTION_DEPARTMENT_MANAGER;
import static com.ttn.stationarymanagement.data.config.Constants.FUNCTION_PRODUCT_MANAGER;
import static com.ttn.stationarymanagement.data.config.Constants.FUNCTION_ROLE_MANAGER;
import static com.ttn.stationarymanagement.data.config.Constants.FUNCTION_STAFT_MANAGER;
import static com.ttn.stationarymanagement.data.config.Constants.FUNCTION_STATISTIC;
import static com.ttn.stationarymanagement.data.config.Constants.FUNTION_HOME;

public class HomeActivity extends BaseActivity {

    private boolean isSetIcon = true;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setControls();
        initHomeScreen();

    }

    private void initHomeScreen() {
        addFragment(R.id.content_view, HomeScreenFragment.newInstance());
        getSupportActionBar().setTitle("");
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_black_30));
        toolbar.setNavigationIcon(R.drawable.ic_app_white_24);

    }

    private void setControls() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setSupportActionBar(toolbar);

        setupNavigationDrawer();
    }


    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private ActionBarDrawerToggle drawerToggle;

    private void setupNavigationDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this::onOptionsItemSelected);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_app_white_24);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {

            case R.id.mn_home:
               changeViewById(FUNTION_HOME);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_allocation:
                changeViewById(FUNCTION_ALLOCATION);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_statistic:
                changeViewById(FUNCTION_STATISTIC);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_staft_manager:
                changeViewById(FUNCTION_STAFT_MANAGER);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_department_manager:
                changeViewById(FUNCTION_DEPARTMENT_MANAGER);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_stationary_management:
                changeViewById(FUNCTION_PRODUCT_MANAGER);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_role_manager:
                changeViewById(FUNCTION_ROLE_MANAGER);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_contact:
                drawerLayout.closeDrawers();
                return true;
            case R.id.mn_infor:
                drawerLayout.closeDrawers();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.app_main_drawer, menu);*/
        if (isSetIcon) {
            toolbar.setNavigationIcon(R.drawable.ic_app_white_24);
            isSetIcon = false;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private  void changeViewById(int idFunction) {
        switch (idFunction) {

            case FUNTION_HOME:
                replaceFragment(R.id.content_view, HomeScreenFragment.newInstance(), "");
                getSupportActionBar().setTitle("");
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_black_30));
                toolbar.setNavigationIcon(R.drawable.ic_app_white_24);
                break;

            case FUNCTION_ALLOCATION:
                replaceFragment(R.id.content_view, AllocationFragment.newInstance(), "");
                getSupportActionBar().setTitle("Cấp phát");
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
                toolbar.setNavigationIcon(R.drawable.ic_app_blue_24);
                break;

            case FUNCTION_STAFT_MANAGER:
                replaceFragment(R.id.content_view, StaftManagerFragment.newInstance(), "");
                getSupportActionBar().setTitle("Nhân viên");
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
                toolbar.setNavigationIcon(R.drawable.ic_app_blue_24);
                break;

            case FUNCTION_DEPARTMENT_MANAGER:
                replaceFragment(R.id.content_view, DepartmentManagerFragment.newInstance(), "");
                getSupportActionBar().setTitle("Phòng ban");
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
                toolbar.setNavigationIcon(R.drawable.ic_app_blue_24);
                break;

            case FUNCTION_PRODUCT_MANAGER:
                replaceFragment(R.id.content_view, StationaryManagerFragment.newInstance(), "");
                getSupportActionBar().setTitle("Sản phẩm");
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
                toolbar.setNavigationIcon(R.drawable.ic_app_blue_24);
                break;

            case FUNCTION_ROLE_MANAGER:
                replaceFragment(R.id.content_view, RoleManagerFragment.newInstance(), "");
                getSupportActionBar().setTitle("Vai trò");
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
                toolbar.setNavigationIcon(R.drawable.ic_app_blue_24);
                break;

            case FUNCTION_STATISTIC:
                replaceFragment(R.id.content_view, StatisticFragment.newInstance(), "");
                getSupportActionBar().setTitle("Thống kê");
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
                toolbar.setNavigationIcon(R.drawable.ic_app_blue_24);
                break;
        }

    }


}