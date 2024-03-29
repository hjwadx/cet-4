package com.mozillaonline.downloadprovider;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.DownloadManager.Request;
import com.mozillaonline.providers.downloads.DownloadService;
import com.mozillaonline.providers.downloads.ui.DownloadList;

public class DownloadProviderActivity extends Activity implements OnClickListener {
	@SuppressWarnings("unused")
	private static final String TAG = DownloadProviderActivity.class.getName();

	private BroadcastReceiver mReceiver;

	EditText mUrlInputEditText;
	Button mStartDownloadButton;
	DownloadManager mDownloadManager;
	Button mShowDownloadListButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mDownloadManager = new DownloadManager(getContentResolver(), getPackageName());
		buildComponents();
		startDownloadService();

		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				showDownloadList();
			}
		};

		registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	private void buildComponents() {
		mUrlInputEditText = (EditText) findViewById(R.id.url_input_edittext);
		mStartDownloadButton = (Button) findViewById(R.id.start_download_button);
		mShowDownloadListButton = (Button) findViewById(R.id.show_download_list_button);

		mStartDownloadButton.setOnClickListener(this);
		mShowDownloadListButton.setOnClickListener(this);

		mUrlInputEditText.setText("http://down.mumayi.com/41052/mbaidu");
	}

	private void startDownloadService() {
		Intent intent = new Intent();
		intent.setClass(this, DownloadService.class);
		startService(intent);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.start_download_button) {
			startDownload();
		} else if (id == R.id.show_download_list_button) {
			showDownloadList();
		} else {
		}
	}

	private void showDownloadList() {
		Intent intent = new Intent();
		intent.setClass(this, DownloadList.class);
		startActivity(intent);
	}

	private void startDownload() {
		String url = mUrlInputEditText.getText().toString();
		Uri srcUri = Uri.parse(url);
		DownloadManager.Request request = new Request(srcUri);
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/");
		request.setDescription("ddd  Just for test");
		mDownloadManager.enqueue(request);
	}
}