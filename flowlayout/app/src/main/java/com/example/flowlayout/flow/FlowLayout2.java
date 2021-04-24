package com.example.flowlayout.flow;

import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flowlayout.FlowLayout1;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class FlowLayout2 extends ViewGroup {
    private int isAlignByCenter = 1;

    public FlowLayout2(Context context) {
        super(context);
    }

    public FlowLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private Line mline = null;
    private int mUsedWidth = 0;
    private final int DEFAULT_SPACING = 10;
    //横纵间距
    private int mHorizontalSpacing = DEFAULT_SPACING;
    private int mVerticalSacing = DEFAULT_SPACING;
    //行集合
    private final List<Line> mLines = new ArrayList<Line>();
    private int mMaxLineCount = 5;
    private SparseArray<View> mViews;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int left = getPaddingLeft();
            int top = getPaddingTop();
            int count = mLines.size();
            for (int i = 0; i < count; i++) {
                Line line = mLines.get(i);
                line.layout(left, top,getMeasuredWidth(),getPaddingLeft(),getPaddingRight(),mHorizontalSpacing,mVerticalSacing);
                top += line.mHeight + mVerticalSacing;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight() - getPaddingLeft();
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        restoreLine();
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            int cwm = MeasureSpec.makeMeasureSpec(sizeWidth, modeWidth == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeWidth);
            int chm = MeasureSpec.makeMeasureSpec(sizeHeight, modeHeight == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeHeight);
            child.measure(cwm, chm);

            if (mline == null) {
                mline = new Line();
            }

            int cw = child.getMeasuredWidth();
            mUsedWidth += cw;
            if (mUsedWidth <= sizeWidth) {
                mline.addView(child);
                mUsedWidth += mHorizontalSpacing;
                if (mUsedWidth >= sizeWidth) {
                    if (!newLine()) {
                        break;
                    }
                }
            } else {
                if (mline.getViewCount() == 0) {
                    mline.addView(child);
                    if (!newLine()) {
                        break;
                    }
                } else {
                    if (!newLine()) {
                        break;
                    }
                    mline.addView(child);
                    mUsedWidth += cw + mHorizontalSpacing;
                }
            }

    }
        if(mline !=null&&mline.getViewCount()>0&&!mLines.contains(mline))

    {
        mLines.add(mline);
    }

    int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
    int totalHeight = 0;
    final int linesCount = mLines.size();
        for(
    int k = 0;
    k<linesCount;k++)

    {
        totalHeight += mLines.get(k).mHeight;
    }

    totalHeight +=mVerticalSacing *(linesCount -1);
    totalHeight +=

    getPaddingTop() +

    getPaddingBottom();

    setMeasuredDimension(totalWidth, resolveSize(totalHeight, heightMeasureSpec));

}

    private void restoreLine() {
        mLines.clear();
        mline = new Line();
        mUsedWidth = 0;
    }

    private boolean newLine() {
        mLines.add(mline);
        if (mLines.size() < mMaxLineCount) {
            mline = new Line();
            mUsedWidth = 0;
            return true;
        }
        return false;
    }

    public void setHorizontalSpacing(int spacing) {
        if (mHorizontalSpacing != spacing) {
            mHorizontalSpacing = spacing;
            requestLayoutInner();
        }
    }

    public void setVerticalSpacing(int spacing) {
        if (mVerticalSacing != spacing) {
            mVerticalSacing = spacing;
            requestLayoutInner();
        }
    }

    public void setAdapter(List<?> list, int res, ItemView mItemView) {
        if (list == null) {
            return;
        }
        removeAllViews();
        int layoutPadding = dipToPx(getContext(), 8);
        setHorizontalSpacing(layoutPadding);
        setVerticalSpacing(layoutPadding);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Object item = list.get(i);
            View inflate = LayoutInflater.from(getContext()).inflate(res, null);
            mItemView.getCover(item, new ViewHolder(inflate), inflate, i);
            addView(inflate);
        }
    }

public abstract static class ItemView<T> {
    protected abstract void getCover(T item, ViewHolder holder, View inflate, int position);
}

public class ViewHolder {
    View mConvertView;

    public ViewHolder(View mConvertView) {
        this.mConvertView = mConvertView;
        mViews = new SparseArray<>();
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        try {
            return (T) view;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
    }
}

    public static int dipToPx(Context ctx, float dip) {
        return (int) TypedValue.applyDimension(1, dip, ctx.getResources().getDisplayMetrics());
    }

public interface AlienState {
    int RIGHT = 0;
    int LEFT = 1;
    int CENTER = 2;

}

    public void setAlignByCenter(int isAlignByCenter) {
        this.isAlignByCenter = isAlignByCenter;
        requestLayoutInner();
    }

    private void requestLayoutInner() {
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }



}
