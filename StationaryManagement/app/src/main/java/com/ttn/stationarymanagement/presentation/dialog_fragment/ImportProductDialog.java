package com.ttn.stationarymanagement.presentation.dialog_fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.daimajia.androidanimations.library.attention.ShakeAnimator;
import com.ttn.stationarymanagement.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImportProductDialog  extends DialogFragment {

    @BindView(R.id.tv_close)
    TextView tvClose;

    @BindView(R.id.btn_import_product)
    Button btnImport;

    @BindView(R.id.edt_amount)
    EditText edtAmount;

    public static ImportProductDialog newInstance() {
        ImportProductDialog fragment = new ImportProductDialog();
        return fragment;
    }

    public interface OnImportProductListener {
        void onImportProduct(int amount);
    }

   private  OnImportProductListener mListener;

    public void setListener(OnImportProductListener listener) {
        this.mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_import_product, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setEvents();
    }

    private void setEvents() {

        tvClose.setOnClickListener(v -> {
            dismiss();
        });

        btnImport.setOnClickListener(v -> {

            if (TextUtils.isEmpty(edtAmount.getText().toString())) {

                new ShakeAnimator().setDuration(700).setRepeatTimes(0).setTarget(edtAmount).start();
                edtAmount.setError("Vui lòng nhập số lượng nhập");
                edtAmount.requestFocus();
                return;
            }

            Integer amount = null;

            try {
                amount = Integer.parseInt(edtAmount.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (amount != null && mListener != null) {
                mListener.onImportProduct(amount);
                dismiss();
            }

        });


    }
}
