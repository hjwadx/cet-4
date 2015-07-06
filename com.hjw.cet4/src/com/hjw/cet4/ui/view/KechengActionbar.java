package com.hjw.cet4.ui.view;

import com.hjw.cet4.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class KechengActionbar extends RelativeLayout implements OnClickListener {
	
	private ImageView menuButton;
	private ImageView actionButton;
	private ImageView redPoint;
	private TextView actionButton_TV;
	private TextView menuButton_TV;
	private TextView title;
	private boolean showBack;

	public KechengActionbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public KechengActionbar(Context context) {
		super(context);
		init(context);
	}
	
	void init(Context context){
		LayoutInflater.from(context).inflate(R.layout.titlebar, this);
		setBackgroundResource(R.drawable.actionbar_background);
		menuButton = (ImageView) findViewById(R.id.menu);
		actionButton = (ImageView) findViewById(R.id.action);
		title = (TextView) findViewById(R.id.title);
		actionButton_TV = (TextView) findViewById(R.id.action_textview);
		redPoint=(ImageView) findViewById(R.id.menu_redpoint);
		menuButton_TV = (TextView) findViewById(R.id.menu_textview);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_textview:
			if (showBack) {
				((Activity)getContext()).finish();
			}
			break;
		default:
			break;
		}
	}
	
	public ImageView getLeftButton(){
		return getMenuButton();
	}
	
	public ImageView getRightButton(){
		return getActionButton();
	}
	
	public TextView getRightTextButton(){
		return getActionTextButton();
	}
	
	public TextView getLeftTextButton(){
		return getMenuTextButton();
	}
	
	public ImageView getMenuButton(){
		return menuButton;
	}
	
	public TextView getTitleView(){
		return title;
	}
	
	public TextView getMenuTextButton(){
		return menuButton_TV;
	}
	
	public ImageView getActionButton(){
		return actionButton;
	}
	
	public TextView getActionTextButton(){
		return actionButton_TV;
	}
	
	public void showRedPoint(){
		redPoint.setVisibility(View.VISIBLE);
	}
	
	public void hideRedPoint(){
		redPoint.setVisibility(View.GONE);
	}
	
	public void setTitle(int res){
		title.setText(getResources().getString(res));
	}
	
	public void setTitle(CharSequence text){
		title.setText(text);
	}
	
	public void showBackBtn(){
		setShowBackBtn(true);
	}
	
	public void setLeftImage(int res){
		getLeftButton().setImageResource(res);
		getMenuTextButton().setVisibility(View.GONE);
		getLeftButton().setVisibility(View.VISIBLE);
	}
	
	public void setLeftText(String text){
		getMenuTextButton().setVisibility(View.VISIBLE);
		getMenuTextButton().setText(text);
		getLeftButton().setVisibility(View.GONE);
	}
	
	public void setLeftText(int stringRes){
		this.setLeftText(getContext().getString(stringRes));
	}
	
	public void setRightImage(int res){
		getActionTextButton().setVisibility(View.GONE);
		getRightButton().setVisibility(View.VISIBLE);
		((ImageView)getRightButton()).setImageResource(res);
	}
	
	public void setRightText(String text){
		getActionTextButton().setVisibility(View.VISIBLE);
		getRightButton().setVisibility(View.GONE);
		getActionTextButton().setText(text);
	}
	
	public void setRightText(int stringRes){
		this.setRightText(getContext().getString(stringRes));
	}
	
	public void setRightButtonGone(){
		getRightTextButton().setVisibility(View.GONE);
		getRightButton().setVisibility(View.GONE);
	}
	
	public void setLefttButtonGone(){
		getLeftButton().setVisibility(View.GONE);
		getLeftTextButton().setVisibility(View.GONE);
	}
	
	public void setShowBackBtn(boolean showBack){
		this.showBack = showBack;
		if (showBack) {
			menuButton_TV.setText("返回");
			menuButton_TV.setOnClickListener(this);
		}else {
			menuButton.setImageResource(R.drawable.menubar_btn_icon_menu);
			menuButton.setOnClickListener(null);
		}
	}
}
