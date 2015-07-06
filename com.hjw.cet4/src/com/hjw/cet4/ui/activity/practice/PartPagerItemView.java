package com.hjw.cet4.ui.activity.practice;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class PartPagerItemView extends ProblemPagerAdapterItemView{
	
	private TextView mSubject;
	
	public PartPagerItemView(Context context) {
		super(context);
		setupViews();
	}
	
	public PartPagerItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}
	
	@Override
	public void setupViews() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.view_part_pager_adapter_item, null);
		mSubject = (TextView)view.findViewById(R.id.subject);
		addView(view);
	}
	


	@Override
	public void setData(Problem problem, Piece piece, boolean is_review, boolean is_practice) {
		super.setData(problem, piece, is_review, is_practice);
		mSubject.setText(mProblem.subject);
	}

	@Override
	public void recycle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

}
