package com.hjw.cet4.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

public class Compatibility extends fm.jihua.common.utils.Compatibility{
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
	public static void callOnClick(View v){
		if (isCompatible(15)) {
			v.callOnClick();
		} else {
			v.performClick();
		}
	}

}
