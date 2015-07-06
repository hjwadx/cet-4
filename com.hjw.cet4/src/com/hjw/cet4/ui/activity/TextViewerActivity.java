package com.hjw.cet4.ui.activity;

import com.hjw.cet4.R;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TextViewerActivity extends BaseActivity{
	
	String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_textviewer);
//			content = getIntent().getStringExtra(Const.INTENT_TEXT_CONTENT);
			content = getResources().getString(R.string.guide);
			((TextView)findViewById(R.id.content)).setText(content);
			initTitlebar();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	void initTitlebar(){
//		setTitle(getIntent().getStringExtra(Const.INTENT_TITLE_CONTENT));
		setTitle("新四级题型指南");
		getKechengActionBar().getRightButton().setVisibility(View.GONE);
	}

}
