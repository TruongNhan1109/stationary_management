package com.ttn.stationarymanagement.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.activity.AddStaftActivity;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;

public class StaftManagerFragment extends BaseFragment {


    public static StaftManagerFragment newInstance() {
        Bundle args = new Bundle();
        StaftManagerFragment fragment = new StaftManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_staft_manager, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_staft_manager, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.mn_staft_manager_add:

                Intent intent = AddStaftActivity.getCallingIntent(getContext());
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
