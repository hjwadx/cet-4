package com.hjw.cet4.ui.adapter;

import java.util.List;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Problem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PracticeResultAdapter extends BaseAdapter{
	
	List<Problem> mProblems;
	Context context;
	Problem fillProblem;
	
	public PracticeResultAdapter(List<Problem> problems, Context context){
		this.mProblems = problems;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mProblems.size() + (4-(mProblems.size()%4))%4;
	}

	@Override
	public Object getItem(int position) {
		return mProblems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Problem problem;
		if(position >= mProblems.size()){
			if(fillProblem == null){
				fillProblem = new Problem(0, "", "", "", "", 0, 0);
			}
			problem = fillProblem;
		} else {
			problem = mProblems.get(position);
		}
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_practice_result, parent, false);
			holder = new ViewHolder();
			holder.num = (TextView) convertView.findViewById(R.id.num);
			holder.result = (ImageView) convertView.findViewById(R.id.result);
			holder.topline = convertView.findViewById(R.id.topline);
			holder.rightline = convertView.findViewById(R.id.rightline);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(position < 4){
			holder.topline.setVisibility(View.VISIBLE);
		} else {
			holder.topline.setVisibility(View.GONE);
		}
		if(position%4 == 3){
			holder.rightline.setVisibility(View.GONE);
		} else {
			holder.rightline.setVisibility(View.VISIBLE);
		}
		if(position >= mProblems.size()){
			holder.num.setText("");
			holder.result.setImageResource(-1);
		} else {
			holder.num.setText("" + problem.num);
			holder.result.setImageResource(problem.checkResult() ? R.drawable.mock_exam_result_right_bg : R.drawable.mock_exam_result_wrong_bg);
		}
		return convertView;
	}
	
	static class ViewHolder{
		View topline;
		View rightline;
		TextView num;
		ImageView result;
	}

}
