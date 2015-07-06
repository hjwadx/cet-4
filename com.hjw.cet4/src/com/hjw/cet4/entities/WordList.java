package com.hjw.cet4.entities;

import java.io.Serializable;

public class WordList implements Serializable{
	
	public int id;
	public String name;
	public int num;
	public int jotter_id;
	
	public WordList(int id, String name, int num, int jotter_id) {
		super();
		this.id = id;
		this.name = name;
		this.num = num;
		this.jotter_id = jotter_id;
	}
	
	
}
