package com.hjw.cet4.ui.activity.reading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hjw.cet4.App;
import com.hjw.cet4.R;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.BaseActivity;
import com.hjw.cet4.ui.activity.practice.ProblemPagerAdapter;
import com.hjw.cet4.utils.AnimationUtils;
import com.hjw.cet4.utils.Compatibility;
import com.hjw.cet4.utils.Const;
import com.hjw.cet4.utils.TutorUtils;

import android.R.integer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.TextView;

public class ReadingActivity extends BaseActivity{
	
    private ViewPager mViewPager;
	
	private ProblemPagerAdapter mPagerAdapter;
	
	TextView mContent;
	Button mRightBtn;
	
	List<Problem> mProblems;
	List<Piece> mPieces;
	HashMap<Integer, Piece> mPieceMap = new HashMap<Integer, Piece>();
	
	int mType;
	int mAmount;
	boolean isReview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reading);
		mType = getIntent().getIntExtra("TYPE", 0);
		mAmount = getIntent().getIntExtra("AMOUNT", 0);
		if(isReview = getIntent().getBooleanExtra("REVIEW", false)){
			mProblems = (ArrayList<Problem>) getIntent().getSerializableExtra("SUBJECTS");
			mPieceMap = (HashMap<Integer, Piece>) getIntent().getSerializableExtra("PIECES");
			if(mProblems != null && mProblems.size() > 0){
				mType = mProblems.get(0).type;
			}
			mPieces = new ArrayList<Piece>();
			int pieceId = 0;
			for(Problem problem : mProblems){
				if(pieceId != problem.piece_id){
					pieceId = problem.piece_id;
					mPieces.add(mPieceMap.get(pieceId));
				}
			}
			mAmount = mPieces.size();
		}
		findViews();
		initView();
		initTitlebar(getTitleSuffix(0));
	}

	private void initTitlebar(String suffix) {
		String title = "阅读" + suffix;
		switch (mType) {
		case Problem.WORDS_COMPREHENSION:
			title = "词汇理解" + suffix;
			break;
		case Problem.LONG_TO_READ:
			title = "长篇阅读" + suffix;
			break;
		case Problem.CAREFUL_READING:
			title = "仔细阅读" + suffix;
			break;
		default:
			break;
		}
		setTitle(title);
	}

	private void initView() {
		if(!App.getInstance().getSharedBoolean(Const.SLIDE_TUTOR)){
			TutorUtils.getInstance().showSlideTutor(this);
		}
		
		if(!isReview){
			initData();   //这里从数据库里获得实际的数据
		}
		
		mPagerAdapter = new ProblemPagerAdapter(this, mProblems, mPieceMap, isReview, Problem.READING);
		mViewPager.setAdapter(mPagerAdapter); 
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
//				setTitle("词汇理解");
				if(arg0 < mProblems.size()){
					mContent.setText(mPieceMap.get(mProblems.get(arg0).piece_id).original);
					mRightBtn.setVisibility(View.VISIBLE);
					initTitlebar(getTitleSuffix(getPieceIndexByProblem(mProblems.get(arg0))));
				} else {
					mRightBtn.setVisibility(View.GONE);
					mPagerAdapter.onLastItemShow();
					initTitlebar("");
				}
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
		
		mContent.setText(mPieceMap.get(mProblems.get(0).piece_id).original);
		mRightBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((View)mContent.getParent()).getVisibility() == View.GONE){
					((View)mContent.getParent()).setVisibility(View.VISIBLE);
					((View)mContent.getParent()).setAnimation(AnimationUtils.getInstance().createEnterFromBottomAnim(new AnimationListener(){

						@Override
						public void onAnimationEnd(Animation animation) {
							mViewPager.setVisibility(View.GONE);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}}));
					mRightBtn.setBackgroundResource(R.drawable.btn_view_back);
					getKechengActionBar().getLeftTextButton().setVisibility(View.GONE);
				} else {
					((View)mContent.getParent()).setVisibility(View.GONE);
					mViewPager.setVisibility(View.VISIBLE);
					getKechengActionBar().getLeftTextButton().setVisibility(View.VISIBLE);
					((View)mContent.getParent()).setAnimation(AnimationUtils.getInstance().createExitFromBottomAnim(null));
					mRightBtn.setBackgroundResource(R.drawable.btn_view);
				}
				
			}
		});
	}
	
	private String getTitleSuffix(int index){
		if(mAmount < 2){
			return "";
		}
		int current = index + 1;
		return "（" + current + "/"  + mAmount + "）";
	}
	
	private int getPieceIndexByProblem(Problem problem){
		return mPieces.indexOf(mPieceMap.get(problem.piece_id));
	}

	private void findViews() {
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		mRightBtn = (Button) findViewById(R.id.right_btn);
		mContent = (TextView) findViewById(R.id.content);
	}
	
	private void initData(){
//   	Problem(int id, String subject, String options, String answer, String analyze, int piece_id, int type)
		int lastId = App.getInstance().getCurrentValue(Problem.getTypeCurrentString(mType));
//		mProblems = App.getInstance().getExamDBHelper().getProblemsByType(mType);
		mPieces = App.getInstance().getExamDBHelper().getPiecesByType(mType);
		List<String> pieceIds = new ArrayList<String>();
		mPieces = Piece.getNextPieces(lastId, mPieces, mAmount);
		for(Piece piece : mPieces){
			pieceIds.add(String.valueOf(piece.id));
		}
//		App.getInstance().setCurrentValue(Problem.getTypeCurrentString(mType), mPieces.get(mPieces.size() - 1).id);
		mProblems = App.getInstance().getExamDBHelper().getProblemsByPieceId(pieceIds);
		for(Piece piece : mPieces){
			mPieceMap.put(piece.id, piece);
		}
		
		//for test
//   	    mProblems = new ArrayList<Problem>();
//		mPieces = new ArrayList<Piece>();
//		int piece_id = 0;
//		int problem_id = 0;
//		//添加写作数据
//		if(mType == Problem.WRITING){
//			for(int i = 0; i< 10; i++){
//				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, null));
//				mPieceMap.put(piece_id, mPieces.get(piece_id));
//				mProblems.add(new Problem(problem_id, "题目：" + problem_id + "写作", null, "写作答案", "题目：" + problem_id + "的解析", piece_id, Problem.WRITING));
//				piece_id++;
//				problem_id++;
//			}
//		}
//		
//		//短对话
//		if(mType == Problem.SHORT_CONVERSATIONS){
//			for(int i=0; i<10; i++){
//				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", "选项A;选项B;选项C;选项D", Environment.getExternalStorageDirectory() + "/MIUI/music/mp3/test.mp3"));
//				mPieceMap.put(piece_id, mPieces.get(piece_id));
//				mProblems.add(new Problem(problem_id, "题目：" + problem_id, "选项A;选项B;选项C;选项D", "A", "题目：" + problem_id + "的解析", piece_id, Problem.SHORT_CONVERSATIONS));
//				piece_id++;
//				problem_id++;
//			}
//		}
//		
//		//长对话
//		if(mType == Problem.LONG_CONVERSATIONS){
//			for(int j=0; j<2; j++){
//				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, Environment.getExternalStorageDirectory() + "/MIUI/music/mp3/test.mp3"));
//				mPieceMap.put(piece_id, mPieces.get(piece_id));
//				for(int i=0; i<3; i++){
//					mProblems.add(new Problem(problem_id, "题目：" + problem_id, "选项A;选项B;选项C;选项D", "A", "题目：" + problem_id + "的解析", piece_id, Problem.LONG_CONVERSATIONS));
//					problem_id++;
//				}
//				piece_id++;
//			}
//		}
//		
//		//短文理解
//		if(mType == Problem.SHORT_PASSAGES){
//			for(int j=0; j<2; j++){
//				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, Environment.getExternalStorageDirectory() + "/MIUI/music/mp3/test.mp3"));
//				mPieceMap.put(piece_id, mPieces.get(piece_id));
//				for(int i=0; i<3; i++){
//					mProblems.add(new Problem(problem_id, "题目：" + problem_id, "选项A;选项B;选项C;选项D", "A", "题目：" + problem_id + "的解析", piece_id, Problem.SHORT_PASSAGES));
//					problem_id++;
//				}
//				piece_id++;
//			}
//		}
//		
//		//PASSAGE_DICTATION 短文听写跳过
//		
//		//词汇理解
//		if(mType == Problem.WORDS_COMPREHENSION){
//			for(int j=0; j<2; j++){
//				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", "选项A;选项B;选项C;选项D;选项E;选项F;选项G;选项H", null));
//				mPieceMap.put(piece_id, mPieces.get(piece_id));
//				for(int i=0; i<3; i++){
//					mProblems.add(new Problem(problem_id, "题目：" + problem_id, null, "A", "题目：" + problem_id + "的解析", piece_id, Problem.WORDS_COMPREHENSION));
//					problem_id++;
//				}
//				piece_id++;
//			}
//		}
//		
//		//长篇阅读
//		if(mType == Problem.LONG_TO_READ){
//			for(int j=0; j<2; j++){
//				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", "选项A;选项B;选项C;选项D;选项E;选项F;选项G;选项H", null));
//				mPieceMap.put(piece_id, mPieces.get(piece_id));
//				for(int i=0; i<3; i++){
//					mProblems.add(new Problem(problem_id, "题目：" + problem_id, null, "A", "题目：" + problem_id + "的解析", piece_id, Problem.LONG_TO_READ));
//					problem_id++;
//				}
//				piece_id++;
//			}
//		}
//		
//		//仔细阅读
//		if(mType == Problem.CAREFUL_READING){
//			for(int j=0; j<2; j++){
//				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, null));
//				mPieceMap.put(piece_id, mPieces.get(piece_id));
//				for(int i=0; i<3; i++){
//					mProblems.add(new Problem(problem_id, "题目：" + problem_id, "选项A;选项B;选项C;选项D", "A", "题目：" + problem_id + "的解析", piece_id, Problem.CAREFUL_READING));
//					problem_id++;
//				}
//				piece_id++;
//			}
//		}
//		
//		//翻译
//		if(mType == Problem.WRITING){
//			for(int i = 0; i< 10; i++){
//				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, null));
//				mPieceMap.put(piece_id, mPieces.get(piece_id));
//				mProblems.add(new Problem(problem_id, "题目：" + problem_id + "翻译", null, "翻译答案", "题目：" + problem_id + "的解析", piece_id, Problem.TRANSLATE));
//				piece_id++;
//				problem_id++;
//			}
//		}
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
		if(((View)mContent.getParent()).getVisibility() == View.GONE){
			finish();
		} else {
			Compatibility.callOnClick(mRightBtn);
		}
	}

}
