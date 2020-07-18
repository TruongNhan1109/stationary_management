package com.ttn.stationarymanagement.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.adapter.AppFunctionAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;
import com.ttn.stationarymanagement.presentation.dialog_fragment.SearchDialogFragment;
import com.ttn.stationarymanagement.presentation.model.HomeModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

import static com.ttn.stationarymanagement.data.config.Constants.FUNCTION_ALLOCATION;
import static com.ttn.stationarymanagement.data.config.Constants.FUNCTION_DEPARTMENT_MANAGER;
import static com.ttn.stationarymanagement.data.config.Constants.FUNCTION_PRODUCT_MANAGER;
import static com.ttn.stationarymanagement.data.config.Constants.FUNCTION_ROLE_MANAGER;
import static com.ttn.stationarymanagement.data.config.Constants.FUNCTION_STAFT_MANAGER;
import static com.ttn.stationarymanagement.data.config.Constants.FUNCTION_STATISTIC;

public class HomeScreenFragment extends BaseFragment {

    public static String TAG = HomeScreenFragment.class.getSimpleName();

    List<HomeModel> listFunctions;      // Danh sách chức năng
    AppFunctionAdapter adapterAppFunction;

    @BindView(R.id.rv_fragment_home_screen_list_function)
    RecyclerView rvListFuntion;

    @BindView(R.id.lnl_area_seach)
    LinearLayout lnlSearch;

    public interface HomeScreenFragmentListener {
        void onFuntionClick(int idFunction);
    }

    private HomeScreenFragmentListener mListener;

    public static HomeScreenFragment newInstance() {
        Bundle args = new Bundle();
        HomeScreenFragment fragment = new HomeScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(HomeScreenFragmentListener listener) {
        this.mListener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
       ButterKnife.bind(this, view);
       return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setControls();
        initDataHomeScreen();
        setEvents();
    }

    private void setEvents() {

        // Khi click vào một chức năng
        adapterAppFunction.setListener(position -> {
            if (mListener != null) {
                mListener.onFuntionClick(listFunctions.get(position).getIdSetting());
            }
        });

        // Khi click vào seach
        lnlSearch.setOnClickListener(v -> {

            SearchDialogFragment searchDialogFragment = SearchDialogFragment.newInstance();
            searchDialogFragment.show(getChildFragmentManager(),  "");

        });
    }

    // Khởi tạo các chức năng của chương trình
    private void initDataHomeScreen() {

        // Cấp phát
        HomeModel ALLOCATION = new HomeModel(FUNCTION_ALLOCATION,getResources().getString(R.string.allocation) , R.drawable.ic_sheet_color_32);
        listFunctions.add(ALLOCATION);

        // Thống kê
        HomeModel STATISTIC = new HomeModel(FUNCTION_STATISTIC,getResources().getString(R.string.statistic) , R.drawable.ic_statistic_color_32);
        listFunctions.add(STATISTIC);

        // Nhân viên
        HomeModel STAFT_MANAGER = new HomeModel(FUNCTION_STAFT_MANAGER,getResources().getString(R.string.staft), R.drawable.ic_people_color_32);
        listFunctions.add(STAFT_MANAGER);

        // Phòng ban
        HomeModel DEPARTMENT_MANAGER = new HomeModel(FUNCTION_DEPARTMENT_MANAGER,getResources().getString(R.string.department) , R.drawable.ic_desktop_color_32);
        listFunctions.add(DEPARTMENT_MANAGER);

        // Sản phẩm
        HomeModel PRODUCT_MANAGER = new HomeModel(FUNCTION_PRODUCT_MANAGER,getResources().getString(R.string.stationery) , R.drawable.ic_printer_color_32);
        listFunctions.add(PRODUCT_MANAGER);

        // Vai trò
        HomeModel ROLE_MANAGER = new HomeModel(FUNCTION_ROLE_MANAGER,getResources().getString(R.string.role) , R.drawable.ic_task_color_32);
        listFunctions.add(ROLE_MANAGER);

        adapterAppFunction.notifyDataSetChanged();

    }

    private void setControls() {

        // Khởi tạo adapter và danh sách chức năng
        listFunctions = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new androidx.recyclerview.widget.GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);

        rvListFuntion.setLayoutManager(gridLayoutManager);

        adapterAppFunction = new AppFunctionAdapter(getContext(), listFunctions);
        rvListFuntion.setHasFixedSize(true);

        rvListFuntion.setAdapter( new ScaleInAnimationAdapter(adapterAppFunction));


    }
}
