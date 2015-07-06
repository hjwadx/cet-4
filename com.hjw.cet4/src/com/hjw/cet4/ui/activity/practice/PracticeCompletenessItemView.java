package com.hjw.cet4.ui.activity.practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hjw.cet4.App;
import com.hjw.cet4.R;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import fm.jihua.common.ui.helper.Hint;

public class PracticeCompletenessItemView extends CompletenessItemView{
	
	private TextView mCompleteness;
	private Button mCommit;

	public PracticeCompletenessItemView(Context context) {
		super(context);
		setupViews();
	}

	public PracticeCompletenessItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	// 初始化View.
	@Override
	public void setupViews() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.view_reading_completeness_item,
				null);
		mCompleteness = (TextView) view.findViewById(R.id.completeness);
		mCommit = (Button) view.findViewById(R.id.commit);
		mCommit.setText("交卷并查看结果");
		// mAlbumImageView = (ImageView)view.findViewById(R.id.album_imgview);
		// mALbumNameTextView = (TextView)view.findViewById(R.id.album_name);
		addView(view);
	}

	/**
	 * 填充数据，共外部调用.
	 * 
	 * @param object
	 */
	@Override
	public void setData( List<Problem> problems, HashMap<Integer, Piece> piece_map) {
		super.setData(problems, piece_map);
		mCompleteness.setText("你还有" + getRemanent() + "道题没有做");
		mCommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveResult();
				
				Intent intent = new Intent(getContext(), PracticeResultActivity.class);
				List<Problem> problems = new ArrayList<Problem>();
				for(Problem problem : mProblems){
					if(problem.type != Problem.PART){
						problems.add(problem);
					}
				}
				intent.putExtra("SUBJECTS", new ArrayList<Problem>(problems));
				intent.putExtra("PIECES", mPieceMap);
				getContext().startActivity(intent);
				((Activity)getContext()).finish();
				Hint.showTipsShort(getContext(), "交卷");
				MobclickAgent.onEvent(getContext(), "event_submit_all_simulate", App.getInstance().getExamDBHelper().getPaperById(mPieceMap.get(problems.get(0).piece_id).paper_id).name);
			}
		});
		// try {
		// int resId = object.getInt("resid");
		// String name = object.getString("name");
		// // 实战中如果图片耗时应该令其一个线程去拉图片异步,不然把UI线程卡死.
		// mAlbumImageView.setImageResource(resId);
		// mALbumNameTextView.setText(name);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
	}
	
	void saveResult(){
		App.getInstance().addDonePaper(mPieceMap.get(mProblems.get(mProblems.size() - 1).piece_id).paper_id);
		App.getInstance().getDBHelper().saveProblems(App.getInstance().getUserDB(), mProblems);
	}

	/**
	 * 这里内存回收.外部调用.
	 */
	@Override
	public void recycle() {
		// mAlbumImageView.setImageBitmap(null);
		// if ((this.mBitmap == null) || (this.mBitmap.isRecycled()))
		// return;
		// this.mBitmap.recycle();
		// this.mBitmap = null;
	}

	/**
	 * 重新加载.外部调用.
	 */
	@Override
	public void reload() {
		mCompleteness.setText("你还有" + getRemanent() + "道题没有做");
		// try {
		// int resId = mObject.getInt("resid");
		// // 实战中如果图片耗时应该令其一个线程去拉图片异步,不然把UI线程卡死.
		// mAlbumImageView.setImageResource(resId);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
	}
	
	int getRemanent(){
		int result = 0;
		for(Problem problem : mProblems){
			if(problem.getResult() == null && problem.mustHasResult()){
				result++;
			}
		}
		return result;
	}

}
