package com.hjw.cet4.ui.activity.practice;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.widget.CachedImageView;
import com.hjw.cet4.utils.CommonUtils;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WritingPagerItemView extends ProblemPagerAdapterItemView{
	
	private TextView mSubject, mAnswer;
	private CachedImageView picture;
	private Button mShowAnswer;
	
	public WritingPagerItemView(Context context) {
		super(context);
		setupViews();
	}
	
	public WritingPagerItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	@Override
	public void setupViews() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.view_writing_pager_adapter_item, null);
		mSubject = (TextView)view.findViewById(R.id.subject);
		mAnswer = (TextView)view.findViewById(R.id.answer);
		picture = (CachedImageView)view.findViewById(R.id.picture);
		mShowAnswer = (Button) view.findViewById(R.id.show_answer);
		mShowAnswer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mAnswer.getVisibility() == View.VISIBLE){
					mAnswer.setVisibility(View.GONE);
					mShowAnswer.setText("显示答案");
				} else {
					mAnswer.setVisibility(View.VISIBLE);
					mShowAnswer.setText("隐藏答案");
				}
				
			}
		});
		addView(view);
		
	}

	@Override
	public void setData(Problem problem, Piece piece, boolean is_review, boolean is_practice) {
		super.setData(problem, piece, is_review, is_practice);
		mSubject.setText(mProblem.subject);
		mAnswer.setText(mProblem.answer);
		mAnswer.setVisibility(View.GONE);
		if(!isReview && isPractice){
			mShowAnswer.setVisibility(View.GONE);
		} else {
			mShowAnswer.setVisibility(View.VISIBLE);
		}
		mShowAnswer.setText("显示答案");
		if(!CommonUtils.isNullString(mPiece.getDownloadUrlString())){
			picture.setImageURI(Uri.parse(mPiece.getDownloadUrlString()));
			picture.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public void recycle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reload() {
		if(isReview || !isPractice){
			mAnswer.setVisibility(View.GONE);
			mShowAnswer.setText("显示答案");
		}
	}

}
