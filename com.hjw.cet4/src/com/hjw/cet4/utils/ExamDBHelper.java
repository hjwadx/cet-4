package com.hjw.cet4.utils;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.hjw.cet4.entities.Jotter;
import com.hjw.cet4.entities.Paper;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;
import com.hjw.cet4.entities.Word;
import com.hjw.cet4.entities.WordList;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class ExamDBHelper extends SQLiteAssetHelper {
	Context context;

	private static final String DATABASE_NAME = "exams";
    private static final int DATABASE_VERSION = 1;

	public ExamDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.setForcedUpgradeVersion(2);
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
	}
	
	public List<Paper> getPapers() {
		List<Paper> list = new ArrayList<Paper>();
		try {
			Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM cets order by id desc;", null);
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Paper item = new Paper(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
					list.add(item);
					cursor.moveToNext();
				}
				cursor.close();
				return list;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Paper getPaperById(int id) {
		Paper item = null;
		try {
			Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM cets where id = " + id + ";", null);
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				item = new Paper(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
				cursor.close();
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return item;
	}
	
	public List<Piece> getPiecesByPaperId(int paper_id) {
		List<Piece> list = new ArrayList<Piece>();
		try {
			Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM groups where cet_id = ? order by num;", new String[]{String.valueOf(paper_id)});
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
//					groups(
//					        id integer PRIMARY KEY AUTOINCREMENT,
//					        cet_id integer,
//					        num integer,
//					        start integer,
//					        end integer,
//					        content text,
//					        options text,
//					        audio text,
//					        is_free tinyint(1),
//					        part_id integer
//					      );
					// Piece(int id, int paper_id, int start, int end, String original, String options, String voice_url, int type)
					Piece item = new Piece(cursor.getInt(0), cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(9));
					list.add(item);
					cursor.moveToNext();
				}
				cursor.close();
				return list;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Problem> getProblemsByPaperId(int paper_id) {
		List<Problem> list = new ArrayList<Problem>();
		try {
//			SELECT id, num FROM problems where group_id in(SELECT id FROM groups where cet_id = 17 order by num) order by num;
			Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM problems where group_id in(SELECT id FROM groups where cet_id = ? order by num) order by num;", new String[]{String.valueOf(paper_id)});
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
//				problems(
//			        id INTEGER primary key AUTOINCREMENT,
//			        name text,
//			        num integer,
//			        content text,
//			        options text,
//			        answer text,
//			        analysis text,
//			        audio text,
//			        cet_id text,
//			        describe text,
//			        group_id integer,
//			        is_free tinyint(1),
//			        part_id integer
//			      );
//					      );
					//  Problem(int id, String subject, int num, String options, String answer, String analyze, int piece_id, int type) 
					Problem item = new Problem(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(10), cursor.getInt(12));
					list.add(item);
					cursor.moveToNext();
				}
				cursor.close();
				return list;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<Piece> getPiecesByType(int type) {
		List<Piece> list = new ArrayList<Piece>();
		try {
			Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM groups where part_id = ? and attr_tag = ? order by id;", new String[]{String.valueOf(type), String.valueOf(0)});
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
//					groups(
//					        id integer PRIMARY KEY AUTOINCREMENT,
//					        cet_id integer,
//					        num integer,
//					        start integer,
//					        end integer,
//					        content text,
//					        options text,
//					        audio text,
//					        is_free tinyint(1),
//					        part_id integer
//					      );
					// Piece(int id, int paper_id, int start, int end, String original, String options, String voice_url, int type)
					Piece item = new Piece(cursor.getInt(0), cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(9));
					list.add(item);
					cursor.moveToNext();
				}
				cursor.close();
				return list;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Problem> getProblemsByType(int type) {
		List<Problem> list = new ArrayList<Problem>();
		try {
//			SELECT id, num FROM problems where group_id in(SELECT id FROM groups where cet_id = 17 order by num) order by num;
			Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM problems where part_id = ? order by group_id, num;", new String[]{String.valueOf(type)});
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
//				problems(
//			        id INTEGER primary key AUTOINCREMENT,
//			        name text,
//			        num integer,
//			        content text,
//			        options text,
//			        answer text,
//			        analysis text,
//			        audio text,
//			        cet_id text,
//			        describe text,
//			        group_id integer,
//			        is_free tinyint(1),
//			        part_id integer
//			      );
//					      );
					//  Problem(int id, String subject, int num, String options, String answer, String analyze, int piece_id, int type) 
					Problem item = new Problem(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(10), cursor.getInt(12));
					list.add(item);
					cursor.moveToNext();
				}
				cursor.close();
				return list;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<Problem> getProblemsByPieceId(List<String> pieceIds) {
		List<Problem> list = new ArrayList<Problem>();
		try {
			String pieceIdString = CommonUtils.join(pieceIds, ",");
//			SELECT id, num FROM problems where group_id in(SELECT id FROM groups where cet_id = 17 order by num) order by num;
			Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM problems where group_id in(" + pieceIdString + ") order by group_id, num;", null);
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
//				problems(
//			        id INTEGER primary key AUTOINCREMENT,
//			        name text,
//			        num integer,
//			        content text,
//			        options text,
//			        answer text,
//			        analysis text,
//			        audio text,
//			        cet_id text,
//			        describe text,
//			        group_id integer,
//			        is_free tinyint(1),
//			        part_id integer
//			      );
//					      );
					//  Problem(int id, String subject, int num, String options, String answer, String analyze, int piece_id, int type) 
					Problem item = new Problem(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(10), cursor.getInt(12));
					list.add(item);
					cursor.moveToNext();
				}
				cursor.close();
				return list;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Piece> getPiecesByPieceId(List<String> pieceIds) {
		List<Piece> list = new ArrayList<Piece>();
		try {
			String pieceIdString = CommonUtils.join(pieceIds, ",");
			Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM groups where id in(" + pieceIdString + ") order by id;", null);
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
//					groups(
//					        id integer PRIMARY KEY AUTOINCREMENT,
//					        cet_id integer,
//					        num integer,
//					        start integer,
//					        end integer,
//					        content text,
//					        options text,
//					        audio text,
//					        is_free tinyint(1),
//					        part_id integer
//					      );
					// Piece(int id, int paper_id, int start, int end, String original, String options, String voice_url, int type)
					Piece item = new Piece(cursor.getInt(0), cursor.getInt(1), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(9));
					list.add(item);
					cursor.moveToNext();
				}
				cursor.close();
				return list;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	//已下是单词
	public List<Jotter> getJotters() {
		List<Jotter> list = new ArrayList<Jotter>();
		try {
			Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM books;", null);
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Jotter item = new Jotter(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
					list.add(item);
					cursor.moveToNext();
				}
				cursor.close();
				return list;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Jotter getJottersById(int id) {
		Jotter jotter = null;
		try {
			Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM books where id = ?;",  new String[]{String.valueOf(id)});
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				jotter = new Jotter(cursor.getInt(0), cursor.getString(1),cursor.getInt(2));
				cursor.moveToNext();
				cursor.close();
				return jotter;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jotter;
	}
	
	public List<WordList> getWordListsByJotterId(int id) {
		List<WordList> list = new ArrayList<WordList>();
		try {
			Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM lists where book_id = ? order by num;", new String[]{String.valueOf(id)});
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					WordList item = new WordList(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
					list.add(item);
					cursor.moveToNext();
				}
				cursor.close();
				return list;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public WordList getWordListsById(int id) {
		WordList wordlist = null;
		try {
			Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM lists where id = ?;", new String[]{String.valueOf(id)});
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				wordlist = new WordList(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
				cursor.close();
				return wordlist;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wordlist;
	}
	
	public List<Word> getWordsByWordListId(int id) {
		List<Word> list = new ArrayList<Word>();
		try {
			Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM vocabularies where list_id = ? order by id;", new String[]{String.valueOf(id)});
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
//					Word item = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(3), cursor.getString(2), cursor.getString(4), cursor.getString(5), cursor.getInt(7));
					Word item = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(3), cursor.getString(2), cursor.getString(4), cursor.getString(5), cursor.getInt(7), cursor.getString(2));
					list.add(item);
					cursor.moveToNext();
				}
				cursor.close();
				return list;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
