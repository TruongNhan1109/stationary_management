package com.ttn.stationarymanagement.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.PhongBan;
import com.ttn.stationarymanagement.data.local.model.VaiTro;
import com.ttn.stationarymanagement.presentation.adapter.DepartmentAdapter;
import com.ttn.stationarymanagement.presentation.adapter.RoleAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;
import com.ttn.stationarymanagement.presentation.dialog_fragment.AddDepartmentDialog;
import com.ttn.stationarymanagement.presentation.dialog_fragment.AddRoleDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DepartmentManagerFragment extends BaseFragment {

    @BindView(R.id.tv_fragment_role_manager_notify_empty)
    TextView tvNotifyEmpty;

    @BindView(R.id.rv_fragment_role_manager_list_role)
    RecyclerView rvListDepartment;

    private List<PhongBan> listDepartment;
    private DepartmentAdapter adapterDepartment;

    public static DepartmentManagerFragment newInstance() {
        Bundle args = new Bundle();
        DepartmentManagerFragment fragment = new DepartmentManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department_manager, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        setControls();
        getDataAndSetView();
    }

    private void getDataAndSetView() {

        PhongBan phongBan = new PhongBan("Phong 1", "Ghi chú");
        listDepartment.add(phongBan);
        listDepartment.add(phongBan);
        listDepartment.add(phongBan);
        listDepartment.add(phongBan);
        adapterDepartment.notifyDataSetChanged();

    }

    private void setControls() {
        listDepartment = new ArrayList<>();
        adapterDepartment = new DepartmentAdapter(getContext(), listDepartment);

        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        rvListDepartment.setLayoutManager(linearLayoutManager);
        rvListDepartment.setAdapter(adapterDepartment);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_department, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_department_manager_add:
                AddDepartmentDialog addDepartmentDialog = AddDepartmentDialog.newInstance();
                addDepartmentDialog.show(getChildFragmentManager(), "");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
