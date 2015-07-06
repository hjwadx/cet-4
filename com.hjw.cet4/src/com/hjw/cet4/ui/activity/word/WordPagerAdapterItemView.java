package com.hjw.cet4.ui.activity.word;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Word;
import com.hjw.cet4.utils.CommonUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class WordPagerAdapterItemView extends FrameLayout{
	
    private Word mWord;
	
	private TextView mSubject, mAnswer, mPhonetic, mSentence, mSentence2;
//	private ImageView mPlay;
	private Button mShowAnswer;
	View mAnswerParent;

	public WordPagerAdapterItemView(Context context) {
		super(context);
		setupViews();
	}
	
	public WordPagerAdapterItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		 setupViews();
	}
	
	//初始化View.
	private void setupViews() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.view_word_pager_adapter_item, null);
		mSubject = (TextView)view.findViewById(R.id.subject);
		mAnswer = (TextView)view.findViewById(R.id.answer);
//		mPlay = (ImageView)view.findViewById(R.id.play);
		mShowAnswer = (Button) view.findViewById(R.id.show_answer);
//		mPhonetic = (TextView)view.findViewById(R.id.phonetic);
		mSentence = (TextView)view.findViewById(R.id.sentence);
		mSentence2 = (TextView)view.findViewById(R.id.sentence2);
		mAnswerParent = view.findViewById(R.id.answer_parent);
		// mAlbumImageView = (ImageView)view.findViewById(R.id.album_imgview);
		// mALbumNameTextView = (TextView)view.findViewById(R.id.album_name);
		mShowAnswer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mAnswerParent.getVisibility() == View.VISIBLE){
					mAnswerParent.setVisibility(View.GONE);
					mShowAnswer.setText("显示释义/例句");
				} else {
					mAnswerParent.setVisibility(View.VISIBLE);
					mShowAnswer.setText("隐藏释义/例句");
				}
				
			}
		});
//		mPlay.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Hint.showTipsShort(getContext(), "播放语音");
//			}
//		});
		addView(view);
	}
	
	/**
	    * 填充数据，共外部调用.
	    * @param object
	    */
	public void setData(Word word) {
		this.mWord = word;
		mSubject.setText(mWord.subject);
		mAnswer.setText(mWord.answer);
		mSentence.setText(mWord.sentence1.trim());
//		mPhonetic.setText(mWord.phonetic);
		mAnswerParent.setVisibility(View.GONE);
		mShowAnswer.setText("显示释义/例句");
		if(CommonUtils.isNullString(mWord.sentence2)){
			mSentence2.setVisibility(View.GONE);
		} else {
			mSentence2.setText(mWord.sentence2.trim());
		}
	}
	
	/**
	    * 这里内存回收.外部调用.
	    */
	public void recycle() {
//		
	}
	
	/**
	    * 重新加载.外部调用.
	    */
	public void reload() {
		mAnswerParent.setVisibility(View.GONE);
		mShowAnswer.setText("显示释义/例句");
	}

}
