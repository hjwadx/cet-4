package com.hjw.cet4.ui.activity.word;

import java.util.ArrayList;
import java.util.List;

import com.hjw.cet4.App;
import com.hjw.cet4.R;
import com.hjw.cet4.entities.Jotter;
import com.hjw.cet4.entities.Word;
import com.hjw.cet4.entities.WordList;
import com.hjw.cet4.ui.activity.BaseActivity;
import com.hjw.cet4.utils.Const;
import com.hjw.cet4.utils.TutorUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class WordActivity extends BaseActivity{
	
	public static final int SELECT_WORD = 1234;
	
	private ViewPager mViewPager;
	
//	private WordPagerAdapter mPagerAdapter;
	private WordFragmentPagerAdapter mPagerFragmentAdapter;
	
	List<Word> mWords;
	List<Word> mCurrnetWords;
	
	List<WordList> mWordLists;
	
	Jotter mJotter;
	WordList mWordList;
	int jotter_id;
	int wordlist_id;
	int list_index = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writing);
		findViews();
		initView();
		initTitlebar();
	}

	private void initTitlebar() {
		if(mWords != null && mWords.size() > 0){
			setTitle("List" + list_index + "（" + (mCurrnetWords.indexOf(mWords.get(App.getInstance().getCurrentValue(Const.WORD_CURRENT))) + 1) + "/" + mCurrnetWords.size() + "）");
//			setTitle("单词（" + (App.getInstance().getCurrentValue(Const.WORD_CURRENT) + 1) + "/" + mWords.size() + "）");
		} else {
			setTitle("单词");
		}
		getKechengActionBar().setRightText("单词本");
		getKechengActionBar().getRightTextButton().setOnClickListener(l);
	}

	private void initView() {
		if(!App.getInstance().getSharedBoolean(Const.SLIDE_TUTOR)){
			TutorUtils.getInstance().showSlideTutor(this);
		}
		
		initData();
		if(mWords.size() < 1){
			Intent intent = new Intent(WordActivity.this, WordSelectActivity.class);
			startActivityForResult(intent, SELECT_WORD);
		}
//		mPagerAdapter = new WordPagerAdapter(this, mWords);
		mPagerFragmentAdapter = new WordFragmentPagerAdapter(getSupportFragmentManager(), mWords);
		mViewPager.setAdapter(mPagerFragmentAdapter); 
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				App.getInstance().setCurrentValue(Const.WORD_CURRENT, arg0);
				if(wordlist_id != mWords.get(arg0).list_id){
					wordlist_id = mWords.get(arg0).list_id;
					mCurrnetWords.clear();
					for(Word word : mWords){
						if(wordlist_id == word.list_id){
							mCurrnetWords.add(word);
						}
					}
					App.getInstance().setCurrentValue(Const.WORDLIST_CURRENT, wordlist_id);
					for(WordList wordList : mWordLists){
						if(wordList.id == wordlist_id){
							list_index = mWordLists.indexOf(wordList) + 1;
							break;
						}
					}
				}
				setTitle("List" + list_index + "（" + (mCurrnetWords.indexOf(mWords.get(arg0)) + 1) + "/" + mCurrnetWords.size() + "）");
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mViewPager.setCurrentItem(App.getInstance().getCurrentValue(Const.WORD_CURRENT));
	}

	private void initData() {
		mWords = new ArrayList<Word>();
		mCurrnetWords = new ArrayList<Word>();
		mWordLists = new ArrayList<WordList>();
		jotter_id = App.getInstance().getCurrentValue(Const.JOTTER_CURRENT);
		wordlist_id	= App.getInstance().getCurrentValue(Const.WORDLIST_CURRENT);
		if(jotter_id * wordlist_id == 0){
			return;
		}

		mWordLists.addAll(App.getInstance().getExamDBHelper().getWordListsByJotterId(jotter_id));
		for(WordList wordList : mWordLists){
			List<Word> list = App.getInstance().getExamDBHelper().getWordsByWordListId(wordList.id);
			mWords.addAll(list);
			if(wordlist_id == wordList.id){
				mCurrnetWords.addAll(list);
			}
		}
		for(WordList wordList : mWordLists){
			if(wordList.id == wordlist_id){
				list_index = mWordLists.indexOf(wordList) + 1;
				break;
			}
		}
		
		//test
//		mWords = new ArrayList<Word>();
//		for(int i=0; i<10; i++){
//			mWords.add(new Word(i, "单词：" + i, "单词：" + i + "的翻译", "单词：" + i + "的音标", null));
//		}
	}

	private void findViews() {
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
	}
	
	OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.action_textview:
				Intent intent = new Intent(WordActivity.this, WordSelectActivity.class);
				startActivityForResult(intent, SELECT_WORD);
				break;
			default:
				break;
			}
			
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode != RESULT_OK) {
			if(mWords.size() < 1){
				finish();
			}
			return;
		}
		if(requestCode == SELECT_WORD){
			Jotter jotter = (Jotter) data.getSerializableExtra("JOTTER");
			WordList wordList = (WordList) data.getSerializableExtra("WORDLIST");
			if(wordlist_id != wordList.id){
				if(jotter_id == jotter.id){
					mWordList = wordList;
					for(Word word : mWords){
						if(wordList.id == word.list_id){
							mViewPager.setCurrentItem(mWords.indexOf(word));
							break;
						}
					}
				} else {
					mJotter = jotter;
					mWordList = wordList;
					jotter_id = jotter.id;
					wordlist_id = wordList.id;
					mWords.clear();
					mCurrnetWords.clear();
					mWordLists.clear();
					
					mWordLists.addAll(App.getInstance().getExamDBHelper().getWordListsByJotterId(jotter_id));
					for(WordList wordlist : mWordLists){
						List<Word> list = App.getInstance().getExamDBHelper().getWordsByWordListId(wordlist.id);
						mWords.addAll(list);
						if(wordlist_id == wordlist.id){
							mCurrnetWords.addAll(list);
						}
					}
//					mWords = App.getInstance().getExamDBHelper().getWordsByWordListId(mWordList.id);
//					mPagerAdapter = new WordPagerAdapter(this, mWords);
					mPagerFragmentAdapter = new WordFragmentPagerAdapter(getSupportFragmentManager(), mWords);
					mViewPager.setAdapter(mPagerFragmentAdapter); 
					mViewPager.setCurrentItem(mWords.indexOf(mCurrnetWords.get(0)));

					App.getInstance().setCurrentValue(Const.JOTTER_CURRENT, mJotter.id);
					App.getInstance().setCurrentValue(Const.WORDLIST_CURRENT, mWordList.id);
				}
			}
			Log.e("hjw", "jotter:" + jotter.id + "    wordList:" + wordList.id);
		}
	};

}
