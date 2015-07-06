package com.hjw.cet4.utils;

import java.util.ArrayList;
import java.util.List;

import com.hjw.cet4.App;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;

import fm.jihua.common.utils.AppLogger;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	
	public static final String DATABASE_USER_FILE = "cet4";
	public static final String DATABASE_TABLE_PIECES = "pieces";
	public static final String DATABASE_TABLE_PROBLEMS = "problems";
	
	private final static String DATABASE_NAME = DATABASE_USER_FILE;
	private final static int DATABASE_VERSION = 1;
	
	
	private final String TAG = getClass().getSimpleName();
	App mApp;
	// private Object transactionLock = new Object();

	Context mContext;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
		mApp = (App) mContext.getApplicationContext();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String piece_create = "CREATE TABLE IF NOT EXISTS "
				+ DATABASE_TABLE_PIECES
				+ "(ID INTEGER PRIMARY KEY, PRO_TYPE INTEGER);";
		String  problem_create = "CREATE TABLE IF NOT EXISTS "
				+ DATABASE_TABLE_PROBLEMS
				+ "(ID INTEGER PRIMARY KEY, RESULT TEXT);";
		db.beginTransaction();
		try {
			// Create tables & test data
			db.execSQL(piece_create);
			db.execSQL(problem_create);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			AppLogger.e(TAG,
					"Error creating tables and debug data" + e.getMessage());
		} finally {
			db.endTransaction();
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion) {
			return;
		}
		db.beginTransaction();
		try {
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			AppLogger.e(TAG,
					"Error creating tables and debug data" + e.getMessage());
		} finally {
			db.endTransaction();
		}
		
	}
	
	public void cleanData(SQLiteDatabase db) {
		execSQL(db, "delete from " + DATABASE_TABLE_PIECES + ";");
		execSQL(db, "delete from " + DATABASE_TABLE_PROBLEMS + ";");
	}
	
	public void execSQL(SQLiteDatabase db, String sql) {
		db.beginTransaction();
		try {
			// Create tables & test data
			db.execSQL(sql);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			AppLogger.e(TAG,
					"Error execSQL " + sql + ";ErrorMessage:" + e.getMessage());
		} finally {
			db.endTransaction();
		}
	}

	public void execSQL(SQLiteDatabase db, String sql, Object[] params) {
		db.beginTransaction();
		try {
			// Create tables & test data
			db.execSQL(sql, params);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			AppLogger.e(TAG,
					"Error execSQL " + sql + ";ErrorMessage:" + e.getMessage());
		} finally {
			db.endTransaction();
		}
	}
	
	
	//题的
	public void saveProblems(SQLiteDatabase db, List<Problem> problems) {
		db.beginTransaction();
		try {
			for (Problem problem : problems) {
				saveProblem(db, problem);
			}
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	
	public void saveProblem(SQLiteDatabase db, Problem problem) {
		deleteProblem(db, problem);
		addProblem(db, problem);
	}
	
	public void addProblem(SQLiteDatabase db, Problem problem) {
		try {
			db.execSQL("INSERT INTO " + DATABASE_TABLE_PROBLEMS
					+ "(ID, RESULT) " + "VALUES(?, ?);", new Object[] {
					String.valueOf(problem.id), problem.result });
		} catch (SQLException e) {
			AppLogger.e(TAG, e.getMessage(), e);
		}
	}
	
	public void deleteProblem(SQLiteDatabase db, Problem problem) {
		try {
			db.execSQL("delete from " + DATABASE_TABLE_PROBLEMS
					+ " where ID = ? ",
					new String[] { String.valueOf(problem.id) });
		} catch (SQLException e) {
			AppLogger.e(TAG, "Error execSQL deleteComment:" + problem.id
					+ ";ErrorMessage:" + e.getMessage());
		} finally {

		}
	}
	
	
	
	//篇的
	public void savePieces(SQLiteDatabase db, List<Piece> pieces) {
		db.beginTransaction();
		try {
			for (Piece piece : pieces) {
				savePiece(db, piece);
			}
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	
	public void savePiece(SQLiteDatabase db, Piece piece) {
		deletePiece(db, piece);
		addPiece(db, piece);
	}
	
	public void addPiece(SQLiteDatabase db, Piece piece) {
		try {
			db.execSQL("INSERT INTO " + DATABASE_TABLE_PIECES
					+ "(ID, PRO_TYPE) " + "VALUES(?, ?);", new Object[] {
					String.valueOf(piece.id), piece.type });
		} catch (SQLException e) {
			AppLogger.e(TAG, e.getMessage(), e);
		}
	}
	
	public void deletePiece(SQLiteDatabase db, Piece piece) {
		try {
			db.execSQL("delete from " + DATABASE_TABLE_PIECES
					+ " where ID = ? ",
					new String[] { String.valueOf(piece.id) });
		} catch (SQLException e) {
			AppLogger.e(TAG, "Error execSQL deleteComment:" + piece.id
					+ ";ErrorMessage:" + e.getMessage());
		} finally {

		}
	}
	
	public List<String> getPieceIdsByType (SQLiteDatabase db, int type){
		List<String> pieceIds = new ArrayList<String>();
		try {
			Cursor cursor = db.rawQuery("SELECT * FROM "
					+ DATABASE_TABLE_PIECES + " where PRO_TYPE = ? ;", new String[] { String.valueOf(type) });
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					pieceIds.add(String.valueOf(cursor.getInt(0)));
					cursor.moveToNext();
				}
				cursor.close();
				return pieceIds;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			AppLogger.e(TAG, "Error getRecords" + e.getMessage());
		}
		return pieceIds;
	}
	
	public String getResultByProblem (SQLiteDatabase db, Problem problem){
		String result = null;
		try {
			Cursor cursor = db.rawQuery("SELECT * FROM "
					+ DATABASE_TABLE_PROBLEMS + " where ID = ? ;", new String[] { String.valueOf(problem.id) });
			int count = cursor.getCount();
			if (count > 0) {
				cursor.moveToFirst();
				result = cursor.getString(1);
				cursor.close();
				return result;
			} else {
				cursor.close();
			}
		} catch (SQLException e) {
			AppLogger.e(TAG, "Error getRecords" + e.getMessage());
		}
		return result;
	}
	
	public void getResultByProblems (SQLiteDatabase db, List<Problem> problems){
		db.beginTransaction();
		try {
			for (Problem problem : problems) {
				problem.setResult(getResultByProblem(db, problem));
			}
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

}
