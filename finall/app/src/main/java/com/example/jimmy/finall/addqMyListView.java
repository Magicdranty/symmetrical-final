package com.example.jimmy.finall;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by jimmy on 2016/5/2.
 */
public class addqMyListView extends ListView {
    private addqMyLinearLayout mCurView;
    public addqMyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //我们想知道当前点击了哪一行
                int position = pointToPosition(x, y);
                if (position != INVALID_POSITION) {
                    addqAdapter.DataHolder data = (addqAdapter.DataHolder) getItemAtPosition(position);
                    mCurView =  data.rootView;
                }
            }
            break;
            default:
                break;
        }
        if (mCurView != null){
            mCurView.disPatchTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }


}
