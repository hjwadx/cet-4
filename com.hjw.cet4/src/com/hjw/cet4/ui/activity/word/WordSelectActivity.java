package com.hjw.cet4.ui.activity.word;

import java.util.ArrayList;
import java.util.List;

import com.hjw.cet4.App;
import com.hjw.cet4.R;
import com.hjw.cet4.entities.Jotter;
import com.hjw.cet4.entities.WordList;
import com.hjw.cet4.ui.activity.BaseActivity;
import com.hjw.cet4.ui.adapter.JotterAdapter;
import com.hjw.cet4.ui.adapter.WordListsAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class WordSelectActivity  extends BaseActivity{
	
	ListView listView1, listView2;
	
	final int JOTTER = 0;
	final int LIST = 1;
	
	int mState = 0;
	
	JotterAdapter mJotterAdapter;
	WordListsAdapter mWordListsAdapter;
	List<Jotter> mJotters = new ArrayList<Jotter>();
	List<WordList> mWordLists = new ArrayList<WordList>();
	
	Jotter jotter;
	WordList wordList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_words);
		findViews();
		initView();
		initTitlebar();
		setListener();
	}

	private void setListener() {
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mState = 1;
				listView1.setVisibility(View.GONE);
				listView2.setVisibility(View.VISIBLE);
				jotter = mJotters.get(position);
				initData2(jotter);
				changeTitle();
			}
		});
		listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				wordList = (WordList) mWordListsAdapter.getItem(position);
				Intent intent = new Intent();
				intent.putExtra("JOTTER", jotter);
				intent.putExtra("WORDLIST", wordList);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
	}

	private void initTitlebar() {
		changeTitle();
		getKechengActionBar().getLeftTextButton().setOnClickListener(l);
	}

	private void changeTitle() {
		if(mState == 1){
			setTitle("List");
		} else {
			setTitle("单词本");
		}
		
	}

	private void initView() {
		initData();
		mJotterAdapter = new JotterAdapter(mJotters, this);
		listView1.setAdapter(mJotterAdapter);
		mWordListsAdapter = new WordListsAdapter(mWordLists, this);
		listView2.setAdapter(mWordListsAdapter);
		
	}

	private void findViews() {
		listView1 = (ListView) findViewById(R.id.list1);
		listView2 = (ListView) findViewById(R.id.list2);
	}
	
    OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.menu_textview:
				onBackClick();
				break;
			default:
				break;
			}
		}
	};
	
	
	private void initData() {
		//测试
//		for(int i=0; i<10; i++){
//			mJotters.add(new Jotter(i, "单词本" + i, 0));
//		}
		mJotters = App.getInstance().getExamDBHelper().getJotters();
	}
	
	private void initData2(Jotter jotter) {
		mWordLists.clear();
		mWordLists.addAll(App.getInstance().getExamDBHelper().getWordListsByJotterId(jotter.id));
		mWordListsAdapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			onBackClick();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	void onBackClick() {
		if(mState == 1){
			mState = 0;
			listView1.setVisibility(View.VISIBLE);
			listView2.setVisibility(View.GONE);
			changeTitle();
		} else {
			finish();
		}
	}

}
