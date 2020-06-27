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
public class MButtonAction extends Button {

    float textSize = 0;
    int textColor = 0;

    public MButtonAction(Context context) {
        super(context);
        initView();
    }

    public MButtonAction(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttreibute(attrs);
        initView();

    }

    public MButtonAction(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttreibute(attrs);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MButtonAction(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getAttreibute(attrs);
        initView();
    }

    public void initView() {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize != 0 ? textSize : getResources().getDimension(R.dimen.button_text_size));
        setTextColor(textColor != 0 ? textColor : Color.WHITE);
        // setTypeface(this.getTypeface(), Typeface.BOLD);

    }

    private void getAttreibute(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.MButtonAction, 0, 0);
        try {
            textSize = a.getDimension(R.styleable.MButtonAction_setButtonActionTextSize, textSize);
            textColor = a.getColor(R.styleable.MButtonAction_setButtonActionTextColor, textColor);
        } finally {
            a.recycle();
        }

    }


}
