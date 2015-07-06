package fm.jihua.common.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.umeng.analytics.MobclickAgent;

import fm.jihua.common.App;
import fm.jihua.common.utils.Compatibility;
import fm.jihua.ui.R;

public class BaseActivity extends SherlockFragmentActivity{
	
	public static final int DIALOG_FOR_BLOCK = 0x1111;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		App app = (App)getApplication();
		app.addActivity(this);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		App app = (App)getApplication();
		app.removeActivity(this);
	}
	
	protected void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);
	}
	
	protected void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if (!Compatibility.isCompatible(8)) {
			return onCreateDialog(id, new Bundle());
		}
		return super.onCreateDialog(id);
	}
	
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case DIALOG_FOR_BLOCK:{
			Dialog dialog = new Dialog(this, R.style.Dialog_Fullscreen);
			dialog.setContentView(R.layout.dialog);
			String message = args.getString("message");
			if (message != null && message.length() != 0) {
				dialog.findViewById(R.id.tv_message).setVisibility(View.VISIBLE);
				((TextView)dialog.findViewById(R.id.tv_message)).setText(message);
			}else {
				dialog.findViewById(R.id.tv_message).setVisibility(View.GONE);
			}
			return dialog;
		}
		default:
			return super.onCreateDialog(id);
		}
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		super.onPrepareDialog(id, dialog, args);
		switch (id) {
		case DIALOG_FOR_BLOCK:
			String message = args.getString("message");
			if (message != null && message.length() != 0) {
				dialog.findViewById(R.id.tv_message).setVisibility(View.VISIBLE);
				((TextView)dialog.findViewById(R.id.tv_message)).setText(message);
			}else {
				dialog.findViewById(R.id.tv_message).setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}
}
