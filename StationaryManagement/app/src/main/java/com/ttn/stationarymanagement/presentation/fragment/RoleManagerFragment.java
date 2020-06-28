package com.ttn.stationarymanagement.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.VaiTro;
import com.ttn.stationarymanagement.presentation.adapter.RoleAdapter;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;
import com.ttn.stationarymanagement.presentation.dialog_fragment.AddRoleDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoleManagerFragment extends BaseFragment {

    @BindView(R.id.tv_fragment_role_manager_notify_empty)
    TextView tvNotifyEmpty;

    @BindView(R.id.rv_fragment_role_manager_list_role)
    RecyclerView rvListRole;

   private List<VaiTro> listRole;
   private RoleAdapter adapterRole;


    public static RoleManagerFragment newInstance() {
        Bundle args = new Bundle();
        RoleManagerFragment fragment = new RoleManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_role_manager, container, false);
        ButterKnife.bind(this, view);
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        setControls();
        getDataAndShowView();
    }

    private void getDataAndShowView() {

        VaiTro vaiTro = new VaiTro("Vai trò 1");
        listRole.add(vaiTro);
        listRole.add(vaiTro);
        listRole.add(vaiTro);
        listRole.add(vaiTro);

        adapterRole.notifyDataSetChanged();
    }

    private void setControls() {

        listRole = new ArrayList<>();
        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapterRole = new RoleAdapter(getContext(), listRole);
        rvListRole.setLayoutManager(linearLayoutManager);
        rvListRole.setAdapter(adapterRole);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_role, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_role_add:

                AddRoleDialog addRoleDialog = AddRoleDialog.newInstance();
                addRoleDialog.show(getChildFragmentManager(), "");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
