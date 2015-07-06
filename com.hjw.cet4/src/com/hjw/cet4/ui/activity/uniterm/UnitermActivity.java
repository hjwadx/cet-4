package com.hjw.cet4.ui.activity.uniterm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.BaseActivity;
import com.hjw.cet4.ui.activity.practice.ProblemPagerAdapter;
import com.hjw.cet4.utils.AnimationUtils;
import com.hjw.cet4.utils.Player;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import fm.jihua.common.ui.helper.Hint;

public class UnitermActivity extends BaseActivity{
	
	private ViewPager mViewPager;

	private ProblemPagerAdapter mPagerAdapter;
	
	TextView mContent;
	Button mRightBtn;
	
	List<Problem> mProblems;
	List<Piece> mPieces;
	HashMap<Integer, Piece> mPieceMap = new HashMap<Integer, Piece>();
	
	int mType;
	
	//new
	Button mPlay; 
    private SeekBar skbProgress;
    private Player player;
    boolean isReview;
    ViewStub mViewStub;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice);
		mType = getIntent().getIntExtra("TYPE", Problem.WRITING);
		if(isReview = getIntent().getBooleanExtra("REVIEW", false)){
			mProblems = (ArrayList<Problem>) getIntent().getSerializableExtra("SUBJECTS");
			mPieceMap = (HashMap<Integer, Piece>) getIntent().getSerializableExtra("PIECES");
		}
		findViews();
		initView();
		initPlayer();
		setListener();
		initTitlebar();
	}

	private void setListener() {
		mRightBtn.setOnClickListener(l);
		if(isReview){
			mPlay.setOnClickListener(l);
			skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());  
		}
	}

	private void initPlayer() {
		String url = Environment.getExternalStorageDirectory() + "/MIUI/music/mp3/test.mp3";  
        player = new Player(url,skbProgress);
        TelephonyManager telephonyManager=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);  
        telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);  
	}

	private void initView() {
		if(!isReview){
			initData();   //这里从数据库里获得实际的数据
		}
		mPagerAdapter = new ProblemPagerAdapter(this, mProblems, mPieceMap, isReview, 0);
		mViewPager.setAdapter(mPagerAdapter); 
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
//				setTitle("词汇理解");
				if(arg0 < mProblems.size()){
					mType = mProblems.get(arg0).type;
					mContent.setText(mPieceMap.get(mProblems.get(arg0).piece_id).original);
					if(player.getUrl() == null || !player.getUrl().equals(mPieceMap.get(mProblems.get(arg0).piece_id).voice_url)){
						player.setUrl(mPieceMap.get(mProblems.get(arg0).piece_id).voice_url);
						if(isReview){
							mPlay.setText("播放");
							skbProgress.setProgress(0);
						}
					}
					if(isReview && mProblems.get(arg0).showPlayerControl()){
						findViewById(R.id.player_control).setVisibility(View.VISIBLE);
					} else if(isReview){
						findViewById(R.id.player_control).setVisibility(View.GONE);
					}
					if(mProblems.get(arg0).needOriginal(isReview)){
						mRightBtn.setVisibility(View.VISIBLE);
					} else {
						mRightBtn.setVisibility(View.GONE);
					}
				} else {
					if (isReview) {
						findViewById(R.id.player_control).setVisibility(View.GONE);
					}
				    mRightBtn.setVisibility(View.GONE);
					if(player.isPlaying()){
						player.stop();
					}
					mType = Problem.COMMIT;
					mPagerAdapter.onLastItemShow();
				}
				initTitlebar();
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
		
		//new
		if(mProblems.get(0).needOriginal(isReview)){
			mRightBtn.setVisibility(View.VISIBLE);
		} else {
			mRightBtn.setVisibility(View.GONE);
		}
		if(isReview){
			mViewStub.inflate(); 
			skbProgress = (SeekBar) findViewById(R.id.skbProgress);
			mPlay = (Button) findViewById(R.id.play);
			if(mProblems.get(0).showPlayerControl()){
				findViewById(R.id.player_control).setVisibility(View.VISIBLE);
			}
		}
	}

	private void findViews() {
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		mRightBtn = (Button) findViewById(R.id.right_btn);
		mContent = (TextView) findViewById(R.id.content);
		mViewStub = (ViewStub) findViewById(R.id.viewstub);
	}
	
	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.play: {
			    if(!player.isPlaying() && !player.isPause()){
			    	player.play();
			    	mPlay.setText("暂停");
			    } else {
			    	if(player.pause()){
			    		mPlay.setText("播放");
			    	} else {
			    		mPlay.setText("暂停");
			    	}
			    }
			}
				break;
			case R.id.right_btn: {;
				if (mContent.getVisibility() == View.GONE) {
					mContent.setVisibility(View.VISIBLE);
					mContent.setAnimation(AnimationUtils.getInstance().createEnterFromBottomAnim(new AnimationListener() {

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
								}
							}));
					mRightBtn.setText("返回题目");
				} else {
					mContent.setVisibility(View.GONE);
					mViewPager.setVisibility(View.VISIBLE);
					mContent.setAnimation(AnimationUtils.getInstance().createExitFromBottomAnim(null));
					mRightBtn.setText("查看全文");
				}
			}
				break;
			default:
				break;
			}
		}
	};
	
	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {    
        int progress;    
        @Override    
        public void onProgressChanged(SeekBar seekBar, int progress,    
                boolean fromUser) {    
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()    
            this.progress = progress * player.mediaPlayer.getDuration()    
                    / seekBar.getMax();    
        }    
    
        @Override    
        public void onStartTrackingTouch(SeekBar seekBar) {    
    
        }    
    
        @Override    
        public void onStopTrackingTouch(SeekBar seekBar) {    
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字    
            player.mediaPlayer.seekTo(progress);    
        }    
    }
	
	/** 
     * 只有电话来了之后才暂停音乐的播放 
     */  
    private final class MyPhoneListener extends android.telephony.PhoneStateListener{  
        @Override  
        public void onCallStateChanged(int state, String incomingNumber) {  
            switch (state) {  
            case TelephonyManager.CALL_STATE_RINGING://电话来了  
                player.callIsComing();  
                break;  
            case TelephonyManager.CALL_STATE_IDLE: //通话结束  
                player.callIsDown();  
                break;  
            }  
        }  
    }  
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	player.release();
    }

	private void initTitlebar() {
		String title = null;
		switch (mType) {
		case Problem.WRITING:
			title = "写作（" + (mViewPager.getCurrentItem() + 1) + "/" + mProblems.size() + "）";
			break;
		case Problem.SHORT_CONVERSATIONS:
			title = "短对话";
			break;
		case Problem.LONG_CONVERSATIONS:
			title = "长对话";
			break;
		case Problem.SHORT_PASSAGES:
			title = "短文理解";
			break;
		case Problem.PASSAGE_DICTATION:
			title = "短文听写";
			break;
		case Problem.WORDS_COMPREHENSION:
			title = "词汇理解";
			break;
		case Problem.LONG_TO_READ:
			title = "长篇阅读";
			break;
		case Problem.CAREFUL_READING:
			title = "仔细阅读";
			break;
		case Problem.TRANSLATE:
			title = "翻译（" + (mViewPager.getCurrentItem() + 1) + "/" + mProblems.size() + "）";
			break;
		case Problem.COMMIT:
			title += "-提交";
			break;
		default:
			break;
		}
		setTitle(title);
	}
	
	private void initData(){
//   	Problem(int id, String subject, String options, String answer, String analyze, int piece_id, int type)
   	    mProblems = new ArrayList<Problem>();
		mPieces = new ArrayList<Piece>();
		int piece_id = 0;
		int problem_id = 0;
		//添加写作数据
		if(mType == Problem.WRITING){
			for(int i = 0; i< 10; i++){
				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, null));
				mPieceMap.put(piece_id, mPieces.get(piece_id));
				mProblems.add(new Problem(problem_id, "题目：" + problem_id + "写作", null, "写作答案", "题目：" + problem_id + "的解析", piece_id, Problem.WRITING));
				piece_id++;
				problem_id++;
			}
		}
		
		//短对话
		if(mType == Problem.SHORT_CONVERSATIONS){
			for(int i=0; i<10; i++){
				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", "选项A;选项B;选项C;选项D", Environment.getExternalStorageDirectory() + "/MIUI/music/mp3/test.mp3"));
				mPieceMap.put(piece_id, mPieces.get(piece_id));
				mProblems.add(new Problem(problem_id, "题目：" + problem_id, "选项A;选项B;选项C;选项D", "A", "题目：" + problem_id + "的解析", piece_id, Problem.SHORT_CONVERSATIONS));
				piece_id++;
				problem_id++;
			}
		}
		
		//长对话
		if(mType == Problem.LONG_CONVERSATIONS){
			for(int j=0; j<2; j++){
				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, Environment.getExternalStorageDirectory() + "/MIUI/music/mp3/test.mp3"));
				mPieceMap.put(piece_id, mPieces.get(piece_id));
				for(int i=0; i<3; i++){
					mProblems.add(new Problem(problem_id, "题目：" + problem_id, "选项A;选项B;选项C;选项D", "A", "题目：" + problem_id + "的解析", piece_id, Problem.LONG_CONVERSATIONS));
					problem_id++;
				}
				piece_id++;
			}
		}
		
		//短文理解
		if(mType == Problem.SHORT_PASSAGES){
			for(int j=0; j<2; j++){
				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, Environment.getExternalStorageDirectory() + "/MIUI/music/mp3/test.mp3"));
				mPieceMap.put(piece_id, mPieces.get(piece_id));
				for(int i=0; i<3; i++){
					mProblems.add(new Problem(problem_id, "题目：" + problem_id, "选项A;选项B;选项C;选项D", "A", "题目：" + problem_id + "的解析", piece_id, Problem.SHORT_PASSAGES));
					problem_id++;
				}
				piece_id++;
			}
		}
		
		//PASSAGE_DICTATION 短文听写跳过
		
		//词汇理解
		if(mType == Problem.WORDS_COMPREHENSION){
			for(int j=0; j<2; j++){
				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", "选项A;选项B;选项C;选项D;选项E;选项F;选项G;选项H", null));
				mPieceMap.put(piece_id, mPieces.get(piece_id));
				for(int i=0; i<3; i++){
					mProblems.add(new Problem(problem_id, "题目：" + problem_id, null, "A", "题目：" + problem_id + "的解析", piece_id, Problem.WORDS_COMPREHENSION));
					problem_id++;
				}
				piece_id++;
			}
		}
		
		//长篇阅读
		if(mType == Problem.LONG_TO_READ){
			for(int j=0; j<2; j++){
				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", "选项A;选项B;选项C;选项D;选项E;选项F;选项G;选项H", null));
				mPieceMap.put(piece_id, mPieces.get(piece_id));
				for(int i=0; i<3; i++){
					mProblems.add(new Problem(problem_id, "题目：" + problem_id, null, "A", "题目：" + problem_id + "的解析", piece_id, Problem.LONG_TO_READ));
					problem_id++;
				}
				piece_id++;
			}
		}
		
		//仔细阅读
		if(mType == Problem.CAREFUL_READING){
			for(int j=0; j<2; j++){
				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, null));
				mPieceMap.put(piece_id, mPieces.get(piece_id));
				for(int i=0; i<3; i++){
					mProblems.add(new Problem(problem_id, "题目：" + problem_id, "选项A;选项B;选项C;选项D", "A", "题目：" + problem_id + "的解析", piece_id, Problem.CAREFUL_READING));
					problem_id++;
				}
				piece_id++;
			}
		}
		
		//翻译
		if(mType == Problem.WRITING){
			for(int i = 0; i< 10; i++){
				mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, null));
				mPieceMap.put(piece_id, mPieces.get(piece_id));
				mProblems.add(new Problem(problem_id, "题目：" + problem_id + "翻译", null, "翻译答案", "题目：" + problem_id + "的解析", piece_id, Problem.TRANSLATE));
				piece_id++;
				problem_id++;
			}
		}
   }

}
