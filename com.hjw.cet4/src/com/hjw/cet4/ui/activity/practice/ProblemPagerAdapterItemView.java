package com.hjw.cet4.ui.activity.practice;

import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class ProblemPagerAdapterItemView extends FrameLayout{
	
	protected Problem mProblem;
	protected Piece mPiece;
	protected boolean isReview;
	protected boolean isPractice;
	
	public ProblemPagerAdapterItemView(Context context) {
		super(context);
	}
	
	public ProblemPagerAdapterItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setData(Problem problem, Piece piece, boolean is_review, boolean is_practice){
		mProblem = problem;
		mPiece = piece;
		isReview = is_review;
		isPractice = is_practice;
	}

	public abstract void setupViews();
	/**
	    * 这里内存回收.外部调用.
	    */
	public abstract void recycle();
	
	/**
	    * 重新加载.外部调用.
	    */
	public abstract void reload();
	
	public static ProblemPagerAdapterItemView createByCategory(Context context, Problem problem){
		switch (problem.type) {
		case Problem.WRITING:
			return new WritingPagerItemView(context);
		case Problem.SHORT_CONVERSATIONS:
			return new ListeningPagerItemView(context);
		case Problem.LONG_CONVERSATIONS:
			return new ListeningPagerItemView(context);
		case Problem.SHORT_PASSAGES:
			return new ListeningPagerItemView(context);
		case Problem.PASSAGE_DICTATION:
			return new PassageDictationPagerItemView(context);
		case Problem.WORDS_COMPREHENSION:
			return new ListeningPagerItemView(context);
		case Problem.LONG_TO_READ:
			return new ListeningPagerItemView(context);
		case Problem.CAREFUL_READING:
			return new ListeningPagerItemView(context);
		case Problem.TRANSLATE:
			return new WritingPagerItemView(context);
		case Problem.PART:
			return new PartPagerItemView(context);
		default:
			break;
		}
		return null;
	}

}
