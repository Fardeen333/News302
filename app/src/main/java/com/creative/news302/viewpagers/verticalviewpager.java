package com.creative.news302.viewpagers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

import androidx.viewpager.widget.ViewPager;

public class verticalviewpager extends ViewPager {
    public verticalviewpager(Context context) {
        super(context);
        init();
    }

    public verticalviewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setPageTransformer(true, new VerticalPageTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }
    public void setDurationScroll(int millis) {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new OwnScroller(getContext(), millis));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class OwnScroller extends Scroller {

        private int durationScrollMillis = 1;

        public OwnScroller(Context context, int durationScroll) {
            super(context, new DecelerateInterpolator());
            this.durationScrollMillis = durationScroll;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, durationScrollMillis);
        }
    }
    private class VerticalPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;

        @Override
        public void transformPage(View view, float position) {

            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 1) {
                view.setAlpha(1);
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                //view.setScaleX(scaleFactor);
               // view.setScaleY(scaleFactor);

                view.setTranslationX(view.getWidth() * -position);

                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);
            }else if(position<=0){
               // view.setScaleX(1f);
                //view.setScaleY(1f);
            } else {
                view.setAlpha(0);
            }
        }
    }

    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev);
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapXY(ev));
    }

}