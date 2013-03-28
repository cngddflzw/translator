package org.uestc.translator.core;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.uestc.translator.DbHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LocalDatabase {
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	private Cursor cursor;
	
	public LocalDatabase(Activity act) {
		dbHelper = new DbHelper(act, DbHelper.DB_NAME, null, 1);
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * 获取本地最近查询单词
	 * @return
	 */
	public Set<String> getHistory() {
		Set<String> set = new LinkedHashSet<String>();
		cursor = db.query(DbHelper.TB_HISTORY, null, null, null, null, null, null);
		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			String word = cursor.getString(1);
			set.add(word);
		}
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
	 * 获取本地生词本
	 * @return
	 */
	public Set<String> getNewWords() {
		Set<String> set = new TreeSet<String>();
		cursor = db.query(DbHelper.TB_NEW_WORD, null, null, null, null, null, null);
		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			String word = cursor.getString(1);
			set.add(word);
		}
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
					+ DbHelper.COL_NW_WORD + " ) " + "VALUES ( " +
					word + " )");
		}
		db.setTransactionSuccessful();
		db.endTransaction(); 
	}
}