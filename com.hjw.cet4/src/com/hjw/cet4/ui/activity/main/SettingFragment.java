package com.hjw.cet4.ui.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hjw.cet4.App;
import com.hjw.cet4.R;
import com.hjw.cet4.entities.Jotter;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.entities.WordList;
import com.hjw.cet4.ui.activity.setting.StatisticsActivity;
import com.hjw.cet4.utils.CommonUtils;
import com.hjw.cet4.utils.Const;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.UMFeedbackService;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import fm.jihua.common.ui.BaseFragment;
import fm.jihua.common.ui.helper.Hint;
import fm.jihua.common.ui.helper.UIUtil;

public class SettingFragment extends BaseFragment{
	
    Button mClearCache;
    Button mFeedback;
    TextView wordTextView;
    TextView writingView;
    TextView listeningTextView;
    TextView readingView;
    TextView translateView;
    TextView dictationView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		view = inflater.inflate(R.layout.fragment_setting, container, false);
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
		initText();
	}


	private void initText() {
		readingView.setText(getReadingText());
		listeningTextView.setText(getListeningText());
		writingView.setText(getWritingText());
		translateView.setText(getTranslateText());
		wordTextView.setText(getWordText());
		dictationView.setText(getDictationText());
	}
	
	private String getDictationText() {
		List<Piece> pieces;
		HashMap<Integer, Piece> pieceMap = new HashMap<Integer, Piece>();
		pieces = App.getInstance().getExamDBHelper().getPiecesByType(Problem.PASSAGE_DICTATION);
		for(Piece piece : pieces){
			pieceMap.put(piece.id, piece);
		}
		Piece piece = pieceMap.get(App.getInstance().getCurrentValue(Const.PASSAGE_DICTATION_CURRENT));
		return Math.max(1, pieces.indexOf(piece) + 1) + "/" + pieces.size();
	}
	
	private String getWordText() {
		int jotter_id = App.getInstance().getCurrentValue(Const.JOTTER_CURRENT);
		int wordlist_id	= App.getInstance().getCurrentValue(Const.WORDLIST_CURRENT);
		if(jotter_id * wordlist_id == 0){
			return "没有记录";
		}
		Jotter jotter = App.getInstance().getExamDBHelper().getJottersById(jotter_id);
		WordList wordList = App.getInstance().getExamDBHelper().getWordListsById(wordlist_id);
		if(jotter == null || wordList == null){
			return "没有记录";
		}
		return jotter.name + "  " +(CommonUtils.isNullString(wordList.name) ? "list" + wordList.num : wordList.name);
	}
	
	private String getWritingText() {
		List<Piece> pieces;
		HashMap<Integer, Piece> pieceMap = new HashMap<Integer, Piece>();
		pieces = App.getInstance().getExamDBHelper().getPiecesByType(Problem.WRITING);
		for(Piece piece : pieces){
			pieceMap.put(piece.id, piece);
		}
		Piece piece = pieceMap.get(App.getInstance().getCurrentValue(Const.WRITING_CURRENT));
		return Math.max(1, pieces.indexOf(piece) + 1) + "/" + pieces.size();
	}
	
	private String getTranslateText() {
		List<Piece> pieces;
		HashMap<Integer, Piece> pieceMap = new HashMap<Integer, Piece>();
		pieces = App.getInstance().getExamDBHelper().getPiecesByType(Problem.TRANSLATE);
		for(Piece piece : pieces){
			pieceMap.put(piece.id, piece);
		}
		Piece piece = pieceMap.get(App.getInstance().getCurrentValue(Const.TRANSLATE_CURRENT));
		return Math.max(1, pieces.indexOf(piece) + 1) + "/" + pieces.size();
	}


	private String getReadingText() {
		List<Integer> types = new ArrayList<Integer>();
		types.add(Problem.WORDS_COMPREHENSION);
		types.add(Problem.LONG_TO_READ);
		types.add(Problem.CAREFUL_READING);
    	List<String> pieceIds = new ArrayList<String>();
		for(Integer type : types){
			pieceIds.addAll(App.getInstance().getDBHelper().getPieceIdsByType(App.getInstance().getUserDB(), type));
		}
		List<Problem> problems = App.getInstance().getExamDBHelper().getProblemsByPieceId(pieceIds);
		if(problems.size() < 1){
			return "正确率0%";
		}
    	App.getInstance().getDBHelper().getResultByProblems(App.getInstance().getUserDB(), problems);
    	int correct = getCorrect(problems);
		int ratio = correct*100/problems.size();
		return "正确率" + ratio + "%";
	}
	
	private String getListeningText() {
		List<Integer> types = new ArrayList<Integer>();
		types.add(Problem.SHORT_CONVERSATIONS);
		types.add(Problem.LONG_CONVERSATIONS);
		types.add(Problem.SHORT_PASSAGES);
		types.add(Problem.CAREFUL_READING);
    	List<String> pieceIds = new ArrayList<String>();
		for(Integer type : types){
			pieceIds.addAll(App.getInstance().getDBHelper().getPieceIdsByType(App.getInstance().getUserDB(), type));
		}
		List<Problem> problems = App.getInstance().getExamDBHelper().getProblemsByPieceId(pieceIds);
		if(problems.size() < 1){
			return "正确率0%";
		}
    	App.getInstance().getDBHelper().getResultByProblems(App.getInstance().getUserDB(), problems);
    	int correct = getCorrect(problems);
		int ratio = correct*100/problems.size();
		return "正确率" + ratio + "%";
	}
	
	int getCorrect(List<Problem> problems){
		int result = 0;
		for(Problem problem : problems){
			if(problem.checkResult()){
				result++;
			}
		}
		return result;
	}


	private void findView() {
		mFeedback = (Button) findViewById(R.id.feedback);
		mClearCache = (Button) findViewById(R.id.clear_cache);
		wordTextView = (TextView) findViewById(R.id.word_name);
		writingView = (TextView) findViewById(R.id.writing_name);
		listeningTextView = (TextView) findViewById(R.id.listening_name);
		readingView = (TextView) findViewById(R.id.reading_name);
		translateView = (TextView) findViewById(R.id.translate_name);
		dictationView = (TextView) findViewById(R.id.dictation_name);
	}

	private void setListener() {
		mFeedback.setOnClickListener(l);
		mClearCache.setOnClickListener(l);
//		((View)wordTextView.getParent()).setOnClickListener(l);
//		((View)writingView.getParent()).setOnClickListener(l);
		((View)listeningTextView.getParent()).setOnClickListener(l);
		((View)readingView.getParent()).setOnClickListener(l);
//		((View)translateView.getParent()).setOnClickListener(l);
	}
	

	
	OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
//			case R.id.button: {
//				DownloadSpirit downloadSpirit = new DownloadSpirit(parent, "http://exam-files.b0.upaiyun.com//test/uploads/problem/test/751af114fdb242ca9c6a5055217243f1_cet4_200206.mp3", Const.DOWNLOAD_FOLDER_PATH, "751af114fdb242ca9c6a5055217243f1_cet4_200206.mp3");
//				downloadSpirit.start(new OnDownloadListener() {
//
//					@Override
//					public void statusChanged(int statusType, int precent, int current, int total) {
//						if(progressBar.getMax() != total){
//							progressBar.setMax(total);
//						}
//						progressBar.setProgress(current);
//						if(statusType == DownloadManager.STATUS_SUCCESSFUL || statusType == DownloadManager.STATUS_FAILED || statusType == DownloadManager.STATUS_PAUSED){
//							progressBar.setMax(100);
//							progressBar.setProgress(0);
//						}
//						if(statusType == DownloadManager.STATUS_SUCCESSFUL){
//						}
//					}
//				});
//			}
//				break;
			case R.id.clear_cache: {
				clearCache();
			}
				break;
			case R.id.word_name_parent: {
				Hint.showTipsShort(parent, "单词");
			}
				break;
			case R.id.writing_name_parent: {
				Hint.showTipsShort(parent, "写作");
			}
				break;
			case R.id.listening_name_parent: {
				Intent intent = new Intent(parent, StatisticsActivity.class);
				intent.putExtra("TYPE", Problem.LISTENING);
				startActivity(intent);
				MobclickAgent.onEvent(parent, "action_click_listening_statistics");
			}
				break;
			case R.id.reading_name_parent: {
				Intent intent = new Intent(parent, StatisticsActivity.class);
				intent.putExtra("TYPE", Problem.READING);
				startActivity(intent);
				MobclickAgent.onEvent(parent, "action_click_reading_statistics");
			}
				break;
			case R.id.translate_name_parent: {
				Hint.showTipsShort(parent, "翻译");
			}
				break;
			case R.id.feedback: {
				UMFeedbackService.setGoBackButtonVisible();
				UMFeedbackService.openUmengFeedbackSDK(parent);
			}
				break;
			default:
				break;
			}
		}
	};


	private void clearCache() {
		UIUtil.block(parent);
		if(App.getInstance().clearSDcardFile()){
			Hint.showTipsShort(parent, "缓存清除成功");
		}
		UIUtil.unblock(parent);
	}
	
	

}
