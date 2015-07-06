package com.hjw.cet4.ui.activity.word;

import java.util.List;

import com.hjw.cet4.entities.Word;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class WordFragmentPagerAdapter extends FragmentStatePagerAdapter{
	
	List<Word> mWords;

	public WordFragmentPagerAdapter(FragmentManager fm, List<Word> words) {
		super(fm);
		mWords = words;
	}

	@Override
	public Fragment getItem(int arg0) {
		return WordFragment.newInstance(mWords.get(arg0));
	}

	@Override
	public int getCount() {
		return mWords.size();
	}

}
