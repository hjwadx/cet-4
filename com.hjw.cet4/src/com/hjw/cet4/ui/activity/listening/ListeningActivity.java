package com.hjw.cet4.ui.activity.listening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
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
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.BaseActivity;
import com.hjw.cet4.ui.activity.practice.ProblemPagerAdapter;
import com.hjw.cet4.utils.AnimationUtils;
import com.hjw.cet4.utils.Compatibility;
import com.hjw.cet4.utils.Const;
import com.hjw.cet4.utils.FileUtils;
import com.hjw.cet4.utils.Player;
import com.hjw.cet4.utils.TutorUtils;
import com.mozillaonline.providers.DownloadManager;

import fm.jihua.common.ui.helper.Hint;

public class ListeningActivity  extends BaseActivity{
	
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
	
	//new
	ImageView mPlay; 
    private SeekBar skbProgress;
    private Player player;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listening);
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
		initPlayer();
		initView();
		setListener();
		initTitlebar(getTitleSuffix(0));
	}

	private void setListener() {
		mPlay.setOnClickListener(l);
		skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());   
	}

	private void initPlayer() {
		String url = Environment.getExternalStorageDirectory() + "/MIUI/music/mp3/test.mp3";  
        player = new Player(url,skbProgress);
        TelephonyManager telephonyManager=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);  
        telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);  
	}

	private void initTitlebar(String suffix) {
		String title = "听力" + suffix;
		switch (mType) {
		case Problem.SHORT_CONVERSATIONS:
			title = "短对话" + suffix;
			break;
		case Problem.LONG_CONVERSATIONS:
			title = "长对话" + suffix;
			break;
		case Problem.SHORT_PASSAGES:
			title = "短文理解" + suffix;
			break;
		case Problem.PASSAGE_DICTATION:
			title = "短文听写" + suffix;
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
		
		mPagerAdapter = new ProblemPagerAdapter(this, mProblems, mPieceMap, isReview, Problem.LISTENING);
		mViewPager.setAdapter(mPagerAdapter); 
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
//				setTitle("词汇理解");
				if(arg0 < mProblems.size()){
					checkAudioFile(mPieceMap.get(mProblems.get(arg0).piece_id));
					
					mContent.setText(mPieceMap.get(mProblems.get(arg0).piece_id).original);
					if(!player.getUrl().equals(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + mPieceMap.get(mProblems.get(arg0).piece_id).getVoiceFileName())){
						refreshPlayerControl();
//						player.setUrl(mPieceMap.get(mReadings.get(arg0).piece_id).voice_url);
						player.setUrl(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + mPieceMap.get(mProblems.get(arg0).piece_id).getVoiceFileName());
						skbProgress.setProgress(0);
						mPlay.setImageResource(R.drawable.player_btn_play);
					}
					findViewById(R.id.player_control).setVisibility(View.VISIBLE);
					initTitlebar(getTitleSuffix(getPieceIndexByProblem(mProblems.get(arg0))));
//					mRightBtn.setVisibility(View.VISIBLE);
				} else {
					findViewById(R.id.player_control).setVisibility(View.GONE);
//					mRightBtn.setVisibility(View.GONE);
					if(player.isPlaying()){
						player.stop();
					}
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
		});
		
		player.setUrl(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + mPieceMap.get(mProblems.get(0).piece_id).getVoiceFileName());
		
		//new
		if(!mProblems.get(0).needOriginal(isReview)){
			mRightBtn.setVisibility(View.GONE);
		}
		findViewById(R.id.player_control).setVisibility(View.VISIBLE);
		checkAudioFile(mPieceMap.get(mProblems.get(0).piece_id));
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
	
	void checkAudioFile(final Piece piece){
//		File file = null;
		if (FileUtils.getInstance().isSDAvailable()) {
//			file = new File(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + piece.getVoiceFileName());
			if (!piece.isVoiceDownloaded() && !piece.isDownloading()) {
				piece.setDownloading(true);
				refreshPlayerControl();
				DownloadSpirit downloadSpirit = new DownloadSpirit(ListeningActivity.this, piece.getDownloadUrlString(), Const.AUDIO_DIR, piece.getVoiceFileName());
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
			Hint.showTipsLong(ListeningActivity.this, "SD卡不存在，请检查重新下载");
		}
	}

	private void findViews() {
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		mRightBtn = (Button) findViewById(R.id.right_btn);
		mContent = (TextView) findViewById(R.id.content);
		
		//new
		skbProgress = (SeekBar) findViewById(R.id.skbProgress);
		mPlay = (ImageView) findViewById(R.id.play);
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
    	player.release();
    }
    


	private void refreshPlayerControl() {
//		File file = null;
		if (FileUtils.getInstance().isSDAvailable()) {
//			file = new File(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + mPieceMap.get(mProblems.get(mViewPager.getCurrentItem()).piece_id).getVoiceFileName());
			if(mViewPager.getCurrentItem() < mProblems.size()){
				if (mPieceMap.get(mProblems.get(mViewPager.getCurrentItem()).piece_id).isVoiceDownloaded()) {
					mPlay.setImageResource(R.drawable.player_btn_play);
				} else {
					Drawable drawable = getResources().getDrawable(R.drawable.player_btn_play_77); 
					drawable.setAlpha(77);
					mPlay.setImageDrawable(drawable);
				}
			}
		}
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
//		mProblems = new ArrayList<Problem>();
//		mPieces = new ArrayList<Piece>();
//		mPieces.add(new Piece(0, "第" + 0 + "篇的原文", "选项A;选项B;选项C;选项D;选项E;选项F;选项G;选项H", "http://zhangmenshiting.baidu.com/data2/music/87914787/72657070252000128.mp3?xcode=5f74ef287771020ca646e1c4d8722169942314346824f9bb"));
//		mPieceMap.put(0, mPieces.get(0));
//		mPieces.add(new Piece(1, "第" + 1 + "篇的原文", "选项A;选项B;选项C;选项D;选项E;选项F;选项G;选项H", "http://zhangmenshiting.baidu.com/data2/music/100264921/901213261384030861128.mp3?xcode=cc9f24d8667945ff4b568db69375ca761045aeea9cee72d1"));
//		mPieceMap.put(1, mPieces.get(1));
//		for(int i=0; i<4; i++){
//			int piece_id = i < 2 ? 0 : 1;
//			mProblems.add(new Problem(i, "题目：" + i, "选项A;选项B;选项C;选项D", "A", "题目：" + i + "的解析", piece_id, mType));
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
