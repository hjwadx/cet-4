package com.hjw.cet4.ui.activity.strategy;

import java.util.ArrayList;
import java.util.List;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Strategy;
import com.hjw.cet4.utils.ImageHlp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StrategyAdapter extends BaseAdapter{
	
	List<Strategy> mStrategys = new ArrayList<Strategy>();
	
	public StrategyAdapter(List<Strategy> strategys){
		mStrategys = strategys;
	}

	@Override
	public int getCount() {
		return mStrategys.size();
	}

	@Override
	public Object getItem(int position) {
		return mStrategys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item_strategy, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.guide_name);
			holder.topLine = convertView.findViewById(R.id.topline);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		Strategy strategy = mStrategys.get(position);
		holder.name.setText(strategy.name);
		
		int padding = ImageHlp.changeToSystemUnitFromDP(parent.getContext(), 25);
		if(position == 0){
			holder.topLine.setVisibility(View.VISIBLE);
			convertView.setPadding(0, padding, 0, 0);
		} else {
			if(position == mStrategys.size() - 1){
				convertView.setPadding(0, 0, 0, padding);
			} else {
				convertView.setPadding(0, 0, 0, 0);
			}
			holder.topLine.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView name;
		View topLine;
	}

}
