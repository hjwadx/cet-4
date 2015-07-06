package com.hjw.cet4.ui.activity.practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.BaseActivity;
import com.hjw.cet4.ui.adapter.PracticeResultAdapter;
import com.hjw.cet4.ui.view.MyGridView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import fm.jihua.common.ui.helper.Hint;

public class PracticeResultActivity extends BaseActivity{
	
	private Button mCommit, mAll, mInaccurate;
	private TextView mPart2Total, mPart2Correct, mPart3Total, mPart3Correct, mAccuracy;
	ScrollView mScrollView;
	
	List<Problem> mProblems;
	List<Problem> mProblemsForGridView;
	HashMap<Integer, Piece> mPieceMap;
	
	MyGridView mGridView;
	PracticeResultAdapter mPracticeResultAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice_result);
		mProblems = (ArrayList<Problem>) getIntent().getSerializableExtra("SUBJECTS");
		mPieceMap = (HashMap<Integer, Piece>) getIntent().getSerializableExtra("PIECES");
		mProblemsForGridView = new ArrayList<Problem>();
		for(Problem problem : mProblems){
			if(problem.mustHasResult()){
				mProblemsForGridView.add(problem);
			}
		}
		findViews();
		initView();
		initTitlebar();
		setListener();
	}
	
	private void setListener() {
		mCommit.setOnClickListener(l);
		mAll.setOnClickListener(l);
		mInaccurate.setOnClickListener(l);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position >= mProblemsForGridView.size()){
					return;
				}
				ArrayList<Problem> list = new ArrayList<Problem>();
				list.add((Problem)mPracticeResultAdapter.getItem(position));
				Intent intent = new Intent(PracticeResultActivity.this, PracticeActivity.class);
				intent.putExtra("REVIEW", true);
				intent.putExtra("SUBJECTS", list);
				intent.putExtra("PIECES", mPieceMap);
				startActivity(intent);
			}
		});
	}

	private void initTitlebar() {
		setTitle("练习结果");
		getKechengActionBar().setLefttButtonGone();
	}

	private void initView() {
		initPartII();
		initPartIII();
		initRatio();
//		ratio = (float)(Math.round(ratio*100))/100;
		mPracticeResultAdapter = new PracticeResultAdapter(mProblemsForGridView, this);
		mGridView.setAdapter(mPracticeResultAdapter);
		mGridView.setFocusable(false);
		mScrollView.scrollTo(0, 0);
	}
	
	private void initPartII(){
		List<Problem> problems = new ArrayList<Problem>();
		for(Problem problem : mProblems){
			if(problem.getPart() == Problem.PART_II && problem.type != Problem.PASSAGE_DICTATION){
				problems.add(problem);
			}
		}
		int correct = Problem.getCorrect(problems);
		mPart2Total.setText("/" + problems.size());
		mPart2Correct.setText(correct + "");
	}
	
	private void initRatio(){
		List<Problem> problems = new ArrayList<Problem>();
		for(Problem problem : mProblems){
			if(problem.getPart() == Problem.PART_III || (problem.getPart() == Problem.PART_II && problem.type != Problem.PASSAGE_DICTATION)){
				problems.add(problem);
			}
		}
		int correct = Problem.getCorrect(problems);
		int ratio = correct*100/problems.size();
		mAccuracy.setText(ratio + "%");
	}
	
	private void initPartIII(){
		List<Problem> problems = new ArrayList<Problem>();
		for(Problem problem : mProblems){
			if(problem.getPart() == Problem.PART_III){
				problems.add(problem);
			}
		}
		int correct = Problem.getCorrect(problems);
		mPart3Total.setText("/" + problems.size());
		mPart3Correct.setText(correct + "");
	}
	
	

	private void findViews() {
		mPart2Total = (TextView) findViewById(R.id.part2total);
		mPart2Correct = (TextView) findViewById(R.id.part2correct);
		mPart3Total = (TextView) findViewById(R.id.part3total);
		mPart3Correct = (TextView) findViewById(R.id.part3correct);
		mAccuracy = (TextView) findViewById(R.id.accuracy);
		mCommit = (Button) findViewById(R.id.commit);
		mAll = (Button) findViewById(R.id.all_analyze);
		mInaccurate = (Button) findViewById(R.id.inaccurate_analyze);
		mGridView = (MyGridView) findViewById(R.id.gridview);
		mScrollView = (ScrollView) findViewById(R.id.scrollview);
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
				Intent intent = new Intent(PracticeResultActivity.this, PracticeActivity.class);
				intent.putExtra("REVIEW", true);
				intent.putExtra("SUBJECTS", new ArrayList<Problem>(mProblems));
				intent.putExtra("PIECES", mPieceMap);
				startActivity(intent);
			}
				break;
			case R.id.inaccurate_analyze: {
				Intent intent = new Intent(PracticeResultActivity.this, PracticeActivity.class);
				intent.putExtra("REVIEW", true);
				List<Problem> list = new ArrayList<Problem>();
				for(Problem problem : mProblems){
					if(!problem.checkResult()){
						list.add(problem);
					}
				}
				if(list.size() < 1){
					Hint.showTipsShort(PracticeResultActivity.this, "没有错题");
					break;
				}
				intent.putExtra("SUBJECTS", new ArrayList<Problem>(list));
				intent.putExtra("PIECES", mPieceMap);
				startActivity(intent);
			}
				break;
			default:
				break;
			}
			
		}
	};

}
