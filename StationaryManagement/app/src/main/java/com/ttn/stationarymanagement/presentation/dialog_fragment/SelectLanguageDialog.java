package com.ttn.stationarymanagement.presentation.dialog_fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.activity.SplashScreenActivity;
import com.ttn.stationarymanagement.utils.LanguageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectLanguageDialog extends DialogFragment {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    @BindView(R.id.rdg_select_language)
    RadioGroup rdgLanguage;

    @BindView(R.id.btn_done)
    Button btnDone;

    public static SelectLanguageDialog newInstance() {
        SelectLanguageDialog fragment = new SelectLanguageDialog();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_language, container, false);
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

        tvCancel.setOnClickListener(v -> dismiss());

        btnDone.setOnClickListener(v -> {

            switch(rdgLanguage.getCheckedRadioButtonId()) {

                case R.id.rdo_vietnamese:   // Tiếng viêt
                    LanguageUtils.changeLanguage(getResources().getString(R.string.language_vietnamese_code), getContext());
                    break;

                case R.id.rdo_english:
                    LanguageUtils.changeLanguage(getResources().getString(R.string.language_english_code), getContext());
                    break;
            }

            Intent i = new Intent(getContext(), SplashScreenActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);


        });
    }
}
