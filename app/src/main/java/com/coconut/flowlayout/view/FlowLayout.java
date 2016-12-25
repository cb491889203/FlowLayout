package com.coconut.flowlayout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bob
 * @date 2016/12/25 0025
 * @description com.coconut.flowlayout.view:
 */

public class FlowLayout extends ViewGroup {

	/** FlowLayout中所有的子view的集合，集合中的每一个list代表每一行 */
	List<List<View>> allViews;
	/** FlowLayout中所有行的高度的集合 */
	List<Integer> lineHeights;

	public FlowLayout(Context context) {
		this(context, null);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		allViews = new ArrayList<>();
		lineHeights = new ArrayList<>();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//父控件给我们的约束
		int specSizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int specSizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int specModeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int specModeHeight = MeasureSpec.getMode(heightMeasureSpec);

		//流式布局的宽高
		int width = 0;
		int height = 0;
		//当前行的宽高
		int lineWidth = 0;  //行宽
		int lineHeight = 0;  //行高

		//获取子View数量
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			/*
			测量每一个子View，方法中会自动调用getChildMeasureSpec(parentWidthMeasureSpec,mPaddingLeft + mPaddingRight, lp.width),
			这里不使用measureChildWithMargins(View child,int parentWidthMeasureSpec, int widthUsed,int parentHeightMeasureSpec, int heightUsed)
            是因为我们在这里需要自己来计算margin的数量，决定每行的宽高
			*/
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			MarginLayoutParams childLayoutParams = (MarginLayoutParams) child.getLayoutParams();
			//获取子View的宽高，需要计算它的margin
			int childWidth = child.getMeasuredWidth() + childLayoutParams.leftMargin + childLayoutParams.rightMargin;
			int childHeight = child.getMeasuredHeight() + childLayoutParams.topMargin + childLayoutParams.bottomMargin;

			if (lineWidth + childWidth >= specSizeWidth - getPaddingLeft() - getPaddingRight()) {
				//需要换行,记录当前流式布局的总宽高
				width = Math.max(width, lineWidth);
				height += lineHeight;
				//重置新的一行的宽高
				lineWidth = childWidth;
				lineHeight = childHeight;
			} else {
				//继续添加在本行
				lineWidth += childWidth;
				lineHeight = Math.max(childHeight, lineHeight);
			}
			//当前i到达最后一个控件时。需要处理总的宽高
			if (i == childCount - 1) {
				width = Math.max(lineWidth, width);
				height += lineHeight;
			}
		}
		setMeasuredDimension(specModeWidth == MeasureSpec.EXACTLY ? specSizeWidth : width + getPaddingLeft() + getPaddingRight(), specModeHeight ==
				MeasureSpec.EXACTLY ?
				specSizeHeight : height + (getPaddingLeft() + getPaddingRight()));
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		allViews.clear();
		lineHeights.clear();
		//当前流式布局的宽高
		int width = getWidth();
		int height = getHeight();
		//当前行的宽高
		int lineWidth = 0;
		int lineHeight = 0;
		//当前行的子view的集合
		List<View> lineViews = new ArrayList<>();
		//获取所有子View
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			MarginLayoutParams childLayoutParams = (MarginLayoutParams) child.getLayoutParams();
			int childWidth = child.getMeasuredWidth() + childLayoutParams.leftMargin + childLayoutParams.rightMargin;
			int childHeight = child.getMeasuredHeight() + childLayoutParams.topMargin + childLayoutParams.bottomMargin;
			//每一行的操作
			if(lineWidth + childWidth >= width - (getPaddingLeft() + getPaddingRight())){
			    //需要换行
				//将当前行加入总的集合中
				allViews.add(lineViews);
				lineHeights.add(lineHeight);
				//重置数据
				lineViews = new ArrayList<>();
				lineWidth = 0;
				lineHeight = childHeight;
			}
			lineWidth += childWidth;
			lineHeight = Math.max(lineHeight, childHeight);
			//每一行添加view
			lineViews.add(child);
		}
		//最后一个子View时特殊处理
		allViews.add(lineViews);
		lineHeights.add(lineHeight);

		//开始给每个view布局
		int left = getPaddingLeft();//左边的距离，定位当前view的左边距
		int top = getPaddingTop();//高度，定位当前view的上边距
		int rowCount = lineHeights.size();//当前有几行
		for (int i = 0; i < rowCount; i++) {
			//初始化每行的参数
			lineViews = allViews.get(i);
			lineHeight = lineHeights.get(i);
			for (int j = 0; j < lineViews.size(); j++) {
				View child = lineViews.get(j);
				if (child.getVisibility() == View.GONE) {
					continue;
				}
				MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
				//布局
				int childLeft = left + lp.leftMargin;
				int childTop = top + lp.topMargin;
				int childRight = childLeft + child.getMeasuredWidth();
				int childBottom = childTop + child.getMeasuredHeight();
				child.layout(childLeft, childTop, childRight, childBottom);
				//累加每行宽度
				left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
			}
			//每行布局结束,累加高度，宽度从0开始。
			top += lineHeight;
			left = getPaddingLeft();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}
}
