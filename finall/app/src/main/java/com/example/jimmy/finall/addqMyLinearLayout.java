package com.example.jimmy.finall;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by jimmy on 2016/5/2.
 */
public class addqMyLinearLayout extends LinearLayout {
        private int mlastX = 0;
        private final int MAX_WIDTH =200;
        private Context mContext;
        private Scroller mScroller;
        public addqMyLinearLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            mContext = context;
            mScroller = new Scroller(context, new LinearInterpolator(context, null));
        }
        private OnScrollListener mScrollListener;

        public static interface OnScrollListener {
            public void OnScroll(addqMyLinearLayout view);
        }
        public void disPatchTouchEvent(MotionEvent event){//由listview接收touchevent 再到父元件linearlayout集中處理
            int maxLength = dipToPx(mContext, MAX_WIDTH);
            int x = (int) event.getX();
            int y = (int) event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                }
                break;
                case MotionEvent.ACTION_MOVE: {//計算滑動並滑動
                    int scrollX = this.getScrollX();//ScrollX 類似滑動距離 是視角移動的Ｘ坐標
                    int newScrollX = scrollX + mlastX - x;
                    if (newScrollX < 0) {

                        newScrollX = 0;
                    } else if (newScrollX > maxLength) {

                        newScrollX = maxLength;

                    }

                    this.scrollTo(newScrollX, 0);
                }
                break;
                case MotionEvent.ACTION_UP: {
                    int scrollX = this.getScrollX();
                    int newScrollX = scrollX + mlastX - x;
                    if(scrollX > maxLength / 4 && scrollX < maxLength / 2)
                    {
                        newScrollX = maxLength/2;
                        mScrollListener.OnScroll(this);
                    }else if(scrollX > maxLength / 2 && scrollX <maxLength*3/4 )
                    {
                        newScrollX = maxLength/2;
                        mScrollListener.OnScroll(this);
                    }
                    else if(scrollX > maxLength / 2 )
                    {
                        newScrollX = maxLength;
                        mScrollListener.OnScroll(this);
                    }else {
                        newScrollX = 0;
                    }
                    mScroller.startScroll(scrollX, 0, newScrollX - scrollX, 0);
                    invalidate();
                }
                break;
            }
            mlastX = x;
        }
        @Override
        public void computeScroll() {//調用scrollto會自己使用
            if (mScroller.computeScrollOffset()) {
                this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            }
            invalidate();//刷新view 系統會自動調用 View的onDraw()方法。
        }
        private int dipToPx(Context context, int dip) {
            return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
        }
        //设置监听器
        public void setOnScrollListener(OnScrollListener scrollListener) {
            mScrollListener = scrollListener;
        }
        //緩慢將item移動到目的地/將上一個view緩慢收回時調用
        public void smoothScrollTo(int destX, int destY) {
            int scrollX = getScrollX();
            int delta = destX - scrollX;
            mScroller.startScroll(scrollX, 0, delta, 0);
            invalidate();
        }
    }


