package com.hjw.cet4.ui.activity.listening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.practice.CompletenessItemView;
import com.hjw.cet4.ui.activity.practice.ProblemPagerAdapterItemView;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class DictationPagerAdapter extends PagerAdapter{
	
	private Context mContext; 
	private HashMap<Integer, DictationPagerItemView> mHashMap; 
	List<Problem> mProblems;
	List<Piece> mPieces;
	HashMap<Integer, Piece> mPieceMap = new HashMap<Integer, Piece>();
	
	CompletenessItemView mCompletenessItemView;
	
	//new
	boolean isReview;
	int mType;
	int mStart;
	
	public DictationPagerAdapter(Context context, List<Problem> problems, List<Piece> pieces, boolean is_review, int type) {
		this.mContext = context;
		this.mProblems = problems;
		this.mHashMap = new HashMap<Integer, DictationPagerItemView>();
		mPieces = pieces;
		this.isReview = is_review;
		this.mType = type;
		for(Piece piece : mPieces){
			mPieceMap.put(piece.id, piece);
		}
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
		return mPieces.size() + (isReview ? 0 : 1) - getStart();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	//这里进行回收，当我们左右滑动的时候，会把早期的图片回收掉.
	@Override
	public void destroyItem(View container, int position, Object object) {
		position += getStart();
		if(position != mPieces.size()){
			DictationPagerItemView itemView = (DictationPagerItemView) object;
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
		DictationPagerItemView itemView;
		position+= getStart();
		if(position == mPieces.size()){
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
//			Problem problem = mProblems.get(position);
			Piece piece = mPieces.get(position);
			itemView = new DictationPagerItemView(mContext);
			List<Problem> list = new ArrayList<Problem>();
			for(Problem problem : mProblems){
				if(problem.piece_id == piece.id){
					list.add(problem);
				}
			}
			itemView.setData(list, piece, isReview, true);
			mHashMap.put(position, itemView);
			((ViewPager) container).addView(itemView);
		}
		return itemView;
	}

	public void onLastItemShow(){
		mCompletenessItemView.reload();
	}

}
