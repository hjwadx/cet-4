package com.hjw.cet4.ui.adapter;

import java.util.List;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Paper;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PaperAdapter extends BaseAdapter{
	
	final List<Paper> mPapers;
	Context context;
	
	public PaperAdapter(List<Paper> papers, Context context){
		this.mPapers = papers;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mPapers.size();
	}

	@Override
	public Object getItem(int position) {
		return mPapers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Paper paper = mPapers.get(position);
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
		holder.name.setText(paper.name);
		holder.state.setTextColor(context.getResources().getColor(R.color.textcolor_4c));
		holder.state.setText("未下载");
		if(paper.isDownloading){
			holder.state.setText("下载中");
			holder.state.setTextColor(context.getResources().getColor(R.color.select_red));
		}
		if(paper.isVoiceDownloaded() && !paper.isDownloading){
			holder.state.setText("已下载");
			holder.state.setTextColor(context.getResources().getColor(R.color.actionbar_background));
		}
		if(paper.isDone()){
			holder.state.setText("已完成");
			holder.state.setTextColor(context.getResources().getColor(R.color.select_green));
		}
		return convertView;
	}
	
	static class ViewHolder{
		View topline;
		TextView name;
		TextView state;
	}


}
