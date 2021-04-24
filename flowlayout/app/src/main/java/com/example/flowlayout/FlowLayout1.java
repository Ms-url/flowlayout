package com.example.flowlayout;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout1 extends ViewGroup {
    private Line mLine = null;
    public static final int DEFAULT_SPACING = 20;
    private SparseArray<View> mViews;//所有的子控件
    private int mHorizontalSpacing = DEFAULT_SPACING;
    private int mVerticalSpacing = DEFAULT_SPACING;
    private int mUsedWidth = 0;
    private final List<Line> mLines = new ArrayList<Line>();
    private int isAlignByCenter = 1;
    private int mMaxLinesCount = Integer.MAX_VALUE;

    public FlowLayout1(Context context) {
        this(context, null);
    }

    public FlowLayout1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface AlienState {
        int RIGHT = 0;
        int LEFT = 1;
        int CENTER = 2;

    }

    public void setAlignByCenter( int isAlignByCenter) {
        this.isAlignByCenter = isAlignByCenter;
        requestLayoutInner();
    }

    private void requestLayoutInner() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    public static int dipToPx(Context ctx, float dip) {
        return (int) TypedValue.applyDimension(1, dip, ctx.getResources().getDisplayMetrics());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int left = getPaddingLeft();
            int top = getPaddingTop();
            int count = mLines.size();
            for (int i = 0; i < count; i++) {
                Line line = mLines.get(i);
                line.LayoutView(left, top);
                top += line.mHeight + mVerticalSpacing;
            }
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight() - getPaddingLeft();
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        restoreLine();
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidth, modeWidth == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeWidth);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeHeight, modeHeight == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeHeight);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            if (mLine == null) {
                mLine = new Line();
            }
            int childWidth = child.getMeasuredWidth();
            mUsedWidth += childWidth;
            if (mUsedWidth <= sizeWidth) {
                mLine.addView(child);
                mUsedWidth += mHorizontalSpacing;
                if (mUsedWidth >= sizeWidth) {
                    if (!newLine()) {
                        break;
                    }
                }
            } else {
                if (mLine.getViewCount() == 0) {
                    mLine.addView(child);
                    if (!newLine()) {
                        break;
                    }
                } else {
                    if (!newLine()) {
                        break;
                    }
                    mLine.addView(child);
                    mUsedWidth += childWidth + mHorizontalSpacing;
                }
            }
        }

        if (mLine != null && mLine.getViewCount() > 0 && !mLines.contains(mLine)) {
            mLines.add(mLine);
        }

        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = 0;
        final int linesCount = mLines.size();
        for (int i = 0; i < linesCount; i++) {
            totalHeight += mLines.get(i).mHeight;
        }
        totalHeight += mVerticalSpacing * (linesCount - 1);
        totalHeight += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(totalWidth, resolveSize(totalHeight, heightMeasureSpec));
    }

    private void restoreLine() {
        mLines.clear();
        mLine = new Line();
        mUsedWidth = 0;
    }

    private boolean newLine() {
        mLines.add(mLine);
        if (mLines.size() < mMaxLinesCount) {
            mLine = new Line();
            mUsedWidth = 0;
            return true;
        }
        return false;
    }

    class Line {
        int mWidth = 0;
        int mHeight = 0;
        List<View> views = new ArrayList<View>();

        public void addView(View view) {
            views.add(view);
            mWidth += view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();
            mHeight = mHeight < childHeight ? childHeight : mHeight;
        }

        public int getViewCount() {
            return views.size();
        }


        public void LayoutView(int l, int t) {
            int left = l;
            int top = t;
            int count = getViewCount();
            int layoutWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();//行的总宽度
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
                    if (i == 0) {
                        switch (isAlignByCenter) {
                            case AlienState.CENTER:
                                left += surplusWidth / 2;
                                break;
                            case AlienState.RIGHT:
                                left += surplusWidth;
                                break;
                            default:
                                left = 0;
                                break;
                        }
                    }
                    view.layout(left,top+topOffset,left+childWidth,top + topOffset + childHeight);
                    left += childWidth + mVerticalSpacing;//为下一个View的left赋值
                }
            }
        }
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

    public abstract static class ItemView<T> {
        protected abstract void getCover(T item, ViewHolder holder, View inflate, int position);
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

    public void setHorizontalSpacing(int spacing) {
        if (mHorizontalSpacing != spacing) {
            mHorizontalSpacing = spacing;
            requestLayoutInner();
        }
    }
    public void setVerticalSpacing(int spacing) {
        if (mVerticalSpacing != spacing) {
            mVerticalSpacing = spacing;
            requestLayoutInner();
        }
    }
}
