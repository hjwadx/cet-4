package com.hjw.cet4.ui.activity.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.hjw.cet4.App;
import com.hjw.cet4.R;
import com.hjw.cet4.download.DownloadSpirit;
import com.hjw.cet4.download.DownloadSpirit.OnDownloadListener;
import com.hjw.cet4.entities.Paper;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.practice.ParcticeUtils;
import com.hjw.cet4.ui.activity.practice.PracticeActivity;
import com.hjw.cet4.ui.activity.practice.PracticeResultActivity;
import com.hjw.cet4.ui.adapter.PaperAdapter;
import com.hjw.cet4.utils.Const;
import com.hjw.cet4.utils.FileUtils;
import com.mozillaonline.providers.DownloadManager;
import com.umeng.analytics.MobclickAgent;

import fm.jihua.common.ui.BaseFragment;
import fm.jihua.common.ui.helper.Hint;

public class PracticeFragment extends BaseFragment{
	List<Paper> mPapers;
	ListView mPaperLists;
	PaperAdapter mPaperAdapter;
	
	String url = "http://zhangmenshiting.baidu.com/data2/music/44148422/73007331384286461256.mp3?xcode=ad4f0d2a6fb1e7145ad31e6564b422e15e770a7610358a8a";
	String url2 = "http://zhangmenshiting.baidu.com/data2/music/100897442/876012791384380061128.mp3?xcode=6e120e0d86b529d84829137bf34d2c82d72fa70a10f98447";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		view = inflater.inflate(R.layout.fragment_practice, container, false);
//		initTitlebar();
		return view;
	}

	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findView();
		setListener();
		initView();
	}

	private void initView() {
		initData();
		mPaperAdapter = new PaperAdapter(mPapers, parent);
		mPaperLists.setAdapter(mPaperAdapter);
	}


	private void findView() {
		mPaperLists = (ListView) findViewById(R.id.paper_list);
	}

	private void setListener() {
		mPaperLists.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
				Paper paper = mPapers.get(position);
				if(paper.isDownloading()){
					return;
				}
				if(paper.isVoiceDownloaded() && !paper.isDone()){
					Intent intent = new Intent(parent, PracticeActivity.class);
					intent.putExtra("PAPER", paper);
//					if(paper.isDone()){
//						intent.putExtra("REVIEW", paper.isDone());
//					}
					startActivity(intent);
					MobclickAgent.onEvent(parent, "event_enter_simulate", paper.name);
				} else if(paper.isDone()){
					Intent intent = new Intent(parent, PracticeResultActivity.class);
					intent.putExtra("SUBJECTS", new ArrayList<Problem>(ParcticeUtils.getProblemsByPaper(paper, true)));
					intent.putExtra("PIECES", ParcticeUtils.getPiecesMapByPaper(paper));
					startActivity(intent);
					MobclickAgent.onEvent(parent, "event_enter_completed_simulate", paper.name);
				} else {
					ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
					downloadAudioFile(paper, progressBar);
					MobclickAgent.onEvent(parent, "event_download_simulate", paper.name);
				}
				
			}
		});
	}
	
	void downloadAudioFile(final Paper paper, final ProgressBar progressBar){
		if (FileUtils.getInstance().isSDAvailable()) {
			if (!paper.isVoiceDownloaded()) {
				paper.setDownloading(true);
				mPaperAdapter.notifyDataSetChanged();
				DownloadSpirit downloadSpirit = new DownloadSpirit(parent, paper.voice_url, Const.AUDIO_DIR, paper.getVoiceFileName());
				downloadSpirit.start(new OnDownloadListener() {

					@Override
					public void statusChanged(int statusType, int precent, int current, int total) {
						progressBar.setVisibility(View.VISIBLE);
						if(progressBar.getMax() != total){
							progressBar.setMax(total);
						}
						progressBar.setProgress(current);
						if(statusType == DownloadManager.STATUS_SUCCESSFUL || statusType == DownloadManager.STATUS_FAILED || statusType == DownloadManager.STATUS_PAUSED){
							progressBar.setVisibility(View.GONE);
							paper.setDownloading(false);
							mPaperAdapter.notifyDataSetChanged();
						}
						if(statusType == DownloadManager.STATUS_SUCCESSFUL){
							
						}
					}
				});
			}
		} else {
			Hint.showTipsLong(parent, "SD卡不存在，请检查重新下载");
		}
	}
	
	@Override
	public void onResume() {
		mPaperAdapter.notifyDataSetChanged();
		super.onResume();
	}
	
	void initData(){
		mPapers = App.getInstance().getExamDBHelper().getPapers();
		
		//for test
//		mPapers = new ArrayList<Paper>();
//		mPapers.add(new Paper(17, "真题模拟1", url));
//		mPapers.add(new Paper(18, "真题模拟2", url2));
		
	}
}
