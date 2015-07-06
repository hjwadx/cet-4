package com.hjw.cet4.utils;

public class Const {
	
	public static final String TAG = "cet4";
	
	public static final boolean TEST = false;
	
	public static final String[] OPTIONS = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	
	public static final int FILE_IMAGE_MAX_WIDTH = 800;
	
	public static final String DOWNLOAD_FOLDER_PATH = "/cet4/download/";
	public static final String ROOT_DIR = "/cet4/";
	public static final String IMAGE_DIR = "/cet4/images/";
	public static final String AUDIO_DIR = "/cet4/audios/";
	
	public static final String REST_HOST = "http://exams_server.kechenggezi.com";
	
	public static final String PAY_PRICE = TEST ? "0.01" : "6.00";
	public static final String PREFERENCE_WEIBO_TOKEN = "weibo_token";
	public static final String PREFERENCE_WEIBO_ID = "weibo_id";
	public static final String PREFERENCE_WEIBO_EXPIRES = "weibo_expires";
	//TODO 需要替换
	public static final String WEIBO_CONSUMER_KEY = "3595477789";
	public static final String WEIBO_CONSUMER_SECRET = "9709d49f0f9835cac3e998c943feeff0";
	
	public static final int WEIBO = 1;
	public static final int WEIBO_COUNT_PER_REQUEST = 200;
	public static final String WEIBO_GEZI_ID = "2807417123";
	public static final int FEMALE = 0;
	public static final int MALE = 1;
	
	
	
	//Preference
//	public static final String WRITING = "writing";
//	public static final String SHORT_CONVERSATIONS = "short_conversations";
//	public static final String LONG_CONVERSATIONS = "long_conversations";
//	public static final String SHORT_PASSAGES = "short_passages";
//	public static final String PASSAGE_DICTATION = "passage_dictation";
//	public static final String WORDS_COMPREHENSION = "words_comprehension";
//	public static final String LONG_TO_READ = "long_to_read";
//	public static final String CAREFUL_READING = "careful_reading";
//	public static final String TRANSLATE = "translate";
	
	//保存当前做到了第几题
	public static final String WRITING_CURRENT = "writing_current";
	public static final String SHORT_CONVERSATIONS_CURRENT = "short_conversations_current";
	public static final String LONG_CONVERSATIONS_CURRENT = "long_conversations_current";
	public static final String SHORT_PASSAGES_CURRENT = "short_passages_current";
	public static final String PASSAGE_DICTATION_CURRENT = "passage_dictation_current";
	public static final String WORDS_COMPREHENSION_CURRENT = "words_comprehension_current";
	public static final String LONG_TO_READ_CURRENT = "long_to_read_current";
	public static final String CAREFUL_READING_CURRENT = "careful_reading_current";
	public static final String TRANSLATE_CURRENT = "translate_current";
	
	public static final String JOTTER_CURRENT = "jotter_current";
	public static final String WORDLIST_CURRENT = "wordlist_current";
	public static final String WORD_CURRENT = "word_current";
	
	public static final int MAX_LIMIT = 15;
	public static final String UPPER_LIMIT = "upper_limit";
	public static final String UPPER_LIMIT_CURRENT = "upper_limit_current";
	public static final String UPPER_LIMIT_TIME = "upper_limit_time";
	
	public static final String DONE_PAPER_LIST = "done_paper_list";
	
	public static final String SLIDE_TUTOR = "slide_tutor";
	
	public static final String PREFERENCE_NEWEST_VERSION_CODE = "newest_version_code";
	public static final String PREFERENCE_LAST_TIME_NOTIFY = "last_time_notify";
	public static final int UPDATE_NOTIFY_INTERVAL = 7*24*3600*1000;  //7天	

}
