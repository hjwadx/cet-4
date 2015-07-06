package com.hjw.cet4.ui.activity.uniterm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.BaseActivity;
import com.hjw.cet4.ui.activity.listening.DictationActivity;
import com.hjw.cet4.ui.activity.listening.ListeningAmountSelectActivity;
import com.hjw.cet4.ui.activity.reading.ReadingAmountSelectActivity;
import com.umeng.analytics.MobclickAgent;

public class TypeSelectActivity extends BaseActivity{
	
	int mType;
	Button mTypeBtn0, mTypeBtn1, mTypeBtn2, mTypeBtn3;
	List<Button> buttons = new ArrayList<Button>();
	String[] lintenings = {"短对话", "长对话", "短文理解", "短文听写"};
	String[] readings = {"词汇理解", "长篇阅读", "仔细阅读"};
	List<String> list = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type_select);
		mType = getIntent().getIntExtra("TYPE", Problem.LISTENING);
		findViews();
		initView();
//		initPlayer();
		setListener();
		initTitlebar();
	}
	
	private void initTitlebar() {
		if(mType == Problem.LISTENING){
			setTitle("听力");
		} else {
			setTitle("阅读");
		}
	}
	
	private void findViews() {
		mTypeBtn0 = (Button) findViewById(R.id.type_select_btn_0);
		mTypeBtn1 = (Button) findViewById(R.id.type_select_btn_1);
		mTypeBtn2 = (Button) findViewById(R.id.type_select_btn_2);
		mTypeBtn3 = (Button) findViewById(R.id.type_select_btn_3);
		buttons.add(mTypeBtn0);
		buttons.add(mTypeBtn1);
		buttons.add(mTypeBtn2);
		buttons.add(mTypeBtn3);
	}
	
	private void initView() {
		list.clear();
		if(mType == Problem.LISTENING){
			list.addAll(Arrays.asList(lintenings));
		} else {
			list.addAll(Arrays.asList(readings));
		}
		for(int i = 0; i < 4; i++){
			if(i < list.size()){
				buttons.get(i).setText(list.get(i));
			} else {
				buttons.get(i).setVisibility(View.GONE);
			}
		}
	}
	
	private void setListener() {
		mTypeBtn0.setOnClickListener(l);
		mTypeBtn1.setOnClickListener(l);
		mTypeBtn2.setOnClickListener(l);
		mTypeBtn3.setOnClickListener(l);
	}
	
    OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.type_select_btn_0: {
				Intent intent = new Intent(TypeSelectActivity.this, mType == Problem.LISTENING ? ListeningAmountSelectActivity.class : ReadingAmountSelectActivity.class);
				intent.putExtra("TYPE", mType == Problem.LISTENING ? Problem.SHORT_CONVERSATIONS : Problem.WORDS_COMPREHENSION);
				startActivity(intent);
				MobclickAgent.onEvent(TypeSelectActivity.this, mType == Problem.LISTENING ? "action_click_short_conversations" : "action_click_words_comperhension");	
			}
				break;
			case R.id.type_select_btn_1: {
				Intent intent = new Intent(TypeSelectActivity.this, mType == Problem.LISTENING ? ListeningAmountSelectActivity.class : ReadingAmountSelectActivity.class);
				intent.putExtra("TYPE", mType == Problem.LISTENING ? Problem.LONG_CONVERSATIONS : Problem.LONG_TO_READ);
				startActivity(intent);
				MobclickAgent.onEvent(TypeSelectActivity.this, mType == Problem.LISTENING ? "action_click_long_conversations" : "action_click_long_to_read");	
			}
				break;
			case R.id.type_select_btn_2: {
				Intent intent = new Intent(TypeSelectActivity.this, mType == Problem.LISTENING ? ListeningAmountSelectActivity.class : ReadingAmountSelectActivity.class);
				intent.putExtra("TYPE", mType == Problem.LISTENING ? Problem.SHORT_PASSAGES : Problem.CAREFUL_READING);
				startActivity(intent);
				MobclickAgent.onEvent(TypeSelectActivity.this, mType == Problem.LISTENING ? "action_click_short_passages" : "action_click_careful_reading");	
			}
				break;
			case R.id.type_select_btn_3: {
				Intent intent = new Intent(TypeSelectActivity.this, mType == Problem.LISTENING ? DictationActivity.class : ReadingAmountSelectActivity.class);
				intent.putExtra("TYPE", Problem.PASSAGE_DICTATION);
				startActivity(intent);
				MobclickAgent.onEvent(TypeSelectActivity.this, "action_click_passage_dictation");	
			}
				break;
			default:
				break;
			}
		}
	};

}
