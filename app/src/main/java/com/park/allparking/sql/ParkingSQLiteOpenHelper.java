package com.park.allparking.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ParkingSQLiteOpenHelper extends SQLiteOpenHelper {

	public static final String TABLE_LOCATION = "parking";

	public static final String COLUMN_ID 		  = "_id";
	public static final String COLUMN_TITLE 	  = "title";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_LATITUDE 	  = "latitude";
	public static final String COLUMN_LONGITUDE   = "longitude";
	public static final String COLUMN_CREATE_DATE = "create_date";

	private static final String DATABASE_NAME	  = "all_parking.db";
	private static final int 	DATABASE_VERSION  = 1;

	private static final String DATABASE_CREATE = "create table " + TABLE_LOCATION
			+ "("
			+ COLUMN_ID 	 	 + " integer primary key autoincrement , "
			+ COLUMN_TITLE 		 + " text not null , "
			+ COLUMN_DESCRIPTION + " text not null , "
			+ COLUMN_LATITUDE 	 + " double not null , "
			+ COLUMN_LONGITUDE	 + " double not null , "
			+ COLUMN_CREATE_DATE + " date not null "
			+ ");";

	public ParkingSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
		onCreate(db);
	}

}