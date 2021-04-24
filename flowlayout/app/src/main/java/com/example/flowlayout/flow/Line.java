package com.example.flowlayout.flow;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Line {
    int mWidth = 0;
    int mHeight = 0;
    List<View> views = new ArrayList<View>();

    public void addView(View view) {
        views.add(view);
        mWidth += view.getMinimumWidth();
        int ch = view.getMeasuredHeight();
        mHeight = mHeight < ch ? ch : mHeight;
    }

    protected int getViewCount() {
        return views.size();
    }

    public void layout(int l, int t, int getMeasuredWidth, int getPaddingLeft, int getPaddingRight, int mHorizontalSpacing,int mVercalSpacing) {
        int left = l;
        int top = t;
        int count = getViewCount();
        int layoutWidth = getMeasuredWidth - getPaddingLeft - getPaddingRight;
        int surplusWidth = layoutWidth - mWidth - mHorizontalSpacing * (count - 1);
        if (surplusWidth >= 0) {
            for (int i = 0; i < count; i++) {
                final View view = views.get(i);
                int childWidth = view.getMeasuredWidth();
                int childHeight = view.getMeasuredHeight();
                int topOffset = (int) ((mHeight - childHeight) / 2.0 + 0.5);
                if (topOffset < 0) {
                    topOffset = 0;
                }

                view.layout(left, top + topOffset, left + childWidth, top + topOffset + childHeight);
                left += childWidth + mVercalSpacing;
            }


        }

    }

}
