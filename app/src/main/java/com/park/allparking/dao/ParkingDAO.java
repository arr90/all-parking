package com.park.allparking.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.park.allparking.sql.ParkingSQLiteOpenHelper;
import com.park.allparking.vo.Parking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParkingDAO {

	private static final String LOG_NOTE_LIST_ADAPTER = ParkingDAO.class.getSimpleName();
	private SQLiteDatabase database;
	private String[] columns = { ParkingSQLiteOpenHelper.COLUMN_ID, ParkingSQLiteOpenHelper.COLUMN_TITLE, ParkingSQLiteOpenHelper.COLUMN_DESCRIPTION};
	private ParkingSQLiteOpenHelper sqliteOpenHelper;

	private static ParkingDAO instance;

	private ParkingDAO() {}

	public static ParkingDAO getInstance(){
		if(instance == null){
			instance = new ParkingDAO();
		}
		return instance;
	}

	public ParkingDAO(Context context) {
		sqliteOpenHelper = new ParkingSQLiteOpenHelper(context);
	}

	public void open() throws SQLException {
		database = sqliteOpenHelper.getWritableDatabase();
	}

	public void close() {
		sqliteOpenHelper.close();
	}

	public Parking create(Parking parking) {
		ContentValues values = new ContentValues();
		values.put(ParkingSQLiteOpenHelper.COLUMN_TITLE	   , parking.getTitle());
		values.put(ParkingSQLiteOpenHelper.COLUMN_DESCRIPTION , parking.getDescription());
		values.put(ParkingSQLiteOpenHelper.COLUMN_LATITUDE    , parking.getLatLng().latitude);
		values.put(ParkingSQLiteOpenHelper.COLUMN_LONGITUDE   , parking.getLatLng().longitude);
		values.put(ParkingSQLiteOpenHelper.COLUMN_CREATE_DATE , new Date().getTime());

		long insertId = database.insert(ParkingSQLiteOpenHelper.TABLE_LOCATION, null, values);
		Cursor cursor = database.query(ParkingSQLiteOpenHelper.TABLE_LOCATION, columns, ParkingSQLiteOpenHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();

		Parking newParking = new Parking();
		newParking.setId(cursor.getLong(0));
		newParking.setTitle(cursor.getString(1));
		newParking.setDescription(cursor.getString(2));
		newParking.setLatLng(new LatLng(cursor.getDouble(3), cursor.getDouble(4)));
		newParking.setCreateDate(new Date(cursor.getLong(5)));

		cursor.close();
		return newParking;
	}

	public void delete(Parking parking) {
		String where = ParkingSQLiteOpenHelper.COLUMN_ID + " = " + parking.getId();
		database.delete(ParkingSQLiteOpenHelper.TABLE_LOCATION, where, null);
		Log.i(LOG_NOTE_LIST_ADAPTER, "DELETE NOTE - id: "+ parking.getId() +" ["+ Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG **********");
	}

	public void edit(Parking parking) {
		ContentValues values = new ContentValues();
		String where = ParkingSQLiteOpenHelper.COLUMN_ID + " = " + parking.getId();

		values.put(ParkingSQLiteOpenHelper.COLUMN_TITLE, parking.getTitle());
		values.put(ParkingSQLiteOpenHelper.COLUMN_DESCRIPTION, parking.getCreateDate().getTime());

		database.update(ParkingSQLiteOpenHelper.TABLE_LOCATION, values, where, null);
		database.close();
	}

	public Parking getNote(Parking parking) {
		Parking location = new Parking();
		Cursor cursor = database.query(ParkingSQLiteOpenHelper.TABLE_LOCATION, columns, null, null, null, null, null);
		cursor.moveToFirst();

		location.setId(cursor.getLong(0));
		location.setTitle(cursor.getString(1));
		location.setCreateDate(new Date(cursor.getLong(2)));

		cursor.close();
		return location;
	}

	public List<Parking> getAll() {
		List<Parking> locations = new ArrayList<Parking>();
		Cursor cursor = database.query(ParkingSQLiteOpenHelper.TABLE_LOCATION, columns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Parking parking = new Parking();
			parking.setId(cursor.getLong(0));
			parking.setTitle(cursor.getString(1));
			parking.setCreateDate(new Date(cursor.getLong(2)));
			locations.add(parking);
			cursor.moveToNext();
		}
		cursor.close();
		return locations;
	}

	public List<Parking> getAllTest() {
		List<Parking> locations = new ArrayList<Parking>();
		Parking location;

		for (int i=1;i<10;i++){
			location = new Parking();
			location.setId(i);
			location.setTitle("TEST"+i);
			location.setDescription("DESCRIPTION TEST "+i);

			if(i==1)
			location.setLatLng(new LatLng(53.558, 9.927));
			if(i==2)
			location.setLatLng(new LatLng(-22.902, -43.180));
			if(i==3)
			location.setLatLng(new LatLng(-22.8634671, -43.345479));
			if(i==4)
			location.setLatLng(new LatLng(-22.906337, -43.181129));
			if(i==5)
			location.setLatLng(new LatLng(-22.903538, -43.181600));
			if(i==6)
			location.setLatLng(new LatLng(-22.900824, -43.182013));
			if(i==7)
			location.setLatLng(new LatLng(-22.901457, -43.182540));
			if(i==8)
			location.setLatLng(new LatLng(-22.902657, -43.177279));
			if(i==9)
			location.setLatLng(new LatLng(-22.902706, -43.178519));
			if(i==10)
			location.setLatLng(new LatLng(-22.903286, -43.179802));

			location.setCreateDate(new Date());
			locations.add(location);
		}

		return locations;
	}
}