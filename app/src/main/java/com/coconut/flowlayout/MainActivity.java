package com.coconut.flowlayout;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.coconut.flowlayout.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MainActivity";
	private FlowLayout mFlowLayout;
	private List<TextView> mTextViewList;
	private String[] labels;
	private Button add_label;
	private Button delete_label;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mFlowLayout = (FlowLayout) findViewById(R.id.fl);
		add_label = (Button) findViewById(R.id.add_label);
		delete_label = (Button) findViewById(R.id.delete_label);
		initData();
		setListener();
	}

	private void initData() {
		mTextViewList = new ArrayList<>();
		labels = new String[]{"Java", "Android", "IOS", "C", "C++", "Go", "C#", "Ruby", "Phyton", "PHP", ".NET", "HTML/CSS", "javaScript",
				"jQuery", "AngularJS", "Cocos2d-x", "MySQL", "React", "WebApp", "Oracle", "SQL Server", "MongoDB"};

	}

	private void setListener() {
		add_label.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Random random = new Random();
				String label = labels[random.nextInt(labels.length)];
				TextView mTextView = new TextView(MainActivity.this);
				ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
						.LayoutParams.WRAP_CONTENT);
//				lp = (FlowLayout.MarginLayoutParams) mTextView.getLayoutParams();
				lp.setMargins(10, 10, 10, 10);
				mTextView.setText(label);
				mTextView.setBackgroundResource(R.drawable.greywhite);
				mTextView.setTextColor(Color.parseColor("#ffffff"));
				mTextView.setLayoutParams(lp);
				mTextViewList.add(mTextView);
				mFlowLayout.addView(mTextView);
				Log.i(TAG, "add_label: mTextViewList.size = " +mTextViewList.size());
				Log.i(TAG, "add_label: mFlowLayout.getChildCount = " +mFlowLayout.getChildCount());
			}
		});

		delete_label.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mTextViewList == null || mTextViewList.isEmpty()) return;

				mFlowLayout.removeView(mFlowLayout.getChildAt(mTextViewList.size() - 1));
				mTextViewList.remove(mTextViewList.size() - 1);
				Log.i(TAG, "delete_label: mTextViewList.size = " +mTextViewList.size());
				Log.i(TAG, "delete_label: mFlowLayout.getChildCount = " +mFlowLayout.getChildCount());
			}
		});
	}
}
