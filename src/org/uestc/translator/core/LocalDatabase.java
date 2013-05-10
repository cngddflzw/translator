package org.uestc.translator.core;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.uestc.translator.DbHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LocalDatabase {
	private static LocalDatabase ldb;
	private final static Object syncRoot = new Object();
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	private Cursor cursor;
	
	private LocalDatabase(Activity act) {
		dbHelper = new DbHelper(act, DbHelper.DB_NAME, null, 1);
		db = dbHelper.getWritableDatabase();
	}
	
	public static LocalDatabase getInstance(Activity act) {
		if (ldb == null) {
			synchronized (syncRoot) {
				if (ldb == null) {
					ldb = new LocalDatabase(act);
				}
			}
		}
		return ldb;
	}
	
	/**
	 * 获取本地最近查询单词
	 * @return
	 */
	public Set<String> getHistory() {
		Set<String> set = new LinkedHashSet<String>();
		try {
			cursor = db.query(DbHelper.TB_HISTORY, null, null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String word = cursor.getString(1);
				set.add(word);
				cursor.moveToNext();
			}
		} finally {
			cursor.close();
		}
//		Log.i("startHis", set.toString());
		return set;
	}
	
	/**
	 * 添加本地最近查询单词
	 * @param word
	 * @return
	 */
	public long addHistory(String word) {
		ContentValues cv = new ContentValues();
		cv.put(DbHelper.COL_HIS_WORD, word.trim());
		long rowId = db.insert(DbHelper.TB_HISTORY, null, cv);
		return rowId;
	}
	
	public void addHistory(Set<String> words) {
		db.beginTransaction();
		for (String word : words) {
			ContentValues cv = new ContentValues();
			cv.put(DbHelper.COL_HIS_WORD, word.trim());
			db.insert(DbHelper.TB_HISTORY, null, cv);
		}
		db.setTransactionSuccessful();
		db.endTransaction(); 
	}
	
	/**
	 * 重置所有历史单词
	 * @param words
	 */
	public void refreshHistory(Set<String> words) {
		db.beginTransaction();
		db.delete(DbHelper.TB_HISTORY, null, null);
		for (String word : words) {
			ContentValues cv = new ContentValues();
			cv.put(DbHelper.COL_HIS_WORD, word.trim());
			db.insert(DbHelper.TB_HISTORY, null, cv);
		}
		db.setTransactionSuccessful();
		db.endTransaction(); 
	}
	
	/**
	 * 获取本地生词本
	 * @return
	 */
	public Set<String> getNewWords() {
		Set<String> set = new TreeSet<String>();
		try {
			cursor = db.query(DbHelper.TB_NEW_WORD, null, null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String word = cursor.getString(1);
				set.add(word);
				cursor.moveToNext();
			}
		} finally {
			cursor.close();
		}
//		Log.i("startNew", set.toString());
		return set;
	}
	
	/**
	 * 添加本地生词
	 * @param text
	 * @return
	 */
	public long addNewWord(String word) {
		ContentValues cv = new ContentValues();
		cv.put(DbHelper.COL_NW_WORD, word.trim());
		long rowId = db.insert(DbHelper.TB_NEW_WORD, null, cv);
		return rowId;
	}
	
	public void addNewWord(Set<String> words) {
		db.beginTransaction();
		for (String word : words) {
			db.execSQL("REPLACE INTO " + DbHelper.TB_NEW_WORD + " ( " 
					+ DbHelper.COL_NW_WORD + " ) " + "VALUES ( \"" +
					word + "\" )");
		}
		db.setTransactionSuccessful();
		db.endTransaction(); 
	}
	
	/**
	 * 重置所有生词
	 * @param words
	 */
	public void refreshNewWord(Set<String> words) {
		db.beginTransaction();
		db.delete(DbHelper.TB_NEW_WORD, null, null);
		for (String word : words) {
			ContentValues cv = new ContentValues();
			cv.put(DbHelper.COL_NW_WORD, word.trim());
			db.insert(DbHelper.TB_NEW_WORD, null, cv);
		}
		db.setTransactionSuccessful();
		db.endTransaction(); 
	}
	
	/**
	 * 获取所有出错单词
	 * @return
	 */
	public Set<String> getMistakeWords() {
		Set<String> set = new HashSet<String>();
		try {
			cursor = db.query(DbHelper.TB_MISTAKE, null, null, null, null, null,
					DbHelper.COL_MISTAKE_TIME + " DESC", "10");
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String word = cursor.getString(1);
				set.add(word);
				cursor.moveToNext();
			}
		} finally {
			cursor.close();
		}
		return set;
	}
	
	/**
	 * 添加出错单词
	 * @param word
	 */
	public long addMistakeWord(String word) {
		// 如果单词已经存在，单词出错次数+1
		long rowId = -1;
		String selection = DbHelper.COL_MISTAKE_WORD + " = ?";
		String[] selectionArgs = { word };
		cursor = db.query(DbHelper.TB_MISTAKE, null, 
				selection, selectionArgs, null, null, null, "1");
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			ContentValues cv = new ContentValues();
			cv.put(DbHelper.COL_MISTAKE_WORD, cursor.getString(1));
			cv.put(DbHelper.COL_MISTAKE_TIME, Integer.valueOf(cursor.getString(2)) + 1);
			rowId = db.update(DbHelper.TB_MISTAKE, cv,
					DbHelper.COL_MISATAKE_WORD_ID + " = " + cursor.getString(0), null);
		} else {
			// 添加新单词
			ContentValues cv = new ContentValues();
			cv.put(DbHelper.COL_MISTAKE_WORD, word);
			cv.put(DbHelper.COL_MISTAKE_TIME, 1);
			rowId = db.insert(DbHelper.TB_MISTAKE, null, cv);
		}
		return rowId;
	}
	
	public void close() {
		db.close();
	}
}