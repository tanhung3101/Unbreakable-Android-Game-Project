package com.me.unbreakable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class GameDatabase {
	public static final String KEY_LEVEL = "level";
	public static final String KEY_COMPLETE = "complete";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDB;
	private static final String DATABASE_NAME = "unbreakable_db.db";
	private static final String DATABASE_TABLE = "unbreakable";
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + KEY_LEVEL + " text not null, "
			+ KEY_COMPLETE + " integer);";
	private static final int DATABASE_VERSION = 2;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);

			ContentValues inititalValues = new ContentValues();
			inititalValues.put(KEY_LEVEL, "1");
			inititalValues.put(KEY_COMPLETE, true);
			db.insert(DATABASE_TABLE, null, inititalValues);
			inititalValues.put(KEY_LEVEL, "2");
			inititalValues.put(KEY_COMPLETE, false);
			db.insert(DATABASE_TABLE, null, inititalValues);
			inititalValues.put(KEY_LEVEL, "3");
			inititalValues.put(KEY_COMPLETE, false);
			db.insert(DATABASE_TABLE, null, inititalValues);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS users");
			onCreate(db);
		}
	}

	public GameDatabase open(Context mContext) {
		mDbHelper = new DatabaseHelper(mContext, DATABASE_NAME, null,
				DATABASE_VERSION);
		mDB = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long updateDB(String level, boolean isComplete) {

		// Insert data to DB
		ContentValues inititalValues = new ContentValues();
		inititalValues.put(KEY_LEVEL, level);
		inititalValues.put(KEY_COMPLETE, isComplete);
		return mDB.update(DATABASE_TABLE, inititalValues, KEY_LEVEL + " = ?",
				new String[] { level });
	}

	public Cursor getAllData() {
		return mDB.query(DATABASE_TABLE,
				new String[] { KEY_LEVEL, KEY_COMPLETE }, null, null, null,
				null, null);
	}
}
