package com.hjw.cet4.ui.activity.main;

import java.util.Random;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.translate.TranslateActivity;
import com.hjw.cet4.ui.activity.uniterm.TypeSelectActivity;
import com.hjw.cet4.ui.activity.word.WordActivity;
import com.hjw.cet4.ui.activity.writing.WritingActivity;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import fm.jihua.common.ui.BaseFragment;
import fm.jihua.common.ui.helper.Hint;

public class UnitermFragment extends BaseFragment{
	
	View mWordBtn, mWritingBtn, mListeningBtn, mReadingBtn, mTranslateBtn;
	TextView mCelebratedDictum;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		view = inflater.inflate(R.layout.fragment_uniterm, container, false);
//		initTitlebar();
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findView();
		setListener();
	}

	private void setListener() {
		mWordBtn.setOnClickListener(l);
		mWritingBtn.setOnClickListener(l);
		mListeningBtn.setOnClickListener(l);
		mReadingBtn.setOnClickListener(l);
		mTranslateBtn.setOnClickListener(l);
	}

	private void findView() {
		mWordBtn = findViewById(R.id.word);
		mWritingBtn = findViewById(R.id.writing);
		mListeningBtn = findViewById(R.id.listening);
		mReadingBtn = findViewById(R.id.reading);
		mTranslateBtn = findViewById(R.id.translate);
		mCelebratedDictum = (TextView) findViewById(R.id.celebrated_dictum);
		Random random = new Random();
		mCelebratedDictum.setText(getResources().getStringArray(R.array.celebrated_dictum)[random.nextInt(19)]);
	}
	
	OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.word: {
				Intent intent = new Intent(parent, WordActivity.class);
				startActivity(intent);
				MobclickAgent.onEvent(parent, "action_click_word");	
			}
				break;
			case R.id.writing:{
				Intent intent = new Intent(parent, WritingActivity.class);
				startActivity(intent);
				MobclickAgent.onEvent(parent, "action_click_writing");	
			}
				break;
			case R.id.listening: {
				Intent intent = new Intent(parent, TypeSelectActivity.class);
				intent.putExtra("TYPE", Problem.LISTENING);
				startActivity(intent);
				MobclickAgent.onEvent(parent, "action_click_listening");	
			}
				break;
			case R.id.reading:{
				Intent intent = new Intent(parent, TypeSelectActivity.class);
				intent.putExtra("TYPE", Problem.READING);
				startActivity(intent);
				MobclickAgent.onEvent(parent, "action_click_reading");	
			}
				break;
			case R.id.translate:{
				Intent intent = new Intent(parent, TranslateActivity.class);
				startActivity(intent);
				MobclickAgent.onEvent(parent, "action_click_translate");	
			}
				break;
			default:
				break;
			}
			
		}
	};

}
