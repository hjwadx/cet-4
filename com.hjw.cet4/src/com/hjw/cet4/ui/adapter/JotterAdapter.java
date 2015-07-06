package com.hjw.cet4.ui.adapter;

import java.util.List;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Jotter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JotterAdapter extends BaseAdapter{
	
	final List<Jotter> mJotters;
	Context context;
	
	public JotterAdapter(List<Jotter> jotters, Context context){
		this.mJotters = jotters;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mJotters.size();
	}

	@Override
	public Object getItem(int position) {
		return mJotters.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Jotter jotter = mJotters.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paper, parent, false);
			holder = new ViewHolder();
			holder.topline = convertView.findViewById(R.id.topline);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(position == 0){
			holder.topline.setVisibility(View.VISIBLE);
		} else {
			holder.topline.setVisibility(View.GONE);
		}
		holder.name.setText(jotter.name);
		holder.state.setVisibility(View.GONE);
		return convertView;
	}
	
	static class ViewHolder{
		View topline;
		TextView name;
		TextView state;
	}

}
