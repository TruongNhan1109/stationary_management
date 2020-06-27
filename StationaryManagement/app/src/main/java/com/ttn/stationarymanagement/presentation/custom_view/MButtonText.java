package com.ttn.stationarymanagement.presentation.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.ttn.stationarymanagement.R;


@SuppressLint("AppCompatCustomView")
public class MButtonText  extends Button {


    float textSize = 0;
    int textColor = 0;

    public MButtonText(Context context) {
        super(context);
        initView();
    }

    public MButtonText(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttreibute(attrs);
        initView();

    }

    public MButtonText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttreibute(attrs);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MButtonText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getAttreibute(attrs);
        initView();
    }

    public void initView() {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize != 0 ?  textSize  : getResources().getDimension(R.dimen.button_text_size));
        setBackgroundColor(Color.TRANSPARENT);
    }

    private void getAttreibute(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.MButtonText, 0, 0);
        try {
            textSize = a.getDimension(R.styleable.MButtonText_setButtonTextTextSize, textSize);
        } finally {
            a.recycle();
        }
    }

}