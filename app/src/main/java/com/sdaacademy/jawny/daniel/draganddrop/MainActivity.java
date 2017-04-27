package com.sdaacademy.jawny.daniel.draganddrop;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.myimage1)
    ImageView mImage1;

    @BindView(R.id.myimage2)
    ImageView mImage2;

    @BindView(R.id.myimage3)
    ImageView mImage3;

    @BindView(R.id.myimage4)
    ImageView mImage4;

    @BindView(R.id.topleft)
    LinearLayout mTopLeft;

    @BindView(R.id.topright)
    LinearLayout mTopRight;

    @BindView(R.id.bottomleft)
    LinearLayout mBottomLeft;

    @BindView(R.id.bottomright)
    LinearLayout mBottomRight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setListeners();
    }

    private void setListeners() {
        mImage1.setOnTouchListener(new MyDragTouchListener());
        mImage2.setOnTouchListener(new MyDragTouchListener());
        mImage3.setOnTouchListener(new MyDragTouchListener());
        mImage4.setOnTouchListener(new MyDragTouchListener());
        mTopLeft.setOnDragListener(new MyDropListener());
        mTopRight.setOnDragListener(new MyDropListener());
        mBottomLeft.setOnDragListener(new MyDropListener());
        mBottomRight.setOnDragListener(new MyDropListener());
    }

    private final class MyDragTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                } else {
                    view.startDrag(data, shadowBuilder, view, 0);
                }
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private class MyDropListener implements View.OnDragListener {
        Drawable enterShape = ContextCompat.getDrawable(MainActivity.this, R.drawable.shape_droptarget);
        Drawable normalShape = ContextCompat.getDrawable(MainActivity.this, R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // nothing to do here...
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    setDrawable(v, enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    setDrawable(v, normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    setDrawable(v, normalShape);
                default:
                    break;
            }
            return true;
        }

        private void setDrawable(View v, Drawable drawable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                v.setBackground(drawable);
            } else {
                v.setBackgroundDrawable(drawable);
            }
        }
    }
}
