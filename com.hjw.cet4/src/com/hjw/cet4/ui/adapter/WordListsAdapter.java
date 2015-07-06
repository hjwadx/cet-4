package com.hjw.cet4.ui.adapter;

import java.util.List;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.WordList;
import com.hjw.cet4.utils.CommonUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WordListsAdapter extends BaseAdapter{
	
	final List<WordList> mWordLists;
	Context context;
	
	public WordListsAdapter(List<WordList> wordlists, Context context){
		this.mWordLists = wordlists;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mWordLists.size();
	}

	@Override
	public Object getItem(int position) {
		return mWordLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		WordList wordlist = mWordLists.get(position);
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
		holder.name.setText(CommonUtils.isNullString(wordlist.name) ? "list" + wordlist.num : wordlist.name);
		holder.state.setVisibility(View.GONE);
		return convertView;
	}
	
	static class ViewHolder{
		View topline;
		TextView name;
		TextView state;
	}

}
