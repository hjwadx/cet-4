package com.hjw.cet4.ui.activity.practice;

import java.util.HashMap;
import java.util.List;

import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ProblemPagerAdapter extends PagerAdapter{
	
	private Context mContext; 
	private HashMap<Integer, ProblemPagerAdapterItemView> mHashMap; 
	List<Problem> mProblems;
	HashMap<Integer, Piece> mPieceMap;
	
	CompletenessItemView mCompletenessItemView;
	
	//new
	boolean isReview;
	int mType;
	int mStart;
	
	public ProblemPagerAdapter(Context context, List<Problem> problems, HashMap<Integer, Piece> piece_map, boolean is_review, int type) {
		this.mContext = context;
		this.mProblems = problems;
		this.mHashMap = new HashMap<Integer, ProblemPagerAdapterItemView>();
		mPieceMap = piece_map;
		this.isReview = is_review;
		this.mType = type;
	}
	
	public void setStart(int start){
		mStart = start;
	}
	
	public int getStart(){
		return mStart;
	}

	@Override
	public int getCount() {
		if(mType == Problem.PRACTICE && !isReview && mProblems.size() < 10){
			return mProblems.size();
		}
		return mProblems.size() + (isReview ? 0 : 1) - getStart();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	//这里进行回收，当我们左右滑动的时候，会把早期的图片回收掉.
	@Override
	public void destroyItem(View container, int position, Object object) {
		position += getStart();
		if(position != mProblems.size()){
			ProblemPagerAdapterItemView itemView = (ProblemPagerAdapterItemView) object;
			itemView.recycle();
		} else {
			CompletenessItemView itemView = (CompletenessItemView) object;
			itemView.recycle();
		}
	}
	
	//这里就是初始化ViewPagerItemView.如果ViewPagerItemView已经存在,
	//重新reload，不存在new一个并且填充数据.
	@Override
	public Object instantiateItem(View container, int position) {
		ProblemPagerAdapterItemView itemView;
		position+= getStart();
		if(position == mProblems.size()){
			if(mCompletenessItemView == null){
				mCompletenessItemView = CompletenessItemView.createByCategory(mContext, mType);
				mCompletenessItemView.setData(mProblems, mPieceMap);
				((ViewPager) container).addView(mCompletenessItemView);
			} else {
				mCompletenessItemView.reload();
			}
			return mCompletenessItemView;
		}
		if (mHashMap.containsKey(position)) {
			itemView = mHashMap.get(position);
			itemView.reload();
		} else {
			Problem problem = mProblems.get(position);
			itemView = ProblemPagerAdapterItemView.createByCategory(mContext, problem);
			itemView.setData(problem, mPieceMap.get(problem.piece_id), isReview, true);
			mHashMap.put(position, itemView);
			((ViewPager) container).addView(itemView);
		}
		return itemView;
	}
	
	public void onLastItemShow(){
		mCompletenessItemView.reload();
	}
}
