package com.hjw.cet4.entities;

import java.io.Serializable;

public class Jotter implements Serializable{
	
	public int id;
	public String name;
	public int free;
	
	public Jotter(int id, String name, int free) {
		super();
		this.id = id;
		this.name = name;
		this.free = free;
	}

}
