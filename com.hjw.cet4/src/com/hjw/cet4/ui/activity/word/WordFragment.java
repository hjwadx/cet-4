package com.hjw.cet4.ui.activity.word;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Word;
import com.hjw.cet4.utils.CommonUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import fm.jihua.common.ui.BaseFragment;

public class WordFragment extends BaseFragment{
	
	private Word mWord;

	private TextView mSubject, mAnswer, mPhonetic, mSentence, mSentence2, mSoundmark;
	// private ImageView mPlay;
	private Button mShowAnswer;
	View mAnswerParent;
	
	public static WordFragment newInstance(Word word) {
		WordFragment fragment = new WordFragment();
		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putSerializable("WORD", word);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.view_word_pager_adapter_item, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Bundle bundle = getArguments();
		mWord = (Word) bundle.getSerializable("WORD");
		
		mSubject = (TextView)view.findViewById(R.id.subject);
		mAnswer = (TextView)view.findViewById(R.id.answer);
//		mPlay = (ImageView)view.findViewById(R.id.play);
		mShowAnswer = (Button) view.findViewById(R.id.show_answer);
//		mPhonetic = (TextView)view.findViewById(R.id.phonetic);
		mSentence = (TextView)view.findViewById(R.id.sentence);
		mSentence2 = (TextView)view.findViewById(R.id.sentence2);
		mAnswerParent = view.findViewById(R.id.answer_parent);
		mSoundmark = (TextView) view.findViewById(R.id.soundmark);
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
		
		mSubject.setText(mWord.subject);
		mSoundmark.setText(mWord.soundmark);
//		mSoundmark.setText("[" + "yinbiao" + "]");
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

}
