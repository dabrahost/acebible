/* 
 * Copyright (C) 2009 Guan YC yc.guanATgmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gyc.ace.bible.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.gyc.ace.bible.R;

public class DataHelper extends SQLiteOpenHelper {

	private static final String TABLE_NAME = "BibleMarks";
	private Context mContext;

	/** Constructor */
	public DataHelper(Context context) {
		super(context, "BibleMarks", null, 1);
		this.mContext = context;
	}

	public DataHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.mContext = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// String[] sql = mContext.getString(R.string.db_create).split("\n");

		String[] sql = { "CREATE TABLE BibleMarks ( id INTEGER PRIMARY KEY AUTOINCREMENT, volume TEXT, chapter INTEGER, subchapter INTEGER, tags	TEXT, createdate INTEGER);" };

		db.beginTransaction();
		try {
			for (String s : sql) {
				if (s.trim().length() > 0) {
					db.execSQL(s);
				}
			}
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e("Error creating tables and debug data", e.toString());
			throw e;
		} finally {
			db.endTransaction();
		}
	}

	// FIXME
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// FIXME 提示用户备份

		Log.i("db onupdate", "Upgrading database from version " + oldVersion
				+ " to " + newVersion + ", which will destroy all old data");

		// String[] sql = mContext.getString(R.string.db_update).split("\n");
		String[] sql = { "DROP TABLE IF EXISTS BibleMarks" };

		db.beginTransaction();

		try {
			for (String s : sql) {
				if (s.trim().length() > 0) {
					db.execSQL(s);
				}
			}
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e("Error upgrading tables and debug data", e.toString());
			throw e;
		} finally {
			db.endTransaction();
		}

		// This is cheating. In the real world, you'll need to add columns, not
		// rebuild from scratch.
		onCreate(db);

	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		// TODO Auto-generated method stub
		return super.getReadableDatabase();
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		return super.getWritableDatabase();
	}

	public List<Mark> getMarkList4Example(Mark markex) {
		List<Mark> result = new ArrayList<Mark>();
		MarkCursor cursor = this.getMarkQueryCursor(markex);

		for (int rowNum = 0; rowNum < cursor.getCount(); rowNum++) {
			cursor.moveToPosition(rowNum);
			Mark mark = copyMarkFromCursor(cursor);
			result.add(mark);
		}

		try {
			cursor.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return result;
	}

	private MarkCursor getMarkQueryCursor(Mark mark) {

		final String where = " WHERE  0=0  ";

		String volume = mark.getVolume();
		int ch = mark.getChapter();
		int subch = mark.getSubchapter();

		StringBuilder sb = new StringBuilder();
		sb.append(where);
		sb.append(" and volume = '" + volume + "'");
		sb.append(" and chapter = " + ch + "");
		sb.append(" and subchapter = " + subch + "");

		String sql = "SELECT id, volume, chapter, subChapter, tags, createdate  "
				+ " FROM BibleMarks  " + where + " ORDER BY volume  ";

		SQLiteDatabase d = getReadableDatabase();

		MarkCursor c = (MarkCursor) d.rawQueryWithFactory(
				new MarkCursor.Factory(), sql, null, null);
		c.moveToFirst();
		return c;
	}

	private MarkCursor getMarkCursor() {
		String sql = "SELECT id, volume, chapter, subChapter, tags, createdate  "
				+ " FROM BibleMarks  "
				+ " WHERE  0=0  "
				+ " ORDER BY volume, chapter ";

		SQLiteDatabase d = getReadableDatabase();

		MarkCursor c = (MarkCursor) d.rawQueryWithFactory(
				new MarkCursor.Factory(), sql, null, null);
		c.moveToFirst();
		return c;
	}

	public List<Mark> getAllMarks() {
		List<Mark> result = new ArrayList<Mark>();
		MarkCursor cursor = this.getMarkCursor();

		for (int rowNum = 0; rowNum < cursor.getCount(); rowNum++) {
			cursor.moveToPosition(rowNum);
			Mark mark = copyMarkFromCursor(cursor);
			result.add(mark);
		}

		try {
			cursor.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return result;
	}

	private Mark copyMarkFromCursor(MarkCursor cursor) {
		Mark mark = new Mark();
		mark.setId(cursor.getMarkid());
		mark.setVolume(cursor.getMarkVolume());
		mark.setChapter(cursor.getMarkChapter());
		mark.setSubchapter(cursor.getMarkSubChapter());
		mark.setTags(cursor.getMarkTags());
		mark.setCreatedate(cursor.getMarkCreateDate());

		return mark;
	}

	public void insertTask(Mark mark) {
		long id = this.insertTask(mark.getVolume(), mark.getChapter(), mark
				.getSubchapter(), mark.getTags(), mark.getCreatedate());
		mark.setId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#close()
	 */
	@Override
	public synchronized void close() {
		super.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onOpen(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	public long insertTask(String volume, long chapter, long subchapter,
			String tags, long createdate) {
		ContentValues map = new ContentValues();

		map.put("volume", volume);
		map.put("chapter", chapter);
		map.put("subchapter", subchapter);
		map.put("tags", tags);
		map.put("createdate", createdate);

		try {
			return getWritableDatabase().insert(DataHelper.TABLE_NAME, null,
					map);
		} catch (SQLException e) {
			Log.e("Error writing new job", e.toString());
		}

		return -1;
	}

	/**
	 * Update a job in the database.
	 * 
	 * @param job_id
	 *            The job id of the existing job
	 * @param employer_id
	 *            The employer offering the job
	 * @param title
	 *            The job title
	 * @param description
	 *            The job description
	 */
	public void updateMark(Mark mark) {
		ContentValues map = new ContentValues();

		map.put("id", mark.getId());
		map.put("volume", mark.getVolume());
		map.put("chapter", mark.getChapter());
		map.put("subchapter", mark.getSubchapter());
		map.put("tags", mark.getTags());
		map.put("createdate", mark.getCreatedate());

		String[] whereArgs = new String[] { Long.toString(mark.getId()) };

		try {
			getWritableDatabase().update(DataHelper.TABLE_NAME, map, "id=?",
					whereArgs);
		} catch (SQLException e) {
			Log.e("Error writing new job", e.toString());
		}
	}

	/**
	 * Delete a job from the database.
	 * 
	 * @param task_id
	 *            The job id of the job to delete
	 */
	public void delete(long task_id) {
		String[] whereArgs = new String[] { Long.toString(task_id) };
		try {
			getWritableDatabase().delete(DataHelper.TABLE_NAME, "id=?",
					whereArgs);
		} catch (SQLException e) {
			Log.e("Error deleteing job", e.toString());
		}
	}

	/**
	 * Delete a job from the database.
	 * 
	 * @param task_id
	 *            The job id of the job to delete
	 */
	public void delete4SQL(long task_id) {
		String sql = String.format("DELETE FROM " + DataHelper.TABLE_NAME
				+ " WHERE id = '%d' ", task_id);
		try {
			getWritableDatabase().execSQL(sql);
		} catch (SQLException e) {
			Log.e("Error deleteing job", e.toString());
		}
	}

	public static class MarkCursor extends SQLiteCursor {

		public MarkCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
				String editTable, SQLiteQuery query) {
			super(db, driver, editTable, query);
		}

		static final String QUERY = "SELECT id, volume, chapter, subChapter, tags, createdate  "
				+ " FROM BibleMarks  "
				// + " WHERE status =  "
				+ " ORDER BY ";

		static class Factory implements SQLiteDatabase.CursorFactory {
			@Override
			public Cursor newCursor(SQLiteDatabase db,
					SQLiteCursorDriver driver, String editTable,
					SQLiteQuery query) {
				return new MarkCursor(db, driver, editTable, query);
			}
		}

		public long getMarkid() {
			return getLong(getColumnIndexOrThrow("id"));
		}

		public String getMarkVolume() {
			return getString(getColumnIndexOrThrow("volume"));
		}

		public int getMarkChapter() {
			return getInt(getColumnIndexOrThrow("chapter"));
		}

		public int getMarkSubChapter() {
			return getInt(getColumnIndexOrThrow("subchapter"));
		}

		public String getMarkTags() {
			return getString(getColumnIndexOrThrow("tags"));
		}

		public long getMarkCreateDate() {
			return getLong(getColumnIndexOrThrow("createdate"));
		}

	}

}
