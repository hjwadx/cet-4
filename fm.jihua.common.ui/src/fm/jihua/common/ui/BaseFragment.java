package fm.jihua.common.ui;

import fm.jihua.common.App;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
	protected FragmentActivity parent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (FragmentActivity)getActivity();
	}
	
	protected View findViewById(int id){
		if (getActivity() != null) {
			return getActivity().findViewById(id);
		}
		return null;
	}
	
	protected App getApplication() {
		return (App) getActivity().getApplication();
	}
	
	protected void setContentView(View view){
		if (getView() != null && ((ViewGroup)getView()).getChildAt(0) != view) {
			((ViewGroup)getView()).removeAllViews();
			((ViewGroup)getView()).addView(view);
		}
	}
}
