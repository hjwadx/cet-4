package com.hjw.cet4.ui.widget;

import com.hjw.cet4.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LinearSettingView extends LinearLayout {

	// 需要设置的5个属性
	// kecheng:parentId="@+id/haha"
	// kecheng:titleName="你好"
	// kecheng:contentId="@+id/content"
	// kecheng:contentHint="你好"
	// kecheng:backgroundType="top|middle|bottom|single" 背景图片
	// kecheng:titleId="@+id/birthday_title" 当需要设置titleId的时候，可以进行设置
	// kecheng:layoutType="normal|vertical" 布局类型：正常的条形布局|垂直布局

	final int LAYOUTTYPE_NORMAL = 0;
	final int LAYOUTTYPE_VERTICAL = 1;
	final int LAYOUTTYPE_TOGGLE = 2;
	final int LAYOUTTYPE_OAUTH = 3;
	final int LAYOUTTYPE_HIGHLIGHT = 4;
	final int LAYOUTTYPE_IMAGEICON =5;

	final int BACKTYPE_TOP = 0;
	final int BACKTYPE_MIDDLE = 1;
	final int BACKTYPE_BOTTOM = 2;
	final int BACKTYPE_SINGLE = 3;

	final int DEFAULT_DATA = -1;

	String titleName = "";
	String contentHint = "";

	public LinearSettingView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinearSetting);

		titleName = a.getString(R.styleable.LinearSetting_titleName);
		contentHint = a.getString(R.styleable.LinearSetting_contentHint);
		int parentId = a.getResourceId(R.styleable.LinearSetting_parentId, DEFAULT_DATA);
		int contentId = a.getResourceId(R.styleable.LinearSetting_contentId, DEFAULT_DATA);
		int titleId = a.getResourceId(R.styleable.LinearSetting_titleId, DEFAULT_DATA);
		int toggleId = a.getResourceId(R.styleable.LinearSetting_toggleId, DEFAULT_DATA);
		int highLightId = a.getResourceId(R.styleable.LinearSetting_highLightId, DEFAULT_DATA);
		int layoutType = a.getInteger(R.styleable.LinearSetting_layoutType, LAYOUTTYPE_NORMAL);
//		int backgroundType = a.getInteger(R.styleable.LinearSetting_backgroundType, -1);
		int visibility = a.getInteger(R.styleable.LinearSetting_visibility, 0);
		
		int oauthBindId = a.getResourceId(R.styleable.LinearSetting_oauthBindId, DEFAULT_DATA);
		int oauthBindDoneId = a.getResourceId(R.styleable.LinearSetting_oauthBindDoneId, DEFAULT_DATA);
		String oauthBindText = a.getString(R.styleable.LinearSetting_oauthBindText);
		String oauthBindDoneText = a.getString(R.styleable.LinearSetting_oauthBindDoneText);
		
		int imageIcon = a.getResourceId(R.styleable.LinearSetting_imageIcon, DEFAULT_DATA);

		a.recycle();
		
		if (DEFAULT_DATA != toggleId){
			layoutType = LAYOUTTYPE_TOGGLE;
		}
		
		if (oauthBindId != DEFAULT_DATA && oauthBindDoneId != DEFAULT_DATA) {
			layoutType = LAYOUTTYPE_OAUTH;
		}
		
		if (highLightId != DEFAULT_DATA) {
			layoutType = LAYOUTTYPE_HIGHLIGHT;
		}
		
		if(imageIcon != DEFAULT_DATA){
			layoutType = LAYOUTTYPE_IMAGEICON;
		}

		if (layoutType == LAYOUTTYPE_NORMAL) {
			LayoutInflater.from(getContext()).inflate(R.layout.linearsetting, this);
		} else if (layoutType == LAYOUTTYPE_VERTICAL) {
			LayoutInflater.from(getContext()).inflate(R.layout.linearsetting_vertical, this);
		} else if (layoutType == LAYOUTTYPE_TOGGLE) {
			LayoutInflater.from(getContext()).inflate(R.layout.linearsetting_toggle, this);
		} else if (layoutType == LAYOUTTYPE_OAUTH) {
			LayoutInflater.from(getContext()).inflate(R.layout.linearsetting_oauth_with_two_text, this);
		} else if (layoutType == LAYOUTTYPE_HIGHLIGHT) {
			LayoutInflater.from(getContext()).inflate(R.layout.linearsetting_highlight, this);
		} else if (layoutType == LAYOUTTYPE_IMAGEICON) {
			LayoutInflater.from(getContext()).inflate(R.layout.linearsetting_imageicon, this);
		}


		if(!TextUtils.isEmpty(titleName)){
			((TextView) findViewById(R.id.title)).setText(titleName);
		}
		
		if (DEFAULT_DATA != titleId) {
			((TextView) findViewById(R.id.title)).setId(titleId);
		}
		
		if (DEFAULT_DATA != parentId){
			findViewById(R.id.parent).setId(parentId);
		}

		if(!TextUtils.isEmpty(contentHint)){
			((TextView) findViewById(R.id.content)).setHint(contentHint);
		}
		if (DEFAULT_DATA != contentId){
			((TextView) findViewById(R.id.content)).setId(contentId);
		}
		
		if (DEFAULT_DATA != toggleId){
			ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggle);
			toggleButton.setId(toggleId);
		}
		
		if (highLightId != DEFAULT_DATA) {
			TextView highText = (TextView) findViewById(R.id.highlight);
			highText.setId(highLightId);
		}
		
		if (visibility == 1) {
			findViewById(parentId == DEFAULT_DATA ? R.id.parent : parentId).setVisibility(View.GONE);
		}
		
		if (oauthBindId != DEFAULT_DATA && oauthBindDoneId != DEFAULT_DATA) {
			TextView textBind = (TextView) findViewById(R.id.bind);
			TextView textBindDone = (TextView) findViewById(R.id.bind_done);
			textBind.setText(oauthBindText);
			textBind.setId(oauthBindId);
			textBindDone.setText(oauthBindDoneText);
			textBindDone.setId(oauthBindDoneId);
		}
		
		if(imageIcon != DEFAULT_DATA){
			((ImageView)findViewById(R.id.icon)).setImageResource(imageIcon);
		}

//		setId(parentId);
//		if(backgroundType == -1) {
//			setBackgroundColor(Color.WHITE);
//		} else{
//			int resource = -1;
//			switch (backgroundType) {
//			case BACKTYPE_TOP:
//				resource = R.drawable.input_top;
//				break;
//			case BACKTYPE_MIDDLE:
//				resource = R.drawable.input_middle;
//				break;
//			case BACKTYPE_BOTTOM:
//				resource = R.drawable.input_bottom;
//				break;
//			case BACKTYPE_SINGLE:
//				resource = R.drawable.input_single;
//				break;
//			}
//			setBackgroundResource(resource);
//		}
	}

}
