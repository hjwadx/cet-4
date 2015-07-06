package fm.jihua.common.ui.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import fm.jihua.common.ui.BaseActivity;
import fm.jihua.common.utils.Compatibility;

public class UIUtil {
	private final static String TAG = "UIUtil"; 
	
	public static void block(Activity activity) {
		block(activity, null);
	}
	
	@SuppressWarnings("deprecation")
	@TargetApi(8)
	public static void block(Activity activity, String message){
		Log.i("UIUtil", "block");
//		unblock(activity);
//		findViewById(R.id.dialog_bg).getBackground().setAlpha(120);
		try {
//			mDialog = new Dialog(activity, R.style.Dialog_Fullscreen);
//			mDialog.setContentView(R.layout.dialog);
//			if (message != null && message.length() != 0) {
//				mDialog.findViewById(R.id.tv_message).setVisibility(View.VISIBLE);
//				((TextView)mDialog.findViewById(R.id.tv_message)).setText(message);
//			}else {
//				mDialog.findViewById(R.id.tv_message).setVisibility(View.GONE);
//			}
//			mDialog.show();
			Bundle bundle = new Bundle();
			bundle.putString("message", message);
			if (Compatibility.isCompatible(8)) {
				activity.showDialog(BaseActivity.DIALOG_FOR_BLOCK, bundle);
			}else {
				activity.showDialog(BaseActivity.DIALOG_FOR_BLOCK);
			}
		} catch (Exception e) {
			Log.w(TAG, e.getMessage());
		}
//		dialog.findViewById(R.id.dialog_bg).getBackground().setAlpha(120);
//		if (activity.findViewById(R.id.refresh) != null) {
//			activity.findViewById(R.id.refresh).setVisibility(View.GONE);
//		}
//		if (activity.findViewById(R.id.progressBar) != null) {
//			activity.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
//		}
	}
	
	@SuppressWarnings("deprecation")
	public static void unblock(Activity activity){
		Log.i("UIUtil", "unblock");
		try {
			activity.dismissDialog(BaseActivity.DIALOG_FOR_BLOCK);
		} catch (Exception e) {
//			Log.w(TAG, e.getMessage());
		}
//		if (activity.findViewById(R.id.refresh) != null && showRefresh) {
//			activity.findViewById(R.id.refresh).setVisibility(View.VISIBLE);
//		}
//		if (activity.findViewById(R.id.progressBar) != null) {
//			activity.findViewById(R.id.progressBar).setVisibility(View.GONE);
//		}
	}
	
	public static boolean setAlpha(int alpha, View view)
	{
	    if (view instanceof ViewGroup)
	    {
	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
	        {
	        	setAlpha(alpha, ((ViewGroup) view).getChildAt(i));
	            if (((ViewGroup) view).getBackground() != null) ((ViewGroup) view).getBackground().setAlpha(alpha);
	        }
	    }
	    else if (view instanceof ImageView)
	    {
	        if (((ImageView) view).getDrawable() != null) ((ImageView) view).getDrawable().setAlpha(alpha);
	        if (((ImageView) view).getBackground() != null) ((ImageView) view).getBackground().setAlpha(alpha);
	    }
	    else if (view instanceof TextView)
	    {
//	        ((TextView) view).setTextColor(((TextView) view).getTextColors().withAlpha(alpha));
	        if (((TextView) view).getBackground() != null) ((TextView) view).getBackground().setAlpha(alpha);
	    }
	    else if (view instanceof EditText)
	    {
//	        ((EditText) view).setTextColor(((EditText) view).getTextColors().withAlpha(alpha));
	        if (((EditText) view).getBackground() != null) ((EditText) view).getBackground().setAlpha(alpha);
	    }else {
	    	if (view.getBackground() != null) view.getBackground().setAlpha(alpha);
		}
	    return true;
	}
}
