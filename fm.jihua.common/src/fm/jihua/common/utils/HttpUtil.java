package fm.jihua.common.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Build;
import android.util.Log;


public class HttpUtil {
//	
//	private static String _token;
//	
//	public static String getToken() {
//		return _token;
//	}
//	
//	public static void setToken(String token) {
//		_token = token;
//	}
//	
//	private static String _version;
//	
//	
//	public static String getVersion() {
//		return _version;
//	}
//
//	public static void setVersion(String version) {
//		_version = version;
//	}

//	public static String getStringFromUrl(String url)
//			throws ClientProtocolException, IOException, JSONException {
//		return getStringFromUrl(url, true, null, false);
//	}
//	
//	public static String getAuthUrl(String url){
//		url += url.contains("?") ? "&" : "?";
//		if (getToken() != null) {
//			url += "token=" + URLEncoder.encode(getToken());
//		}
//		return url;
//	}
	
//	private static String CLIENT_USER_AGENT = "android api(version:" + getVersion() + ")";
	
//	public static String getStringFromUrl(String url, boolean get,
//			List<NameValuePair> params, boolean password) throws ClientProtocolException,
//			IOException, JSONException {
//		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters,
//				Const.TIMEOUT_CONNECTION);
//		HttpConnectionParams.setSoTimeout(httpParameters, Const.TIMEOUT_SOCKET);
//		disableConnectionReuseIfNecessary();
//		HttpClient httpclient = new DefaultHttpClient(httpParameters);
//		// Prepare a request object
//		HttpRequestBase httpRequest;
//		if (get) {
//			url = getAuthUrl(url);
//			Locale myPhoneLocale = Locale.getDefault();
//			if (Locale.CHINA.equals(myPhoneLocale)) {
//				url += "&locale=ch";
//			}
//			url += "&from_app=true";
//			if (getVersion() != null) {
//				url += "&version="+getVersion();
//			}
//			if (params != null) {
//				for (NameValuePair p : params) {
//					url += "&" + p.getName() + "=" + p.getValue();
//				}
//			}
//			httpRequest = new HttpGet(url);
//			if (password){
//				httpRequest.addHeader(BasicScheme.authenticate(
//						 new UsernamePasswordCredentials("1029384756", "qwertyuiop"),
//						 "UTF-8", false));
//			}
//		} else {
//			httpRequest = new HttpPost(url);
//
//			if (getToken() != null) {
//				params.add(new BasicNameValuePair("token", ""
//						+ URLEncoder.encode(getToken())));
//			}
//			Locale myPhoneLocale = Locale.getDefault();
//			if (Locale.CHINA.equals(myPhoneLocale)) {
//				params.add(new BasicNameValuePair("locale", "ch"));
//			}
//			JSONObject jsonObject = new JSONObject();
//			for (NameValuePair p : params) {
//				jsonObject.put(p.getName(), p.getValue());
//			}
//			StringEntity se = new StringEntity( jsonObject.toString(), HTTP.UTF_8 );
//            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//            se.setContentEncoding("UTF-8");
//            
//			((HttpPost) httpRequest).setEntity(se);
//		}
//
//		httpRequest.addHeader("User-Agent", CLIENT_USER_AGENT);
//		httpRequest.addHeader("accept", "application/json");
//		httpRequest.addHeader("Content-Type", "application/json");
//		httpRequest.setHeader("Accept-Encoding", "gzip");
//
//		HttpResponse response = httpclient.execute(httpRequest);
//		// Get hold of the response entity
//		HttpEntity entity = response.getEntity();
//		// If the response does not enclose an entity, there is no need
//		// to worry about connection release
//		if (entity == null) {
//			Log.w(Const.TAG, "entity null? " + url);
//			return null; // when is this ever reached?
//		}
//
//		InputStream instream = null;
//		try {
//			org.apache.http.Header[] headers = response.getHeaders("Content-Encoding");
//			if (headers.length > 0 && "gzip".equals(headers[0].getValue())) {
//				instream = new GZIPInputStream(entity.getContent());
//			}else {
//				instream = entity.getContent();
//			}
//			return convertStreamToString(instream);
//		} finally {
//			closeQuietly(instream);
//		}
//	}
	
//	public static String postJSON(String url,
//			JSONObject data) throws ClientProtocolException,
//			IOException, JSONException {
//		return postJSON(url, data, "post");
//	}
//	
//	public static String postJSON(String url,
//			JSONObject data, String method) throws ClientProtocolException,
//			IOException, JSONException {
//		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters,
//				Const.TIMEOUT_CONNECTION);
//		disableConnectionReuseIfNecessary();
//		HttpConnectionParams.setSoTimeout(httpParameters, Const.TIMEOUT_SOCKET);
//
//		HttpClient httpclient = new DefaultHttpClient(httpParameters);
//
//		// Prepare a request object
//		HttpRequestBase httpRequest;
//		
//		if (method.equals("put")) {
//			httpRequest = new HttpPut(url);
//		}else {
//			httpRequest = new HttpPost(url);
//		}
//		
//		if (getToken() != null) {
//			data.put("token", URLEncoder.encode(getToken()));
//		}
//		Locale myPhoneLocale = Locale.getDefault();
//		if (Locale.CHINA.equals(myPhoneLocale)) {
//			data.put("locale", "ch");
//		}
//		data.put("from_app", true);
//		
//		if (getVersion() != null) {
//			url += "&version="+getVersion();
//		}
//		
//		StringEntity se = new StringEntity( data.toString(), HTTP.UTF_8 );  
//        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//        
//        ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(se);
//		
//		httpRequest.addHeader("User-Agent", CLIENT_USER_AGENT);
//		httpRequest.addHeader("accept", "application/json");
//		httpRequest.addHeader("Content-Type", "application/json");
//		httpRequest.setHeader("Accept-Encoding", "gzip");
//
//		HttpResponse response = httpclient.execute(httpRequest);
//		// Get hold of the response entity
//		HttpEntity entity = response.getEntity();
//
//		// If the response does not enclose an entity, there is no need
//		// to worry about connection release
//		if (entity == null) {
//			Log.w(Const.TAG, "entity null? " + url);
//			return null; // when is this ever reached?
//		}
//
//		InputStream instream = null;
//		try {
//			org.apache.http.Header[] headers = response.getHeaders("Content-Encoding");
//			if (headers.length > 0 && "gzip".equals(headers[0].getValue())) {
//				instream = new GZIPInputStream(entity.getContent());
//			}else {
//				instream = entity.getContent();
//			}
//			return convertStreamToString(instream);
//		} finally {
//			closeQuietly(instream);
//		}
//	}
	
	public static String convertStreamToString(InputStream is)
			throws IOException {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is), 1024);
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
			return sb.toString();
		} finally {
			closeQuietly(is);
		}
	}

	/**
	 * @param stream
	 *            object to close, which can safely be <code>null</code>
	 */
	public static void closeQuietly(Closeable stream) {
		try {
			if (stream != null) {
				stream.close();
			}
		} catch (IOException e) {
			Log.e(Const.TAG, "could not close stream", e);
		}
	}
	
	/**
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }
}
