package com.hjw.cet4.entities;

import java.io.Serializable;

public class Word implements Serializable{
	
	public int id;
	public String subject;
	public String answer;    //在这里是翻译
//	public String picture_url;
	public String phonetic;   //音标
	public String sentence1;   //例句
	public String sentence2;   //例句
//	public String voice_frequency;    //音频文件地址
	public String soundmark; 
	
	public int list_id;
	
	
//	public boolean complete;


	public Word(int id, String subject, String answer, String phonetic,
			String sentence1, String sentence2, int list_id) {
		super();
		this.id = id;
		this.subject = subject;
		this.answer = answer;
		this.phonetic = phonetic;
		this.sentence1 = sentence1;
		this.sentence2 = sentence2;
		this.list_id = list_id;
	}
	
	public Word(int id, String subject, String answer, String phonetic,
			String sentence1, String sentence2, int list_id, String soundmark) {
		super();
		this.id = id;
		this.subject = subject;
		this.answer = answer;
		this.phonetic = phonetic;
		this.sentence1 = sentence1;
		this.sentence2 = sentence2;
		this.list_id = list_id;
		this.soundmark = soundmark;
	}

	
}
