package com.hjw.cet4.ui.activity.listening;

import java.io.File;
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
import com.hjw.cet4.utils.AnimationUtils;
import com.hjw.cet4.utils.Const;
import com.hjw.cet4.utils.FileUtils;
import com.hjw.cet4.utils.Player;
import com.hjw.cet4.utils.TutorUtils;
import com.mozillaonline.providers.DownloadManager;

import fm.jihua.common.ui.helper.Hint;

public class DictationActivity extends BaseActivity{
	
	private ViewPager mViewPager;

	private DictationPagerAdapter mPagerAdapter;
	
	TextView mContent;
	Button mRightBtn;
	
	List<Problem> mProblems;
	List<Piece> mPieces;
	HashMap<Integer, Piece> mPieceMap = new HashMap<Integer, Piece>();
	
	//new
	ImageView mPlay; 
    private SeekBar skbProgress;
    private Player player;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listening);
		findViews();
		initPlayer();
		initView();
		setListener();
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

	private void initTitlebar() {
		setTitle("短文听写（" + 1 + "/" + mPieces.size() + "）");
	}

	private void initView() {
		if(!App.getInstance().getSharedBoolean(Const.SLIDE_TUTOR)){
			TutorUtils.getInstance().showSlideTutor(this);
		}
		
		initData();   //这里从数据库里获得实际的数据
		
		mPagerAdapter = new DictationPagerAdapter(this, mProblems, mPieces, true, 0);
		mViewPager.setAdapter(mPagerAdapter); 
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
//				setTitle("词汇理解");
				if(arg0 < mPieces.size()){
					checkAudioFile(mPieces.get(arg0));
					
					mContent.setText(mPieces.get(arg0).original);
					if(!player.getUrl().equals(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + mPieces.get(arg0).getVoiceFileName())){
						refreshPlayerControl();
//						player.setUrl(mPieceMap.get(mReadings.get(arg0).piece_id).voice_url);
						player.setUrl(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + mPieces.get(arg0).getVoiceFileName());
						skbProgress.setProgress(0);
						mPlay.setImageResource(R.drawable.player_btn_play);
					}
					findViewById(R.id.player_control).setVisibility(View.VISIBLE);
					App.getInstance().setCurrentValue(Const.PASSAGE_DICTATION_CURRENT, mPieces.get(mViewPager.getCurrentItem()).id);
					setTitle("短文听写（" + (mViewPager.getCurrentItem() + 1) + "/" + mPieces.size() + "）");
//					mRightBtn.setVisibility(View.VISIBLE);
				} else {
					findViewById(R.id.player_control).setVisibility(View.GONE);
//					mRightBtn.setVisibility(View.GONE);
					if(player.isPlaying()){
						player.stop();
					}
					mPagerAdapter.onLastItemShow();
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
		
		mContent.setText(mPieces.get(0).original);
		mRightBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mContent.getVisibility() == View.GONE){
					mContent.setVisibility(View.VISIBLE);
					mContent.setAnimation(AnimationUtils.getInstance().createEnterFromBottomAnim(new AnimationListener(){

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
				} else {
					mContent.setVisibility(View.GONE);
					mViewPager.setVisibility(View.VISIBLE);
					mContent.setAnimation(AnimationUtils.getInstance().createExitFromBottomAnim(null));
					mRightBtn.setBackgroundResource(R.drawable.btn_view);
				}
				
			}
		});
		
		player.setUrl(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + mPieceMap.get(mProblems.get(0).piece_id).getVoiceFileName());
		
		//new
//		if(!mProblems.get(0).needOriginal(isReview)){
			mRightBtn.setVisibility(View.GONE);
//		}
		findViewById(R.id.player_control).setVisibility(View.VISIBLE);
		checkAudioFile(mPieces.get(0));
		initTitlebar();
		Piece piece = mPieceMap.get(App.getInstance().getCurrentValue(Const.PASSAGE_DICTATION_CURRENT));
		mViewPager.setCurrentItem(mPieces.indexOf(piece));
	}
	
	void checkAudioFile(final Piece piece){
//		File file = null;
		if (FileUtils.getInstance().isSDAvailable()) {
//			file = new File(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + piece.getVoiceFileName());
			if (!piece.isVoiceDownloaded() && !piece.isDownloading()) {
				refreshPlayerControl();
				piece.setDownloading(true);
				DownloadSpirit downloadSpirit = new DownloadSpirit(DictationActivity.this, piece.getDownloadUrlString(), Const.AUDIO_DIR, piece.getVoiceFileName());
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
			Hint.showTipsLong(DictationActivity.this, "SD卡不存在，请检查重新下载");
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
		File file = null;
		if (FileUtils.getInstance().isSDAvailable()) {
			file = new File(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + mPieceMap.get(mProblems.get(mViewPager.getCurrentItem()).piece_id).getVoiceFileName());
			if (file.exists()) {
				mPlay.setImageResource(R.drawable.player_btn_play);
			} else {
				Drawable drawable = getResources().getDrawable(R.drawable.player_btn_play_77); 
				drawable.setAlpha(77);
				mPlay.setImageDrawable(drawable);
			}
		}
	}
	
	private void initData(){
//   	Problem(int id, String subject, String options, String answer, String analyze, int piece_id, int type)
		mPieces = App.getInstance().getExamDBHelper().getPiecesByType(Problem.PASSAGE_DICTATION);
		List<String> pieceIds = new ArrayList<String>();
		for(Piece piece : mPieces){
			pieceIds.add(String.valueOf(piece.id));
		}
		mProblems = App.getInstance().getExamDBHelper().getProblemsByPieceId(pieceIds);
		for(Piece piece : mPieces){
			mPieceMap.put(piece.id, piece);
		}

//		mProblems = new ArrayList<Problem>();
//		mPieces = new ArrayList<Piece>();
//		mPieces.add(new Piece(0, "Contrary to the old warning that time waits for no one, time slows down when you are on the move. It also slows down more as you move faster, which means astronauts (宇航员) someday may (36) _____ so long in space that they would return to an Earth of the (37) _____ future. If you could move at the speed of light, your time would stand still. If you could move faster than light, your time would move (38) _____.Although no form of matter yet (39) _____ moves as fast as or faster than light, (40) _____ experiments have already confirmed that accelerated (41) _____ causes a traveler's time to be stretched. Albert Einstein (42) _____ this in 1905, when he (43) _____ the concept of relative time as part of his Special Theory of Relativity. A search is now under way to confirm the suspected existence of particles of matter (44) ____________________________________.An obsession (沉迷) with time – saving, gaining, wasting, losing, and mastering it – (45) ____________________________________. Humanity also has been obsessed with trying to capture the meaning of time. Einstein (46) ____________________________________. Thus, time and time's relativity are measurable by any hourglass, alarm clock, or an atomic clock that can measure a billionth of a second.", "选项A;选项B;选项C;选项D;选项E;选项F;选项G;选项H", "http://zhangmenshiting.baidu.com/data2/music/87914787/72657070252000128.mp3?xcode=5f74ef287771020ca646e1c4d8722169942314346824f9bb"));
//		mPieceMap.put(0, mPieces.get(0));
//		mPieces.add(new Piece(1, "第" + 1 + "篇的原文", "选项A;选项B;选项C;选项D;选项E;选项F;选项G;选项H", "http://zhangmenshiting.baidu.com/data2/music/100264921/901213261384030861128.mp3?xcode=cc9f24d8667945ff4b568db69375ca761045aeea9cee72d1"));
//		mPieceMap.put(1, mPieces.get(1));
//		for(int i=0; i<4; i++){
//			int piece_id = i < 2 ? 0 : 1;
//			mProblems.add(new Problem(i, "题目：" + i, "选项A;选项B;选项C;选项D", "A", "题目：" + i + "的解析", piece_id, mType));
//		}
   }

}
