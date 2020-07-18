package com.ttn.stationarymanagement.presentation.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationView;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.baseview.BaseActivity;
import com.ttn.stationarymanagement.presentation.dialog_fragment.SelectLanguageDialog;
import com.ttn.stationarymanagement.presentation.fragment.AllocationFragment;
import com.ttn.stationarymanagement.presentation.fragment.DepartmentManagerFragment;
import com.ttn.stationarymanagement.presentation.fragment.HomeScreenFragment;
import com.ttn.stationarymanagement.presentation.fragment.ProductManagerFragment;
import com.ttn.stationarymanagement.presentation.fragment.RoleManagerFragment;
import com.ttn.stationarymanagement.presentation.fragment.StaftManagerFragment;
import com.ttn.stationarymanagement.presentation.fragment.StatisticFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

public class HomeActivity extends BaseActivity implements  HomeScreenFragment.HomeScreenFragmentListener {

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

        askPermission();


    }

    private void askPermission() {
        askPermisionWriteExternalStorage();
        askPermisionReadExternalStorage();
        askPermisionCamera();
    }

    // Thiết lập màn hình hiển thị mặc định (Home Screen)
    private void initHomeScreen() {

        HomeScreenFragment homeScreenFragment = HomeScreenFragment.newInstance();
        homeScreenFragment.setListener(this);
        addFragment(R.id.content_view, homeScreenFragment);

        getSupportActionBar().setTitle("");
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_black_30));
        toolbar.setNavigationIcon(R.drawable.ic_app_white_24);

    }

    private void setControls() {

        // Thiết lập hiển thị full màn hình
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setSupportActionBar(toolbar);

        // Thiết lập menu navigation drawer
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

    // Các sự kiện khi click vào các item trong navigation drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {

            case R.id.mn_home:  // Home
                changeViewById(FUNTION_HOME);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_allocation:    // Cấp phát
                changeViewById(FUNCTION_ALLOCATION);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_statistic:     // Thống kê
                changeViewById(FUNCTION_STATISTIC);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_staft_manager:     // Quản lý nhân viên
                changeViewById(FUNCTION_STAFT_MANAGER);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_department_manager:        // Phòng ban
                changeViewById(FUNCTION_DEPARTMENT_MANAGER);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_stationary_management:     // Văn phòng phẩm
                changeViewById(FUNCTION_PRODUCT_MANAGER);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_role_manager:          // Vai trò
                changeViewById(FUNCTION_ROLE_MANAGER);
                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_language:       // Chuyển ngôn ngữ

                SelectLanguageDialog selectLanguageDialog = SelectLanguageDialog.newInstance();
                selectLanguageDialog.show(getSupportFragmentManager(), "");

                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_contact:       // Liên hệ

                Intent intent = WebViewActivity.getCallingIntent(this);
                intent.putExtra(WebViewActivity.KEY_LINK, "https://www.google.com/" );
                startActivity(intent);

                drawerLayout.closeDrawers();
                return true;

            case R.id.mn_infor:     // Thông tin ứng dụng

                Intent inforApp = InformationAppActivity.getCallingIntent(this);
                startActivity(inforApp);

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

    // Xử lý view theo từng màn hình được chọn
    private  void changeViewById(int idFunction) {
        switch (idFunction) {

            case FUNTION_HOME:      // Màn hình chính
                HomeScreenFragment homeScreenFragment = HomeScreenFragment.newInstance();
                homeScreenFragment.setListener(this);
                replaceFragment(R.id.content_view,homeScreenFragment, HomeScreenFragment.TAG);

                getSupportActionBar().setTitle("");
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_black_30));
                toolbar.setNavigationIcon(R.drawable.ic_app_white_24);
                break;

            case FUNCTION_ALLOCATION:       // Màn hình cấp phát
                replaceFragment(R.id.content_view, AllocationFragment.newInstance(), "");
                getSupportActionBar().setTitle(getResources().getString(R.string.allocation));
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
                toolbar.setNavigationIcon(R.drawable.ic_app_blue_24);
                break;

            case FUNCTION_STAFT_MANAGER:        // Màn hình quản lý nhân viên
                replaceFragment(R.id.content_view, StaftManagerFragment.newInstance(), "");
                getSupportActionBar().setTitle(getResources().getString(R.string.staft));
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
                toolbar.setNavigationIcon(R.drawable.ic_app_blue_24);
                break;

            case FUNCTION_DEPARTMENT_MANAGER:       // Màn hình quản lý phòng ban
                replaceFragment(R.id.content_view, DepartmentManagerFragment.newInstance(), "");
                getSupportActionBar().setTitle(getResources().getString(R.string.department));
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
                toolbar.setNavigationIcon(R.drawable.ic_app_blue_24);
                break;

            case FUNCTION_PRODUCT_MANAGER:      // Màn hình quản lý văn phòng phẩm
                replaceFragment(R.id.content_view, ProductManagerFragment.newInstance(), "");
                getSupportActionBar().setTitle(getResources().getString(R.string.product));
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
                toolbar.setNavigationIcon(R.drawable.ic_app_blue_24);
                break;

            case FUNCTION_ROLE_MANAGER:     // Màn hình quản lý vai trò
                replaceFragment(R.id.content_view, RoleManagerFragment.newInstance(), "");
                getSupportActionBar().setTitle(getResources().getString(R.string.role));
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
                toolbar.setNavigationIcon(R.drawable.ic_app_blue_24);
                break;

            case FUNCTION_STATISTIC:        // Màn hình thống kê
                replaceFragment(R.id.content_view, StatisticFragment.newInstance(), "");
                getSupportActionBar().setTitle(getResources().getString(R.string.statistic));
                toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
                toolbar.setNavigationIcon(R.drawable.ic_app_blue_24);
                break;
        }

    }

    // Xin người dùng cấp quyền truy cập bộ nhớ trong
    private void askPermisionWriteExternalStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { // Sẽ hiện nếu bị từ chối lần

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage(getResources().getString(R.string.require_write_permission));

            alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(HomeActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            2);

                }
            });

            alertDialog.setNegativeButton(getResources().getString(R.string.no), null);
            alertDialog.show();

        } else { // Hỏi trực tiếp

            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
        }
    }

    // Xin cấp quyền đọc bộ nhớ
    private void askPermisionReadExternalStorage() {

        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

    }

    private void askPermisionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { // Sẽ hiện nếu bị từ chối lần

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage(getResources().getString(R.string.require_camera_permission));

            alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(HomeActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            3);

                }
            });

            alertDialog.setNegativeButton(getResources().getString(R.string.no), null);
            alertDialog.show();

        } else { // Hỏi trực tiếp

            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    3);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
          /*  case 1:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  // Được cấp phép

                    askPermisionWriteExternalStorage();

                } else {        // Bị từ chối


                }
                break;
*/
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onFuntionClick(int idFunction) {
        changeViewById(idFunction);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  // Xử lỹ khi nhấn nút back
            changeViewById(FUNTION_HOME);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}