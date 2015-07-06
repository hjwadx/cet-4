package com.hjw.cet4.ui.activity.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import com.hjw.cet4.App;
import com.hjw.cet4.R;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.ui.activity.listening.DictationActivity;
import com.hjw.cet4.ui.activity.listening.ListeningActivity;
import com.hjw.cet4.ui.activity.reading.ReadingActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatisticePagerItemView extends FrameLayout{
	
	int mType;
	TextView correctTextView;
    TextView doneTextView;
    Button checkBtn;
    LinearLayout imageParent;
    
    List<Problem> mProblems;
	List<Piece> mPieces;
	HashMap<Integer, Piece> mPieceMap = new HashMap<Integer, Piece>();
	
	// 定义一个类别序列，此序列主要用于存储饼图的百分比及相应的标签
	private CategorySeries mSeries;
	// 定义一个渲染器
	private DefaultRenderer mRenderer;
	// 由类别序列和渲染器得到一个图表视图
	private GraphicalView mChartView;
	
	public StatisticePagerItemView(Context context) {
		super(context);
		setupViews();
	}
	
	public StatisticePagerItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		 setupViews();
	}
	
	// 初始化View.
	private void setupViews() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.view_statistice_pager_adapter_item, null);
		correctTextView = (TextView) view.findViewById(R.id.correct_name);
		doneTextView = (TextView) view.findViewById(R.id.done_name);
		checkBtn = (Button) view.findViewById(R.id.check);
		imageParent = (LinearLayout) view.findViewById(R.id.image);
		checkBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Class<?> c = ReadingActivity.class;
				if(mType == Problem.PASSAGE_DICTATION){
					c = DictationActivity.class;
				} else if(mType == Problem.SHORT_CONVERSATIONS || mType == Problem.LONG_CONVERSATIONS || mType == Problem.SHORT_PASSAGES){
					c = ListeningActivity.class;
				}
				Intent intent = new Intent(getContext(), c);
				intent.putExtra("REVIEW", true);
				intent.putExtra("SUBJECTS", new ArrayList<Problem>(mProblems));
				intent.putExtra("PIECES", mPieceMap);
				getContext().startActivity(intent);
			}
		});
		initialPieChartBuilder();
		addView(view);
	}
	
	/**
	    * 填充数据，共外部调用.
	    * @param object
	    */
	public void setData(int type) {
		this.mType = type;
		List<String> pieceIds = App.getInstance().getDBHelper().getPieceIdsByType(App.getInstance().getUserDB(), mType);
		mPieces = App.getInstance().getExamDBHelper().getPiecesByPieceId(pieceIds);
		for(Piece piece : mPieces){
			mPieceMap.put(piece.id, piece);
		}
		mProblems = App.getInstance().getExamDBHelper().getProblemsByPieceId(pieceIds);
    	App.getInstance().getDBHelper().getResultByProblems(App.getInstance().getUserDB(), mProblems);
    	doneTextView.setText("" + mProblems.size());
    	correctTextView.setText("" + getCorrect());
    	if(mProblems.size() < 1 || mType == Problem.PASSAGE_DICTATION){
    		checkBtn.setVisibility(View.GONE);
    	}
    	
//		int ratio = mProblems.size() > 0 ? getCorrect()*100/mProblems.size() : 0;
//		refreshImage(ratio);
	}
	
	/**
	    * 这里内存回收.外部调用.
	    */
	public void recycle() {
	}
	
	/**
	    * 重新加载.外部调用.
	    */
	public void reload() {
	}
	
	int getCorrect(){
		int result = 0;
		for(Problem problem : mProblems){
			if(problem.checkResult()){
				result++;
			}
		}
		return result;
	}

	private void initialPieChartBuilder() {
		// TODO Auto-generated method stub
		mRenderer = new DefaultRenderer();
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.TRANSPARENT);
		// mRenderer.setChartTitleTextSize(20);
		mRenderer.setLabelsTextSize(20);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setShowLabels(false);
		mRenderer.setShowAxes(false); // 是否显示轴线
		// mRenderer.setAxesColor(Color.RED); //设置轴颜色
		mRenderer.setFitLegend(false);
		mRenderer.setInScroll(false);
		mRenderer.setPanEnabled(false);
		mRenderer.setShowCustomTextGrid(false);
		mRenderer.setShowLegend(false); // 不显示图例
		mRenderer.setShowGrid(false);
		mRenderer.setClickEnabled(true);
		// mRenderer.setScale(1.5f);
		// mRenderer.setLegendTextSize(15);
		// mRenderer.setMargins(new int[] { 20, 50, 15, 0 });
		// mRenderer.setZoomButtonsVisible(true);
		mRenderer.setStartAngle(0);

		mSeries = new CategorySeries("");
		mChartView = ChartFactory.getPieChartView(getContext(), mSeries,
				mRenderer);
		imageParent.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
	}
	
	public void refreshImage(int ratio) {
		mSeries.clear(); // 清空类别序列
		for (SimpleSeriesRenderer renderer : mRenderer.getSeriesRenderers()) {
			// 移除老的图表列渲染器
			mRenderer.removeSeriesRenderer(renderer);
		}
		double[] mPercent = new double[] { 100 - ratio, ratio };
		int[] COLORS = new int[] { Color.RED, Color.GREEN };
		for (int i = 0; i < mPercent.length; i++) {
			if (mPercent[i] > 0) {
				// 添加一个图表列，第一个参数是标签，第二个参数是百分比，CATEGORY和COLORS为自定义数组
				mSeries.add("" + i, mPercent[i]);
				SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
				renderer.setColor(COLORS[i]);
				// 为图表列添加渲染器
				mRenderer.addSeriesRenderer(renderer);
			}
		}
		if (mChartView != null) {
			// 重绘
			mChartView.repaint();
		}
	}

}
