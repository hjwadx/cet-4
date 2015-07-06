package com.hjw.cet4.ui.activity.practice;

import java.util.ArrayList;
import java.util.List;

import com.hjw.cet4.App;
import com.hjw.cet4.R;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.utils.CommonUtils;
import com.hjw.cet4.utils.Const;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PassageDictationPagerItemView extends ProblemPagerAdapterItemView{
	
	protected List<Problem> mProblems;
	
	private TextView mOriginal, mPassage, mNumber, mAnswer;
	private Button mShowAnswer;
	View mAnswerParent;
	
	public PassageDictationPagerItemView(Context context) {
		super(context);
		setupViews();
	}
	
	public PassageDictationPagerItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}
	
	@Override
	public void setData(Problem problem, Piece piece, boolean is_review, boolean is_practice) {
		super.setData(problem, piece, is_review, is_practice);
		List<String> idsList = new ArrayList<String>();
		idsList.add(mPiece.id + "");
		mProblems = App.getInstance().getExamDBHelper().getProblemsByPieceId(idsList);
		
		if(!Const.TEST){
			mPassage.setVisibility(View.GONE);
		}
		mPassage.setText("passage " + mPiece.id);
		mNumber.setText(mProblems.get(0).num + "-" + mProblems.get(mProblems.size() - 1).num);
		((View)mPassage.getParent()).setVisibility(View.VISIBLE);
		mOriginal.setText(mPiece.original.trim());
		mAnswerParent.setVisibility(View.GONE);
		mShowAnswer.setText("显示答案");
		mAnswer.setText(getAnswer());
		
		if(isReview){
			mShowAnswer.setVisibility(View.VISIBLE);
		} else {
			mShowAnswer.setVisibility(View.GONE);
		}
		
	}
	
	private String getAnswer() {
		String result = "";
		int i = 1;
		for(Problem problem : mProblems){
			result+= i + ":" +  CommonUtils.replaceNullString(problem.answer) + (i == mProblems.size()? "" :"\n\n");
			i++;
		}
		return result;
	}

	@Override
	public void setupViews() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.view_dictation_item, null);
		mOriginal = (TextView)view.findViewById(R.id.original);
		mPassage = (TextView)view.findViewById(R.id.passage);
		mNumber = (TextView)view.findViewById(R.id.number);
		mAnswer = (TextView)view.findViewById(R.id.answer);
		mShowAnswer = (Button) view.findViewById(R.id.show_answer);
		mAnswerParent = view.findViewById(R.id.answer_parent);
		mShowAnswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAnswerParent.getVisibility() == View.VISIBLE) {
					mAnswerParent.setVisibility(View.GONE);
					mShowAnswer.setText("显示答案");
				} else {
					mAnswerParent.setVisibility(View.VISIBLE);
					mShowAnswer.setText("隐藏答案");
				}

			}
		});
		addView(view);
	}

	@Override
	public void recycle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reload() {
		mAnswerParent.setVisibility(View.GONE);
		mShowAnswer.setText("显示答案");
	}

}
