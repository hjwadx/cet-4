package com.hjw.cet4.entities;

import java.io.Serializable;

public class Strategy implements Serializable{
	
	public String name;
	public String url;
	
	
	public Strategy(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}
}
