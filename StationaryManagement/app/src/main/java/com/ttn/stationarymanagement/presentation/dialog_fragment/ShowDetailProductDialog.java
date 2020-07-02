package com.ttn.stationarymanagement.presentation.dialog_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.baseview.FullScreenDialog;

public class ShowDetailProductDialog extends FullScreenDialog {

    public static ShowDetailProductDialog newInstance() {
        Bundle args = new Bundle();
        ShowDetailProductDialog fragment = new ShowDetailProductDialog();
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_show_detail_product, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
