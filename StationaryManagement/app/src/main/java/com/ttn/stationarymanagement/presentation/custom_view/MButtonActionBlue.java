package com.ttn.stationarymanagement.presentation.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.ttn.stationarymanagement.R;

@SuppressLint("AppCompatCustomView")
public class MButtonActionBlue extends Button {

    public MButtonActionBlue(Context context) {
        super(context);
        initView();
    }

    public MButtonActionBlue(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MButtonActionBlue(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MButtonActionBlue(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    public void initView() {

        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        setTextColor(Color.WHITE);
        setAllCaps(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(getResources().getDrawable(R.drawable.state_background_button_blue));
        }

    }
}
