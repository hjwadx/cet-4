package com.hjw.cet4.ui.activity.listening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.BaseActivity;
import com.hjw.cet4.ui.activity.reading.ReadingActivity;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import fm.jihua.common.ui.helper.Hint;

public class ListeningResultActivity extends BaseActivity{
	
	private Button mCommit, mAll, mInaccurate;
	private TextView mStatistics;
	
	List<Problem> mProblems;
	HashMap<Integer, Piece> mPieceMap;
	int mType;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reading_result);
		mProblems = (ArrayList<Problem>) getIntent().getSerializableExtra("SUBJECTS");
		mPieceMap = (HashMap<Integer, Piece>) getIntent().getSerializableExtra("PIECES");
		mType = getIntent().getIntExtra("TYPE", 0);
		findViews();
		initView();
		initTitlebar();
		setListener();
	}
	
	private void setListener() {
		mCommit.setOnClickListener(l);
		mAll.setOnClickListener(l);
		mInaccurate.setOnClickListener(l);
		
	}

	private void initTitlebar() {
		setTitle("练习结果");
		getKechengActionBar().setLefttButtonGone();
	}

	private void initView() {
		int correct = getCorrect();
		int ratio = correct*100/mProblems.size();
//		ratio = (float)(Math.round(ratio*100))/100;
		String content = "你做了" + mProblems.size() +"道题\n" + "其中正确" + correct +"道\n" + "正确率" + ratio +"%";
		mStatistics.setText(content);
		
	}

	private void findViews() {
		mStatistics = (TextView) findViewById(R.id.statistics);
		mCommit = (Button) findViewById(R.id.commit);
		mAll = (Button) findViewById(R.id.all_analyze);
		mInaccurate = (Button) findViewById(R.id.inaccurate_analyze);
	}
	
	int getCorrect(){
		int result = 0;
		for(Problem problem : mProblems){
			if(problem.checkResult()){
				result++;
			}
		}
		return result;
	}
	
    OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.commit: {
				finish();
			}
				break;
			case R.id.all_analyze: {
				Intent intent = new Intent(ListeningResultActivity.this, mType == Problem.LISTENING ?(mProblems.get(0).type == Problem.PASSAGE_DICTATION ? DictationActivity.class :ListeningActivity.class): ReadingActivity.class);
				intent.putExtra("REVIEW", true);
				intent.putExtra("SUBJECTS", new ArrayList<Problem>(mProblems));
				intent.putExtra("PIECES", mPieceMap);
				startActivity(intent);
				MobclickAgent.onEvent(ListeningResultActivity.this, "action_look_all_analysis", Problem.getTypeName(mProblems.get(0).type));
			}
				break;
			case R.id.inaccurate_analyze: {
				Intent intent = new Intent(ListeningResultActivity.this, mType == Problem.LISTENING ?(mProblems.get(0).type == Problem.PASSAGE_DICTATION ? DictationActivity.class :ListeningActivity.class): ReadingActivity.class);
				intent.putExtra("REVIEW", true);
				List<Problem> list = new ArrayList<Problem>();
				for(Problem problem : mProblems){
					if(!problem.checkResult()){
						list.add(problem);
					}
				}
				if(list.size() < 1){
					Hint.showTipsShort(ListeningResultActivity.this, "没有错题");
					break;
				}
				intent.putExtra("SUBJECTS", new ArrayList<Problem>(list));
				intent.putExtra("PIECES", mPieceMap);
				startActivity(intent);
				MobclickAgent.onEvent(ListeningResultActivity.this, "action_look_wrong_question_analysis", Problem.getTypeName(mProblems.get(0).type));
			}
				break;
			default:
				break;
			}
			
		}
	};
}
