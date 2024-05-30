package com.example.kinmel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatEditText;

public class CustomEditText extends AppCompatEditText {

    private OnDrawableClickListener listener;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (listener != null) {
                if (event.getRawX() >= (getRight() - getCompoundDrawables()[2].getBounds().width())) {
                    listener.onDrawableClick();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setOnDrawableClickListener(OnDrawableClickListener listener) {
        this.listener = listener;
    }

    public interface OnDrawableClickListener {
        void onDrawableClick();
    }
}
