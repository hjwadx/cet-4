package com.hjw.cet4.ui.activity.writing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hjw.cet4.App;
import com.hjw.cet4.R;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.BaseActivity;
import com.hjw.cet4.ui.activity.practice.ProblemPagerAdapter;
import com.hjw.cet4.utils.Const;
import com.hjw.cet4.utils.TutorUtils;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class WritingActivity extends BaseActivity{
	
	private ViewPager mViewPager;
	
	private ProblemPagerAdapter mPagerAdapter;
	
	List<Problem> mProblems;
	List<Piece> mPieces;
	HashMap<Integer, Piece> mPieceMap = new HashMap<Integer, Piece>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writing);
		findViews();
		initView();
	}

	private void initTitlebar() {
		setTitle("写作（" + 1 + "/" + mProblems.size() + "）");
		
	}

	private void initView() {
		if(!App.getInstance().getSharedBoolean(Const.SLIDE_TUTOR)){
			TutorUtils.getInstance().showSlideTutor(this);
		}
		
		initData();   //这里从数据库里获得实际的数据
		
		mPagerAdapter = new ProblemPagerAdapter(this, mProblems, mPieceMap, true, 0);
		mViewPager.setAdapter(mPagerAdapter); 
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setTitle("写作（" + (mViewPager.getCurrentItem() + 1) + "/" + mProblems.size() + "）");
				App.getInstance().setCurrentValue(Const.WRITING_CURRENT, mPieces.get(mViewPager.getCurrentItem()).id);
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
		initTitlebar();
		Piece piece = mPieceMap.get(App.getInstance().getCurrentValue(Const.WRITING_CURRENT));
		mViewPager.setCurrentItem(mPieces.indexOf(piece));
	}

	private void findViews() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
	}
	
	private void initData(){
		mPieces = App.getInstance().getExamDBHelper().getPiecesByType(Problem.WRITING);
		List<String> pieceIds = new ArrayList<String>();
		for(Piece piece : mPieces){
			pieceIds.add(String.valueOf(piece.id));
		}
		mProblems = App.getInstance().getExamDBHelper().getProblemsByPieceId(pieceIds);
		for(Piece piece : mPieces){
			mPieceMap.put(piece.id, piece);
		}
		
		//for test
//		mProblems = new ArrayList<Problem>();
//		mPieces = new ArrayList<Piece>();
//		int piece_id = 0;
//		int problem_id = 0;
//		//添加写作数据
//		mPieceMap.clear();
//		for (int i = 0; i < 10; i++) {
//			mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, null));
//			mPieceMap.put(piece_id, mPieces.get(piece_id));
//			mProblems.add(new Problem(problem_id, "题目：" + problem_id + "写作", null, "写作答案", "题目：" + problem_id + "的解析", piece_id, Problem.WRITING));
//			piece_id++;
//			problem_id++;
//		}
	}

}
