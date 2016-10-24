package com.agtokty.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "yemeks";
	
    public static final String TABLE_NAME = "yemek";
    public static final String COLUMN_DATE = "gun";
    public static final String COLUMN_BIR = "bir";
    public static final String COLUMN_IKI = "iki";
    public static final String COLUMN_UC = "uc";
    public static final String COLUMN_DORT = "dort";
    public static final String COLUMN_TYPE = "type";

	public  static  final String StringOgle = "ogle";
	public  static  final String StringAksam = "aksam";

    public static final String SQL_CREATE_TABLE="create table "	+ TABLE_NAME + "( " + 
    		COLUMN_DATE + " text not null , "+
			COLUMN_BIR	+ " text not null , "+ 
			COLUMN_IKI  + " text not null , "+
			COLUMN_UC   + " text not null , "+
			COLUMN_DORT + " text not null , "+
			COLUMN_TYPE + " text not null )";
    
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	public MySQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_TABLE);
		onCreate(db);
	}

}
