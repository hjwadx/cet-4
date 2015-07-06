package com.hjw.cet4.ui.activity.practice;

import java.util.HashMap;
import java.util.List;

import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.listening.ListeningCompletenssItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class CompletenessItemView extends FrameLayout{
	
	protected List<Problem> mProblems;
	protected HashMap<Integer, Piece> mPieceMap;
	
	public CompletenessItemView(Context context) {
		super(context);
	}
	
	public CompletenessItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setData( List<Problem> problems, HashMap<Integer, Piece> piece_map) {
		mProblems = problems;
		mPieceMap = piece_map;
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
	
	public static CompletenessItemView createByCategory(Context context, int type){
		switch (type) {
		case Problem.PRACTICE:
			return new PracticeCompletenessItemView(context);
		case Problem.READING:
			return new ListeningCompletenssItemView(context, type);
		case Problem.LISTENING:
			return new ListeningCompletenssItemView(context, type);
		default:
			return new PracticeCompletenessItemView(context);
		}
	}

}
