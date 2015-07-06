package com.hjw.cet4.ui.activity.reading;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hjw.cet4.App;
import com.hjw.cet4.R;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.entities.Product;
import com.hjw.cet4.ui.activity.BaseActivity;
import com.hjw.cet4.utils.Const;
import com.hjw.cet4.utils.PayUtils;
import com.hjw.cet4.utils.PayUtils.RefreshListener;
import com.hjw.cet4.utils.UpperLimitHelper;
import com.umeng.analytics.MobclickAgent;

import fm.jihua.common.ui.helper.Hint;

public class ReadingAmountSelectActivity extends BaseActivity implements RefreshListener{
	
	private Button mTypeBtn0, mTypeBtn1, mTypeBtn2, mTypeBtn3;
	
	int mType;
	
	PayUtils mPayUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type_select);
		mType = getIntent().getIntExtra("TYPE", 0);
		findViews();
		initView();
		initTitlebar();
		setListener();
	}

	private void setListener() {
		mTypeBtn0.setOnClickListener(l);
		mTypeBtn1.setOnClickListener(l);
		mTypeBtn2.setOnClickListener(l);
		mTypeBtn3.setOnClickListener(l);
		
	}

	private void initTitlebar() {
		String title = "阅读";
		switch (mType) {
		case Problem.WORDS_COMPREHENSION:
			title = "词汇理解";
			break;
		case Problem.LONG_TO_READ:
			title = "长篇阅读";
			break;
		case Problem.CAREFUL_READING:
			title = "仔细阅读";
			break;
		default:
			break;
		}
		setTitle(title);
	}

	private void initView() {
		mTypeBtn0.setText("来1篇");
		mTypeBtn1.setText("来2篇");
		mTypeBtn2.setText("来3篇");
		mTypeBtn3.setText("来5篇");
	}

	private void findViews() {
		mTypeBtn0 = (Button) findViewById(R.id.type_select_btn_0);
		mTypeBtn1 = (Button) findViewById(R.id.type_select_btn_1);
		mTypeBtn2 = (Button) findViewById(R.id.type_select_btn_2);
		mTypeBtn3 = (Button) findViewById(R.id.type_select_btn_3);
	}
	
	OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.type_select_btn_0: {
				if(UpperLimitHelper.getInstance().getIsUnderLimit(1)){
					Intent intent = new Intent(ReadingAmountSelectActivity.this, ReadingActivity.class);
					intent.putExtra("TYPE", mType);
					intent.putExtra("AMOUNT", 1);
					startActivity(intent);
					finish();
				} else {
					showBuyDialog();
				}
				MobclickAgent.onEvent(ReadingAmountSelectActivity.this, getUmengKeyByType(), 1);	
			}
				break;
			case R.id.type_select_btn_1: {
				if(UpperLimitHelper.getInstance().getIsUnderLimit(2)){
					Intent intent = new Intent(ReadingAmountSelectActivity.this, ReadingActivity.class);
					intent.putExtra("TYPE", mType);
					intent.putExtra("AMOUNT", 2);
					startActivity(intent);
					finish();
				} else {
					showBuyDialog();
				}
				MobclickAgent.onEvent(ReadingAmountSelectActivity.this, getUmengKeyByType(), 1);
			}
				break;
			case R.id.type_select_btn_2: {
				if(UpperLimitHelper.getInstance().getIsUnderLimit(3)){
					Intent intent = new Intent(ReadingAmountSelectActivity.this, ReadingActivity.class);
					intent.putExtra("TYPE", mType);
					intent.putExtra("AMOUNT", 3);
					startActivity(intent);
					finish();
				} else {
					showBuyDialog();
				}
				MobclickAgent.onEvent(ReadingAmountSelectActivity.this, getUmengKeyByType(), 1);
			}
				break;
			case R.id.type_select_btn_3: {
				if(UpperLimitHelper.getInstance().getIsUnderLimit(5)){
					Intent intent = new Intent(ReadingAmountSelectActivity.this, ReadingActivity.class);
					intent.putExtra("TYPE", mType);
					intent.putExtra("AMOUNT", 5);
					startActivity(intent);
					finish();
				} else {
					showBuyDialog();
				}
				MobclickAgent.onEvent(ReadingAmountSelectActivity.this, getUmengKeyByType(), 1);
			}
				break;
			default:
				break;
			}
			
		}
	};
	
	private void showBuyDialog(){
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage("每天15道题的免费练习机会，升级解锁天天无限练习~")
		.setPositiveButton("现在购买",
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialoginterface,
							int i) {
						if(mPayUtils == null){
							mPayUtils = new PayUtils(ReadingAmountSelectActivity.this, ReadingAmountSelectActivity.this);
						}
						mPayUtils.downloadOrPay(new Product("四级考霸", "四级考霸——单项", Const.PAY_PRICE));
						dialoginterface.dismiss();
					}
				})
		.setNegativeButton("稍后购买",
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialoginterface,
							int i) {
						// 按钮事件
						dialoginterface.dismiss();
					}
				}).show();
	}

	@Override
	public void onRefresh(boolean succeed) {
		if(succeed){
			App.getInstance().setPayed(true);
			Hint.showTipsShort(this, "支付成功");
		} else {
			Hint.showTipsShort(this, "支付失败");
		}
	}
	
	private String getUmengKeyByType(){
		String result = "";
		switch (mType) {
		case Problem.WORDS_COMPREHENSION:
			result = "action_select_words_comperhension_amount";
			break;
		case Problem.LONG_TO_READ:
			result = "action_select_long_to_read_amount";
			break;
		case Problem.CAREFUL_READING:
			result = "action_select_careful_reading_amount";
			break;
		default:
			break;
		}
		return result;
	}

}
