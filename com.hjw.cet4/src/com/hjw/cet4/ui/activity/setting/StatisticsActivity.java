package com.hjw.cet4.ui.activity.setting;

import java.util.ArrayList;
import java.util.List;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.BaseActivity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class StatisticsActivity extends BaseActivity{
	
	 private ViewPager mViewPager;
	 
	 StatisticsPagerAdapter mPagerAdapter;
	 
	 List<Integer> mTypes;
	 
	 int mType;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writing);
		mType = getIntent().getIntExtra("TYPE", 0);
		findViews();
		initView();
		initTitlebar();
	}
	
	private void initTitlebar() {
		
	}
	
	private void findViews() {
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
	}
	
	private void initView() {
		initData();
		mPagerAdapter = new StatisticsPagerAdapter(this, mTypes);
		mViewPager.setAdapter(mPagerAdapter); 
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				initTitlebar(mTypes.get(arg0));
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
	}

	private void initData() {
		mTypes = new ArrayList<Integer>();
		if(mType == Problem.READING){
			initTitlebar(Problem.WORDS_COMPREHENSION);
			mTypes.add(Problem.WORDS_COMPREHENSION);
			mTypes.add(Problem.LONG_TO_READ);
			mTypes.add(Problem.CAREFUL_READING);
		} else {
			initTitlebar(Problem.SHORT_CONVERSATIONS);
			mTypes.add(Problem.SHORT_CONVERSATIONS);
			mTypes.add(Problem.LONG_CONVERSATIONS);
			mTypes.add(Problem.SHORT_PASSAGES);
//			mTypes.add(Problem.PASSAGE_DICTATION);
		}
		
	}
	
	private void initTitlebar(int type) {
		String title;
		if(mType == Problem.READING){
			title = "阅读";
		} else{
			title = "听力";
		}
		switch (type) {
		case Problem.SHORT_CONVERSATIONS:
			title += "-短对话";
			break;
		case Problem.LONG_CONVERSATIONS:
			title += "-长对话";
			break;
		case Problem.SHORT_PASSAGES:
			title += "-短文理解";
			break;
		case Problem.PASSAGE_DICTATION:
			title += "-短文听写";
			break;
		case Problem.WORDS_COMPREHENSION:
			title += "-词汇理解";
			break;
		case Problem.LONG_TO_READ:
			title += "-长篇阅读";
		    break;
		case Problem.CAREFUL_READING:
			title += "-仔细阅读";
			break;
		default:
			break;
		}
		setTitle(title);
	}

}
