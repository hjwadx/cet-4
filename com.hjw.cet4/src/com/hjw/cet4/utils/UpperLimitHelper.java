package com.hjw.cet4.utils;

import java.util.Calendar;
import java.util.Date;

import com.hjw.cet4.App;


public class UpperLimitHelper {
	
	private static UpperLimitHelper upperLimitHelper;

	public static final UpperLimitHelper getInstance() {
		if (upperLimitHelper == null) {
			upperLimitHelper = new UpperLimitHelper();
		}
		return upperLimitHelper;
	}
	
	public boolean getIsUnderLimit(int amount){
		if(App.getInstance().getPayed()){
			return true;
		}
		if(App.getInstance().getSharedString(Const.UPPER_LIMIT_TIME, "").equals("") || !areSameDay(new Date(Long.valueOf(App.getInstance().getSharedString(Const.UPPER_LIMIT_TIME, ""))), new Date(System.currentTimeMillis()))){
			App.getInstance().setCurrentValue(Const.UPPER_LIMIT_CURRENT, amount);
			App.getInstance().setSharedString(Const.UPPER_LIMIT_TIME, String.valueOf(System.currentTimeMillis()));
			return true;
		} else {
			if ((App.getInstance().getCurrentValue(Const.UPPER_LIMIT_CURRENT) + amount) > Const.MAX_LIMIT) {
				return false;
			} else {
				App.getInstance().setCurrentValue(Const.UPPER_LIMIT_CURRENT, App.getInstance().getCurrentValue(Const.UPPER_LIMIT_CURRENT) + amount);
				return true;
			}
		}
	}
	
	public int getRemaind(){
		if(App.getInstance().getPayed()){
			return Const.MAX_LIMIT;
		}
		if(App.getInstance().getSharedString(Const.UPPER_LIMIT_TIME, "").equals("") || !areSameDay(new Date(Long.valueOf(App.getInstance().getSharedString(Const.UPPER_LIMIT_TIME, ""))), new Date(System.currentTimeMillis()))){
			return Const.MAX_LIMIT;
		} else {
			return Const.MAX_LIMIT - App.getInstance().getCurrentValue(Const.UPPER_LIMIT_CURRENT);
		}
	}
	
	
	public boolean areSameDay(Date dateA,Date dateB) {
	    Calendar calDateA = Calendar.getInstance();
	    calDateA.setTime(dateA);

	    Calendar calDateB = Calendar.getInstance();
	    calDateB.setTime(dateB);

	    return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
	            && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
	            &&  calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
	}

}
