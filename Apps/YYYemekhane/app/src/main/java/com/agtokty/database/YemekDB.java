package com.agtokty.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class YemekDB {
	private MySQLiteOpenHelper helper;
	private SQLiteDatabase database;
	private String columns[] = { MySQLiteOpenHelper.COLUMN_DATE,MySQLiteOpenHelper.COLUMN_BIR,MySQLiteOpenHelper.COLUMN_IKI,
			MySQLiteOpenHelper.COLUMN_UC,MySQLiteOpenHelper.COLUMN_DORT,MySQLiteOpenHelper.COLUMN_TYPE};

	public YemekDB(Context context){
		helper = new MySQLiteOpenHelper(context);
	}
	
	public void open()throws SQLException{
		database=helper.getWritableDatabase();
	}

	
	public void close(){
		helper.close();
	}
	
	public void recreate(){
		helper.onUpgrade(database, 1, 1);
	}
	
	public long add(String gun,String bir,String iki,String uc,String dort,String type){
		ContentValues values = new ContentValues();
		values.put(MySQLiteOpenHelper.COLUMN_DATE, gun);
		values.put(MySQLiteOpenHelper.COLUMN_BIR, bir);
		values.put(MySQLiteOpenHelper.COLUMN_IKI, iki);
		values.put(MySQLiteOpenHelper.COLUMN_UC, uc);
		values.put(MySQLiteOpenHelper.COLUMN_DORT, dort);
		values.put(MySQLiteOpenHelper.COLUMN_TYPE, type);
		
		long  newRowId=database.insert(MySQLiteOpenHelper.TABLE_NAME, null, values);
		return newRowId;
	}
	
	public Cursor getList(){
		Cursor c = database.query(MySQLiteOpenHelper.TABLE_NAME,columns, null, null, null, null, null);
		return c;
	}



}
