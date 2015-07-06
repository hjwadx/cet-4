package com.hjw.cet4.ui.activity.setting;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class StatisticsPagerAdapter extends PagerAdapter{
	
	private Context mContext; 
	private HashMap<Integer, StatisticePagerItemView> mHashMap; 
	List<Integer> mTypes;
	
	public StatisticsPagerAdapter(Context context, List<Integer> types) {
		this.mContext = context;
		this.mTypes = types;
		this.mHashMap = new HashMap<Integer, StatisticePagerItemView>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mTypes.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	//这里进行回收，当我们左右滑动的时候，会把早期的图片回收掉.
	@Override
	public void destroyItem(View container, int position, Object object) {
		StatisticePagerItemView itemView = (StatisticePagerItemView) object;
		itemView.recycle();
	}
	
	//这里就是初始化ViewPagerItemView.如果ViewPagerItemView已经存在,
	//重新reload，不存在new一个并且填充数据.
	@Override
	public Object instantiateItem(View container, int position) {
		StatisticePagerItemView itemView;
		if (mHashMap.containsKey(position)) {
			itemView = mHashMap.get(position);
			itemView.reload();
		} else {
			itemView = new StatisticePagerItemView(mContext);
			int type = mTypes.get(position);
			itemView.setData(type);
			mHashMap.put(position, itemView);
			((ViewPager) container).addView(itemView);
		}
		return itemView;
	}

}
