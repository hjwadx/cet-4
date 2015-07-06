package com.hjw.cet4.ui.activity.practice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hjw.cet4.App;
import com.hjw.cet4.R;
import com.hjw.cet4.download.DownloadSpirit;
import com.hjw.cet4.download.DownloadSpirit.OnDownloadListener;
import com.hjw.cet4.entities.Paper;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.BaseActivity;
import com.hjw.cet4.utils.AnimationUtils;
import com.hjw.cet4.utils.Compatibility;
import com.hjw.cet4.utils.Const;
import com.hjw.cet4.utils.FileUtils;
import com.hjw.cet4.utils.MyCountDownTimer;
import com.hjw.cet4.utils.Player;
import com.hjw.cet4.utils.TutorUtils;
import com.hjw.cet4.utils.MyCountDownTimer.TimeChangeListener;
import com.mozillaonline.providers.DownloadManager;
import com.umeng.analytics.MobclickAgent;

import fm.jihua.common.ui.helper.Hint;

public class PracticeActivity extends BaseActivity{
	
	private ViewPager mViewPager;

	private ProblemPagerAdapter mPagerAdapter;
	
	TextView mContent;
	Button mRightBtn, mBottomBtn;
	
	List<Problem> mProblems;
	HashMap<Integer, Piece> mPieceMap = new HashMap<Integer, Piece>();
	Paper mPaper;
	
	int mType = Problem.WRITING;
	
	//new
	ImageView mPlay; 
    private SeekBar skbProgress;
    private Player player;
    boolean isReview;
    ViewStub mViewStub;
    
    private MyCountDownTimer mc;
    
    final int WRITING = 1;
    final int OTHER = 2;
    final int WRITING_TIME = 30 * 60 * 1000;
    final int OTHER_TIME = 90 * 60 * 1000;
    
    int mState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice);
		if(isReview = getIntent().getBooleanExtra("REVIEW", false)){
			mPaper = (Paper) getIntent().getSerializableExtra("PAPER");
			mProblems = (ArrayList<Problem>) getIntent().getSerializableExtra("SUBJECTS");
			if(mProblems == null || mProblems.size() < 1){
				initData(true);
			} else {
				mPieceMap = (HashMap<Integer, Piece>) getIntent().getSerializableExtra("PIECES");
			}
			mType = mProblems.get(0).type;
		} else {
			mPaper = (Paper) getIntent().getSerializableExtra("PAPER");
		}
		findViews();
		initView();
		setListener();
		initTitlebar();
	}

	private void setListener() {
		mRightBtn.setOnClickListener(l);
		if(isReview){
			mPlay.setOnClickListener(l);
			skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());  
		} else {
			mBottomBtn.setOnClickListener(l);
		}
	}

	private void initPlayer() {
		String url = Environment.getExternalStorageDirectory() + "/MIUI/music/mp3/test.mp3";  
        player = new Player(url,skbProgress);
        TelephonyManager telephonyManager=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);  
        telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);  
	}
	
	void setTime(String string){
		getKechengActionBar().setRightText(string);
	}

	private void initView() {
		if(!App.getInstance().getSharedBoolean(Const.SLIDE_TUTOR)){
			TutorUtils.getInstance().showSlideTutor(this);
		}
		
		if(!isReview){
			initData(false);   //这里从数据库里获得实际的数据
			insertPart();
			mPagerAdapter = new ProblemPagerAdapter(this, new ArrayList<Problem>(mProblems.subList(0, 2)), mPieceMap, isReview, Problem.PRACTICE);
			mState = WRITING;
			setCountDown(WRITING_TIME);
		} else {
			mPagerAdapter = new ProblemPagerAdapter(this, mProblems, mPieceMap, isReview, Problem.PRACTICE);
		}
		mViewPager.setAdapter(mPagerAdapter); 
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
//				setTitle("词汇理解");
				arg0 += mPagerAdapter.getStart();
				if(arg0 < mProblems.size()){
					mType = mProblems.get(arg0).type;
					if(mType == Problem.PART){
						mType = mProblems.get(arg0 + 1).type;
					}
					mContent.setText(mPieceMap.get(mProblems.get(arg0).piece_id).original);
					if(isReview && mProblems.get(arg0).showPlayerControl()){
						checkAudioFile(mPieceMap.get(mProblems.get(arg0).piece_id));
						if(!player.getUrl().equals(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + mPieceMap.get(mProblems.get(arg0).piece_id).getVoiceFileName())){
//							player.setUrl(mPieceMap.get(mReadings.get(arg0).piece_id).voice_url);
							refreshPlayerControl();
							player.setUrl(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + mPieceMap.get(mProblems.get(arg0).piece_id).getVoiceFileName());
							skbProgress.setProgress(0);
							mPlay.setImageResource(R.drawable.player_btn_play);
						}
					} else if (isReview){
						player.setUrl("");
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
					if(!isReview && mProblems.get(arg0).type == Problem.WRITING){
						mBottomBtn.setVisibility(View.VISIBLE);
					} else {
						mBottomBtn.setVisibility(View.GONE);
					}
				} else {
					if (isReview) {
						findViewById(R.id.player_control).setVisibility(View.GONE);
						if(player.isPlaying()){
							player.stop();
						}
					}
				    mRightBtn.setVisibility(View.GONE);
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
			mPlay = (ImageView) findViewById(R.id.play);
			initPlayer();
			if(mProblems.get(0).showPlayerControl()){
				findViewById(R.id.player_control).setVisibility(View.VISIBLE);
				player.setUrl(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + mPieceMap.get(mProblems.get(0).piece_id).getVoiceFileName());
				checkAudioFile(mPieceMap.get(mProblems.get(0).piece_id));
			}
		}
	}
	

	
	void checkAudioFile(final Piece piece){
//		File file = null;
		if (FileUtils.getInstance().isSDAvailable()) {
//			file = new File(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + piece.getVoiceFileName());
			if (!piece.isVoiceDownloaded()) {
				piece.setDownloading(true);
				refreshPlayerControl();
				DownloadSpirit downloadSpirit = new DownloadSpirit(PracticeActivity.this, piece.getDownloadUrlString(), Const.AUDIO_DIR, piece.getVoiceFileName());
				downloadSpirit.start(new OnDownloadListener() {

					@Override
					public void statusChanged(int statusType, int precent, int current, int total) {
						if(statusType == DownloadManager.STATUS_SUCCESSFUL || statusType == DownloadManager.STATUS_FAILED || statusType == DownloadManager.STATUS_PAUSED){
//							dialogDown.cancel();
							piece.setDownloading(false);
							refreshPlayerControl();
						}
						if(statusType == DownloadManager.STATUS_SUCCESSFUL){
//							refreshListener.onRefresh();
//							sendBroadcast();
						}
					}
				});
			}
		} else {
			Hint.showTipsLong(PracticeActivity.this, "SD卡不存在，请检查重新下载");
		}
	}
	
	private void refreshPlayerControl() {
//		File file = null;
		if (FileUtils.getInstance().isSDAvailable()) {
//			file = new File(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + mPieceMap.get(mProblems.get(mViewPager.getCurrentItem()).piece_id).getVoiceFileName());
			if (mPieceMap.get(mProblems.get(mViewPager.getCurrentItem()).piece_id).isVoiceDownloaded()) {
				mPlay.setImageResource(R.drawable.player_btn_play);
			} else {
				Drawable drawable = getResources().getDrawable(R.drawable.player_btn_play_77); 
				drawable.setAlpha(77);
				mPlay.setImageDrawable(drawable);
			}
		}
	}

	private void findViews() {
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		mRightBtn = (Button) findViewById(R.id.right_btn);
		mBottomBtn = (Button) findViewById(R.id.bottom_btn);
		mContent = (TextView) findViewById(R.id.content);
		mViewStub = (ViewStub) findViewById(R.id.viewstub);
	}
	
	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.play: {
			    if(!mPieceMap.get(mProblems.get(mViewPager.getCurrentItem()).piece_id).isVoiceDownloaded()){
			    	return;
			    }
			    if(!player.isPlaying() && !player.isPause()){
			    	player.play();
			    	mPlay.setImageResource(R.drawable.player_btn_pause);
			    } else {
			    	if(player.pause()){
			    		mPlay.setImageResource(R.drawable.player_btn_play);
			    	} else {
			    		mPlay.setImageResource(R.drawable.player_btn_pause);
			    	}
			    }
			}
				break;
			case R.id.right_btn: {
				if (((View)mContent.getParent()).getVisibility() == View.GONE) {
					((View)mContent.getParent()).setVisibility(View.VISIBLE);
					((View)mContent.getParent()).setAnimation(AnimationUtils.getInstance().createEnterFromBottomAnim(new AnimationListener() {

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
					getKechengActionBar().getLeftTextButton().setVisibility(View.GONE);
					mRightBtn.setBackgroundResource(R.drawable.btn_view_back);
				} else {
					((View)mContent.getParent()).setVisibility(View.GONE);
					mViewPager.setVisibility(View.VISIBLE);
					getKechengActionBar().getLeftTextButton().setVisibility(View.VISIBLE);
					((View)mContent.getParent()).setAnimation(AnimationUtils.getInstance().createExitFromBottomAnim(null));
					mRightBtn.setBackgroundResource(R.drawable.btn_view);
				}
			}
				break;
			case R.id.bottom_btn: {
				mPagerAdapter = new ProblemPagerAdapter(PracticeActivity.this, mProblems, mPieceMap, isReview, Problem.PRACTICE);
				mPagerAdapter.setStart(2);
				mViewPager.removeAllViews();
				mViewPager.setAdapter(mPagerAdapter);
				mBottomBtn.setVisibility(View.GONE);
				mViewPager.setCurrentItem(1);
				mViewPager.setCurrentItem(0);
				mState = OTHER;
				setCountDown(OTHER_TIME);
				MobclickAgent.onEvent(PracticeActivity.this, "event_submit_writing_simulate", mPaper.name);
				
				//开始播放音频
				player = new Player(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + mPaper.getVoiceFileName(), null);
				player.play();
			}
				break;
			default:
				break;
			}
		}
	};
	
	void setCountDown(int milliseconds){
		if(mc != null){
			mc.cancel();
		}
		mc = new MyCountDownTimer(milliseconds, 1000, mTimeChangeListener);
		mc.start();
	}
	
	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
			this.progress = progress;
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
    	if(player != null){
        	player.release();
    	}
    	if(mc != null){
    		mc.cancel();
    		mc = null;
    	}
    }

	private void initTitlebar() {
		String title = "真题模考";
		switch (mType) {
		case Problem.WRITING:
			title += "-写作";
			break;
		case Problem.SHORT_CONVERSATIONS:
		case Problem.LONG_CONVERSATIONS:
		case Problem.SHORT_PASSAGES:
		case Problem.PASSAGE_DICTATION:
			title += "-听力";
			break;
		case Problem.WORDS_COMPREHENSION:
		case Problem.LONG_TO_READ:
		case Problem.CAREFUL_READING:
			title += "-阅读";
			break;
		case Problem.TRANSLATE:
			title += "-翻译";
			break;
		case Problem.COMMIT:
			title += "-提交";
			break;
		default:
			break;
		}
		setTitle(title);
	}
    
    private void initData(boolean withResult){
    	mProblems = ParcticeUtils.getProblemsByPaper(mPaper, withResult);
    	mPieceMap = ParcticeUtils.getPiecesMapByPaper(mPaper);
    	
    	//for test
//    	Problem(int id, String subject, String options, String answer, String analyze, int piece_id, int type)
//    	mProblems = new ArrayList<Problem>();
//		mPieces = new ArrayList<Piece>();
//		int piece_id = 0;
//		int problem_id = 0;
//		//添加写作数据
//		mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, null));
//		mPieceMap.put(piece_id, mPieces.get(piece_id));
//		mProblems.add(new Problem(problem_id, "题目：" + problem_id + "写作", null, "写作答案", "题目：" + problem_id + "的解析", piece_id, Problem.WRITING));
//		piece_id++;
//		problem_id++;
//		
//		//短对话
//		for(int i=0; i<1; i++){
//			mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", "选项A;选项B;选项C;选项D", "http://zhangmenshiting.baidu.com/data2/music/87914787/72657070252000128.mp3?xcode=5f74ef287771020ca646e1c4d8722169942314346824f9bb"));
//			mPieceMap.put(piece_id, mPieces.get(piece_id));
//			mProblems.add(new Problem(problem_id, "题目：" + problem_id, "选项A;选项B;选项C;选项D", "A", "题目：" + problem_id + "的解析", piece_id, Problem.SHORT_CONVERSATIONS));
//			piece_id++;
//			problem_id++;
//		}
//		
//		//长对话
//		mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, "http://zhangmenshiting.baidu.com/data2/music/100264921/901213261384030861128.mp3?xcode=cc9f24d8667945ff4b568db69375ca761045aeea9cee72d1"));
//		mPieceMap.put(piece_id, mPieces.get(piece_id));
//		for(int i=0; i<3; i++){
//			mProblems.add(new Problem(problem_id, "题目：" + problem_id, "选项A;选项B;选项C;选项D", "A", "题目：" + problem_id + "的解析", piece_id, Problem.LONG_CONVERSATIONS));
//			problem_id++;
//		}
//		piece_id++;
//		
//		//短文理解
//		mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, "http://zhangmenshiting.baidu.com/data2/music/87914787/72657070252000128.mp3?xcode=5f74ef287771020ca646e1c4d8722169942314346824f9bb"));
//		mPieceMap.put(piece_id, mPieces.get(piece_id));
//		for(int i=0; i<3; i++){
//			mProblems.add(new Problem(problem_id, "题目：" + problem_id, "选项A;选项B;选项C;选项D", "A", "题目：" + problem_id + "的解析", piece_id, Problem.SHORT_PASSAGES));
//			problem_id++;
//		}
//		piece_id++;
//		
//		//PASSAGE_DICTATION 短文听写跳过
//		
//		//词汇理解
//		mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", "选项A;选项B;选项C;选项D;选项E;选项F;选项G;选项H", null));
//		mPieceMap.put(piece_id, mPieces.get(piece_id));
//		for(int i=0; i<3; i++){
//			mProblems.add(new Problem(problem_id, "题目：" + problem_id, null, "A", "题目：" + problem_id + "的解析", piece_id, Problem.WORDS_COMPREHENSION));
//			problem_id++;
//		}
//		piece_id++;
//		
//		//长篇阅读
//		mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", "选项A;选项B;选项C;选项D;选项E;选项F;选项G;选项H", null));
//		mPieceMap.put(piece_id, mPieces.get(piece_id));
//		for(int i=0; i<3; i++){
//			mProblems.add(new Problem(problem_id, "题目：" + problem_id, null, "A", "题目：" + problem_id + "的解析", piece_id, Problem.LONG_TO_READ));
//			problem_id++;
//		}
//		piece_id++;
//		
//		//仔细阅读
//		mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, null));
//		mPieceMap.put(piece_id, mPieces.get(piece_id));
//		for(int i=0; i<3; i++){
//			mProblems.add(new Problem(problem_id, "题目：" + problem_id, "选项A;选项B;选项C;选项D", "A", "题目：" + problem_id + "的解析", piece_id, Problem.CAREFUL_READING));
//			problem_id++;
//		}
//		piece_id++;
//		
//		//翻译
//		mPieces.add(new Piece(piece_id, "第" + piece_id + "篇的原文", null, null));
//		mPieceMap.put(piece_id, mPieces.get(piece_id));
//		mProblems.add(new Problem(problem_id, "题目：" + problem_id + "翻译", null, "翻译答案", "题目：" + problem_id + "的解析", piece_id, Problem.TRANSLATE));
//		piece_id++;
//		problem_id++;
    }
    
    void insertPart(){
    	List<Problem> problems = new ArrayList<Problem>();
    	problems.addAll(mProblems);
    	mProblems.clear();
    	int piece_id = -1;
    	int lastType = 0;
    	for(Problem problem : problems){
    		if(lastType != problem.type){
    			mProblems.add(new Problem(0, Problem.getPartDirections(problem.type), null, null, null, piece_id, Problem.PART));
    			mPieceMap.put(piece_id, new Piece(piece_id, "Part XX type = " + problem.type, null, null));
    			piece_id--;
    		}
    		mProblems.add(problem);
    		lastType = problem.type;
    	}
    }
    
    TimeChangeListener mTimeChangeListener = new TimeChangeListener(){

		@Override
		public void onTick(long millisUntilFinished) {
			SimpleDateFormat sdf= new SimpleDateFormat("HH:mm:ss");
        	sdf.setTimeZone(TimeZone.getTimeZone("GMT")); 
        	java.util.Date dt = new Date(millisUntilFinished); 
        	setTime(sdf.format(dt));
		}

		@Override
		public void onFinish() {
			showTimeOverDialog();
		}
    	
    };
    
    private void showTimeOverDialog() {
    	DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return false; // 默认返回 false
				}
			}
    		
    	};
    	
    	new AlertDialog.Builder(this).setTitle("时间到了")
    	.setMessage("时间到了")
    	.setCancelable(false)
    	.setOnKeyListener(onKeyListener)
    	.setNegativeButton("提交", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(mState == WRITING){
					MobclickAgent.onEvent(PracticeActivity.this, "event_submit_writing_simulate", mPaper.name);
					mBottomBtn.performClick();
					dialog.dismiss();
		    	} else if (mState == OTHER){
		    		Intent intent = new Intent(PracticeActivity.this, PracticeResultActivity.class);
					List<Problem> problems = new ArrayList<Problem>();
					for(Problem problem : mProblems){
						if(problem.type != Problem.PART){
							problems.add(problem);
						}
					}
					intent.putExtra("SUBJECTS", new ArrayList<Problem>(problems));
					intent.putExtra("PIECES", mPieceMap);
					startActivity(intent);
					MobclickAgent.onEvent(PracticeActivity.this, "event_submit_all_simulate", mPaper.name);
					dialog.dismiss();
					finish();
		    	}
			}
		}).create().show();
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
