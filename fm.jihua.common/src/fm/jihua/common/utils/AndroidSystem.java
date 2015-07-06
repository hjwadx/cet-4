package fm.jihua.common.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AndroidSystem {
	public static void shareText(Context context, String title, String text) {
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("text/plain");
	    intent.putExtra(Intent.EXTRA_SUBJECT, title);
	    intent.putExtra(Intent.EXTRA_TEXT, text);
	    context.startActivity(Intent.createChooser(intent, "选择分享方式"));
	}
	
	public static void shareImage(Context context, String title, Uri image) {
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("image/*");
	    intent.putExtra(Intent.EXTRA_SUBJECT, title);
	    intent.putExtra(Intent.EXTRA_STREAM, image);
	    context.startActivity(Intent.createChooser(intent, "选择分享方式"));
	}
	
	public static void shareTextToSMS(Context context, String text){
		Uri uri = Uri.parse("smsto:");   
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);   
		it.putExtra("sms_body", text);
		context.startActivity(Intent.createChooser(it, "发送短信...")); 
	}
	
	
	public static void shareTextToEmail(Context context, String title, String text) {
		Uri uri = Uri.parse("mailto:");
	    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
	    intent.putExtra(Intent.EXTRA_SUBJECT, title);
	    intent.putExtra(Intent.EXTRA_TEXT, text);
//	    intent.setType("message/rfc822");
	    context.startActivity(Intent.createChooser(intent, "发送邮件..."));
	}
	
	public static void shareToEmail(Context context, String title, String text, Uri attatchment) {
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.putExtra(Intent.EXTRA_SUBJECT, title);
	    intent.putExtra(Intent.EXTRA_TEXT, text);
	    intent.putExtra(Intent.EXTRA_STREAM, attatchment);
	    intent.setType("message/rfc822");
	    context.startActivity(Intent.createChooser(intent, "发送邮件..."));
	}
}
