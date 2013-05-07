package org.uestc.translator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "dictionary";
	public static final String TB_NEW_WORD = "new_words";
	public static final String TB_HISTORY = "history";
	public static final String COL_NW_WORD_ID = "word_id";
	public static final String COL_NW_WORD = "word";
	public static final String COL_HIS_WORD_ID = "word_id";
	public static final String COL_HIS_WORD = "word";

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
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		arg0.execSQL("DROP TABLE IF EXISTS " + TB_NEW_WORD);
		arg0.execSQL("DROP TABLE IF EXISTS " + TB_HISTORY);
		onCreate(arg0);
	}

}
