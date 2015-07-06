package com.hjw.cet4.entities;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hjw.cet4.utils.CommonUtils;
import com.hjw.cet4.utils.Const;

import android.os.Environment;


public class Piece implements Serializable{
	
	public int id;
	public String original;
	public String options;
	
	public String voice_url;
	
//	public int last_position;   //local
	
	public int start;
	public int end;
	public int type;
	public int paper_id;
	
	public boolean isDownloading;  //local


	public Piece(int id, String original, String options, String voice_url) {
		super();
		this.id = id;
		this.original = original;
		this.options = options;
		this.voice_url = voice_url;
	}
	
	public Piece(int id, int paper_id, int start, int end, String original, String options, String voice_url, int type) {
		super();
		this.id = id;
		this.original = original;
		this.options = options;
		this.voice_url = voice_url;
		
		this.paper_id = paper_id;
		this.start = start;
		this.end = end;
		this.type = type;
	}
	
	
//	public int getLastPosition() {
//		return last_position;
//	}
//
//
//	public void setLastPosition(int last_position) {
//		this.last_position = last_position;
//	}
	
	public String getVoiceFileName(){
		if(CommonUtils.isNullString(voice_url)){
			return "不存在";
		}
//		return voice_url.substring(voice_url.lastIndexOf("/") + 1, voice_url.lastIndexOf("?"));
		return "piece"+ id + "_" + voice_url.substring(voice_url.lastIndexOf("/") + 1, voice_url.length());
	}
	
	public String getDownloadUrlString(){
		return voice_url;
	}
	
	public boolean isVoiceDownloaded(){
		File file = new File(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + getVoiceFileName());
		if (file.exists()) {
			return true;
		}
		return false;
	}
	
	public void setDownloading(boolean is_downloading){
		isDownloading = is_downloading;
	}
	
	public boolean  isDownloading(){
		return isDownloading;
	}
	

	public static List<Piece> getNextPieces(int lastPieceId, List<Piece> pieces, int count) {
		List<Piece> list = new ArrayList<Piece>();
		if(lastPieceId == 0 || lastPieceId < pieces.get(0).id){
			for(int i=0 ; i < count; i++){
				list.add(pieces.get(i));
			}
		} else {
			Piece first_piece = null;
			for(Piece piece : pieces){
				if(piece.id > lastPieceId){
					first_piece = piece;
					break;
				}
			}
			if(first_piece == null){
				return getNextPieces(0, pieces, count);
			}
			int index = pieces.indexOf(first_piece);
			while (count > 0) {
				if(index >= pieces.size()){
					index = 0;
				}
				list.add(pieces.get(index));
				index++;
				count--;
			}
		}
		return list;
	}
}
