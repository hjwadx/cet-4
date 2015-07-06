package com.hjw.cet4.ui.activity.practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hjw.cet4.R;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.utils.CommonUtils;
import com.hjw.cet4.utils.Const;
import com.hjw.cet4.utils.ImageHlp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import fm.jihua.common.utils.AppLogger;

public class ListeningPagerItemView extends ProblemPagerAdapterItemView{
	
	private TextView mSubject, mPassage, mNumber, mAnalyze;
	private RadioGroup mOptionRadioGroup;
	
	LinearLayout mSelecterparentLayout;
	
	List<String> options = new ArrayList<String>();
	
	public ListeningPagerItemView(Context context) {
		super(context);
		setupViews();
	}
	
	public ListeningPagerItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	@Override
	public void setupViews() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.view_reading_pager_adapter_item, null);
		mSubject = (TextView)view.findViewById(R.id.subject);
		mPassage = (TextView)view.findViewById(R.id.passage);
		mNumber = (TextView)view.findViewById(R.id.number);
		mAnalyze = (TextView)view.findViewById(R.id.analyze);
		mOptionRadioGroup = (RadioGroup) view.findViewById(R.id.options);
		mSelecterparentLayout = (LinearLayout) view.findViewById(R.id.selecter_parent);
		addView(view);
	}
	
	@Override
	public void setData(Problem problem, Piece piece, boolean is_review, boolean is_practice) {
		super.setData(problem, piece, is_review, is_practice);
//		mSubject.setText(mProblem.subject);
		mSubject.setVisibility(View.GONE);
		if(!Const.TEST){
			mPassage.setVisibility(View.GONE);
		}
		mPassage.setText("passage " + mPiece.id);
		mNumber.setText("Question " + mProblem.num +  (mPiece.start == mPiece.end ? "" : " (" + mPiece.start + "-" + mPiece.end + ")") + (CommonUtils.isNullString(mProblem.subject) ? "" : "  " + mProblem.subject));
		if(mProblem.useSharedOptions()){
			options.addAll(Arrays.asList(mPiece.options.split("\\|")));
		} else {
			if(mProblem.options != null && mProblem.options.length() > 0){
				options.addAll(Arrays.asList(mProblem.options.split("\\|")));
			} else {
				options.addAll(Arrays.asList(mPiece.options.split("\\|")));
			}
		}
		if(mProblem.type == Problem.LONG_TO_READ){
//			int count = mPiece.original.split("[A-Z]\\)").length;

			int count = 0;
			options.clear();
			for(String string : mPiece.original.split("[A-Z]\\)")){
				if(count == 0){
					count++;
					continue;
				}
				String string2 = string.trim();
				options.add(string2.substring(0, Math.min(30, string2.length())) + "...");
			}
		}
		int index = 0;
		View view = new View(getContext());
		view.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.FILL_PARENT, 1));
		view.setBackgroundResource(R.color.black_alpha_20);
		mOptionRadioGroup.addView(view);
		for(String string : options){
			if(CommonUtils.isNullString(string)){
				continue;
			}
			//左边
			TextView textView = new TextView(getContext());
			textView.setText(Const.OPTIONS[index]);
			textView.setGravity(Gravity.CENTER);
			textView.setTextSize(15);
			textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, ImageHlp.changeToSystemUnitFromDP(getContext(), 55) + 1));
			
			
			RadioButton btn = new RadioButton(getContext());
			btn.setPadding(ImageHlp.changeToSystemUnitFromDP(getContext(), 58), 0, 0, 0);
			btn.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.FILL_PARENT, ImageHlp.changeToSystemUnitFromDP(getContext(), 55)));
			btn.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
			btn.setBackgroundColor(Color.WHITE);
			btn.setTextSize(15);
			btn.setTextColor(getResources().getColor(R.color.textcolor_4c));
			textView.setTextColor(Color.BLACK);
			if(isReview){
				btn.setEnabled(false);
				if(Const.OPTIONS[index].equals(mProblem.result)){
					btn.setChecked(true);
//					textView.setBackgroundColor(Color.RED);
					textView.setBackgroundResource(R.color.select_red);
					textView.setTextColor(Color.WHITE);
				}
				if(Const.OPTIONS[index].equals(mProblem.answer)){
					textView.setTextColor(Color.WHITE);
//					textView.setBackgroundColor(Color.GREEN);
					textView.setBackgroundResource(R.color.select_green);
				}
			}
			if(Const.TEST){
				btn.setText(string.trim());
			} else {
				btn.setText(string.replaceFirst("[A-Z][/s]*[\\)|\\）]", "").replaceFirst("\\[[A-Z]\\]", "").trim());
			}
			btn.setTag(Const.OPTIONS[index]);

			mSelecterparentLayout.addView(textView);
			mOptionRadioGroup.addView(btn);
			View view1 = new View(getContext());
			view1.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.FILL_PARENT, 1));
			view1.setBackgroundResource(R.color.black_alpha_20);
			mOptionRadioGroup.addView(view1);
			index++;
		}
		if(isReview){
			if(!CommonUtils.isNullString(mProblem.analyze)){
				((View)mAnalyze.getParent()).setVisibility(View.VISIBLE);
				mAnalyze.setText(mProblem.analyze);
			}
		} else {
			((View)mAnalyze.getParent()).setVisibility(View.GONE);
			mOptionRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
//					mAnswer = (String) group.findViewById(checkedId).getTag();
					mProblem.setResult((String) group.findViewById(checkedId).getTag());
					for(int i = 0; i < options.size(); i++){
						TextView textView = (TextView) mSelecterparentLayout.getChildAt(i);
						if(textView.getText().equals((String) group.findViewById(checkedId).getTag())){
//							textView.setBackgroundColor(Color.BLUE);
							textView.setBackgroundResource(R.color.actionbar_background);
							textView.setTextColor(Color.WHITE);
						} else {
//							textView.setBackgroundColor(Color.TRANSPARENT);
							textView.setBackgroundResource(R.color.transparent);
							textView.setTextColor(Color.BLACK);
						}
					}
					AppLogger.v("**************checkedId = " + checkedId  + "   id = " + group.getCheckedRadioButtonId()  + "mAnswer = " + mProblem.getResult());
				}
			});
		}
		
		if(CommonUtils.isNullString(mProblem.subject)){
			mSubject.setVisibility(view.GONE);
		}
		
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
