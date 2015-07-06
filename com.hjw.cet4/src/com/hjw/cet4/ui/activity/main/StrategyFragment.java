package com.hjw.cet4.ui.activity.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Strategy;
import com.hjw.cet4.ui.activity.strategy.StrategyAdapter;
import com.umeng.analytics.MobclickAgent;

import fm.jihua.common.ui.BaseFragment;

public class StrategyFragment extends BaseFragment{
	
	ListView mListView;
	List<Strategy> mStrategys = new ArrayList<Strategy>();
	StrategyAdapter mStrategyAdapter;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		view = inflater.inflate(R.layout.fragment_strategy, container, false);
//		initTitlebar();
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findView();
		initData();
		initView();
		setListener();
	}

	private void initView() {
		mStrategyAdapter = new StrategyAdapter(mStrategys);
		mListView.setAdapter(mStrategyAdapter);
	}

	private void initData() {
		mStrategys.clear();
		mStrategys.add(new Strategy("2014年6月英语四级写作预测_含题目", "http://sijikaoba.lofter.com/post/3b9f2c_13a1a2b"));
		mStrategys.add(new Strategy("英语四级选词填空题做题步骤与解题技巧", "http://sijikaoba.lofter.com/post/3b9f2c_13a0e45"));
		mStrategys.add(new Strategy("英语四级写作中考生易犯的语法错误", "http://sijikaoba.lofter.com/post/3b9f2c_13a0e3a"));
		mStrategys.add(new Strategy("英语四级单词复习方法与常见问题", "http://sijikaoba.lofter.com/post/3b9f2c_13a0e27"));
		mStrategys.add(new Strategy("关于2013年12月大学英语四、六级考试关材料及说明", "http://sijikaoba.lofter.com/post/3b9f2c_13a56f7"));
		
	}

	private void setListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Strategy strategy = mStrategys.get(position);
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(strategy.url)));  
				MobclickAgent.onEvent(StrategyFragment.this.parent, "action_click_strategy", strategy.name);
			}
		});
	}

	private void findView() {
		mListView = (ListView) findViewById(R.id.listview);
	}
}
