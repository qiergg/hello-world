package com.ithema.defNum;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class MyTextView extends View {
	private String mTitltText;
	private int mTitleColor;
	private int mTitltSize;
	private Paint mPaint;
	private Rect mBound;
	boolean stat = false;
	static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	Handler handler  = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			postInvalidate();
		};
	};

	/**
	 * 一个参数
	 * 
	 * @param context
	 */
	public MyTextView(Context context) {
		this(context, null);

	}

	/**
	 * 两个参数
	 * 
	 * @param context
	 * @param attrs
	 */
	public MyTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	/**
	 * 三个参数
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.MyTextView, defStyle, 0);
		int n = a.getIndexCount();

		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);

			switch (attr) {
			case R.styleable.MyTextView_titleText:
				mTitltText = a.getString(attr);
				break;
			case R.styleable.MyTextView_titleTextColor:
				mTitleColor = a.getColor(attr, Color.GREEN);
				break;
			case R.styleable.MyTextView_titleTextSize:
				mTitltSize = a.getDimensionPixelSize(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
								getResources().getDisplayMetrics()));
				break;

			default:
				break;
			}
			a.recycle();

			mPaint = new Paint();
			mPaint.setTextSize(mTitltSize);
			mBound = new Rect();
			mPaint.getTextBounds(mTitltText, 0, mTitltText.length(), mBound);
		}
		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stat = !stat;
				singleThreadExecutor.execute(new Runnable() {

					@Override
					public void run() {
						while (stat) {

							mTitltText = randomText();
							handler.sendEmptyMessageDelayed(0, 50);
							

						}

					}
				});
				

			}

		});
	}

	private String randomText() {
		Random random = new Random();
		HashSet<Integer> hashSet = new HashSet<Integer>();
		while (hashSet.size() < 4) {
			int nextInt = random.nextInt(10);
			hashSet.add(nextInt);
		}
		StringBuffer sb = new StringBuffer();
		for (Integer i : hashSet) {
			sb.append(i + "");
		}
		return sb.toString();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width;
		int height;
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			mPaint.setTextSize(mTitltSize);
			mPaint.getTextBounds(mTitltText, 0, mTitltText.length(), mBound);
			int textWidth = mBound.width();
			int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
			width = desired;
		}
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			mPaint.setTextSize(mTitltSize);
			mPaint.getTextBounds(mTitltText, 0, mTitltText.length(), mBound);
			int textHeight = mBound.height();
			int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
			height = desired;
		}
		setMeasuredDimension(width, height);
		// System.out.println(width+"gggg"+height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setColor(Color.YELLOW);
		canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
		// System.out.println(getMeasuredWidth()+"kkkk"+getMeasuredHeight());
		mPaint.setColor(mTitleColor);
		canvas.drawText(mTitltText, getWidth() / 2 - mBound.width() / 2,
				getHeight() / 2 + mBound.height() / 2, mPaint);

	}

}
