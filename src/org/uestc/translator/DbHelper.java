package org.uestc.translator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "dictionary";
	public static final String TB_NEW_WORD = "new_words";	// 生词表
	public static final String TB_HISTORY = "history";	// 历史表
	public static final String TB_MISTAKE = "mistake";	// 错词表
	public static final String COL_NW_WORD_ID = "word_id";	// 生词id
	public static final String COL_NW_WORD = "word";	// 生词内容
	public static final String COL_MISATAKE_WORD_ID = "word_id";	// 生词内容
	public static final String COL_MISTAKE_WORD = "word";	// 错词内容
	public static final String COL_MISTAKE_TIME = "time";	// 错词次数
	public static final String COL_HIS_WORD_ID = "word_id";	// 历史单词id
	public static final String COL_HIS_WORD = "word";	// 历史单词内容

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NEW_WORD +
				" ( " + COL_NW_WORD_ID + " integer primary key autoincrement," +
				COL_NW_WORD  + " varchar not null unique )");
		arg0.execSQL("CREATE TABLE IF NOT EXISTS " + TB_HISTORY +
				" ( " + COL_HIS_WORD_ID + " integer primary key autoincrement," +
				COL_HIS_WORD + " varchar not null )");
		arg0.execSQL("CREATE TABLE IF NOT EXISTS " + TB_MISTAKE +
				" ( " + COL_MISATAKE_WORD_ID + " integer primary key autoincrement," +
				COL_MISTAKE_WORD + " varchar not null," +
						COL_MISTAKE_TIME + " integer not null )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		arg0.execSQL("DROP TABLE IF EXISTS " + TB_NEW_WORD);
		arg0.execSQL("DROP TABLE IF EXISTS " + TB_HISTORY);
		arg0.execSQL("DROP TABLE IF EXISTS " + TB_MISTAKE);
		onCreate(arg0);
	}

}
