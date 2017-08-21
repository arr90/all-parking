package com.park.allparking.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.park.allparking.sql.ParkingSQLiteOpenHelper;
import com.park.allparking.vo.Parking;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParkingDAO {

	private static final String LOG_NOTE_LIST_ADAPTER = ParkingDAO.class.getSimpleName();
	private SQLiteDatabase database;
	private String[] columns = {
            ParkingSQLiteOpenHelper.COLUMN_ID,
            ParkingSQLiteOpenHelper.COLUMN_CREATE_DATE,
            ParkingSQLiteOpenHelper.COLUMN_TITLE,
            ParkingSQLiteOpenHelper.COLUMN_DESCRIPTION,
            ParkingSQLiteOpenHelper.COLUMN_LATITUDE,
            ParkingSQLiteOpenHelper.COLUMN_LONGITUDE,
            ParkingSQLiteOpenHelper.COLUMN_CAPACITY,
            ParkingSQLiteOpenHelper.COLUMN_PRICE,
            ParkingSQLiteOpenHelper.COLUMN_START_TIME,
            ParkingSQLiteOpenHelper.COLUMN_END_TIME
	};
	private ParkingSQLiteOpenHelper sqliteOpenHelper;

	public ParkingDAO(@NonNull Context context) {
		sqliteOpenHelper = new ParkingSQLiteOpenHelper(context);
	}

	private void open() throws SQLException {
		database = sqliteOpenHelper.getWritableDatabase();
	}

	private void close() {
		sqliteOpenHelper.close();
	}

	public Parking create(Parking parking) {

        open();

		ContentValues values = new ContentValues();
        values.put(ParkingSQLiteOpenHelper.COLUMN_CREATE_DATE , new Date().getTime());
        values.put(ParkingSQLiteOpenHelper.COLUMN_TITLE	      , parking.getTitle());
        values.put(ParkingSQLiteOpenHelper.COLUMN_DESCRIPTION , parking.getDescription());
        values.put(ParkingSQLiteOpenHelper.COLUMN_LATITUDE    , parking.getLatLng().latitude);
        values.put(ParkingSQLiteOpenHelper.COLUMN_LONGITUDE   , parking.getLatLng().longitude);
        values.put(ParkingSQLiteOpenHelper.COLUMN_CAPACITY    , parking.getCapacity());
        values.put(ParkingSQLiteOpenHelper.COLUMN_PRICE       , parking.getPrice().toString());
        values.put(ParkingSQLiteOpenHelper.COLUMN_START_TIME  , parking.getStartTime().getTime());
        values.put(ParkingSQLiteOpenHelper.COLUMN_END_TIME    , parking.getEndTime().getTime());

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
        close();
		return newParking;
	}

	public void delete(Parking parking) throws Exception {
        open();

		if (parking.getId() == 0)
			throw new Exception("Parking id cannot be '0' Zero !");

		String where = ParkingSQLiteOpenHelper.COLUMN_ID + " = " + parking.getId();
		database.delete(ParkingSQLiteOpenHelper.TABLE_LOCATION, where, null);
		Log.i(LOG_NOTE_LIST_ADAPTER, "DELETE NOTE - id: "+ parking.getId() +" ["+ Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG **********");
        close();
	}

	public void edit(Parking parking) {
        open();
		ContentValues values = new ContentValues();
		String where = ParkingSQLiteOpenHelper.COLUMN_ID + " = " + parking.getId();

		values.put(ParkingSQLiteOpenHelper.COLUMN_TITLE, parking.getTitle());
		values.put(ParkingSQLiteOpenHelper.COLUMN_DESCRIPTION, parking.getCreateDate().getTime());

		database.update(ParkingSQLiteOpenHelper.TABLE_LOCATION, values, where, null);
		database.close();
        close();
	}

	public Parking getParking(Parking parking) {
        open();
		Parking parking1 = new Parking();
		Cursor cursor = database.query(ParkingSQLiteOpenHelper.TABLE_LOCATION, columns, null, null, null, null, null);
		cursor.moveToFirst();

		parking1.setId(cursor.getLong(0));
		parking1.setTitle(cursor.getString(1));
		parking1.setCreateDate(new Date(cursor.getLong(2)));

		cursor.close();
        close();
		return parking1;
	}

	public List<Parking> getAll() {
        open();
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
        close();
		return locations;
	}

	public List<Parking> getAllTest() {
        open();

		List<Parking> locations = new ArrayList<Parking>();
		Parking location;

		for (long i=1;i<=10;i++){
			location = new Parking();
			location.setId(i);
            location.setCreateDate(new Date());
            location.setDescription("DESCRIPTION TEST "+i);

            if(i==1) {
                location.setTitle("PARC DES PRINCES "+i);
                location.setLatLng(new LatLng(53.558000, 9.927000));
            }
            if(i==2) {
                location.setTitle("EMIRATES STADIUM "+i);
                location.setLatLng(new LatLng(-22.902000, -43.180000));
            }
            if(i==3) {
                location.setTitle("SIGNAL IDUNA PARK "+i);
                location.setLatLng(new LatLng(-22.8634671, -43.345479));
            }
            if(i==4) {
                location.setTitle("VICENTE CALDERON "+i);
                location.setLatLng(new LatLng(-22.906337, -43.181129));
            }
            if(i==5) {
                location.setTitle("JUVENTUS STADIUM "+i);
                location.setLatLng(new LatLng(-22.903538, -43.181600));
            }
            if(i==6) {
                location.setTitle("CAMP NOU "+i);
                location.setLatLng(new LatLng(-22.900824, -43.182013));
            }
            if(i==7) {
                location.setTitle("ILHA DO URUBU "+i);
                location.setLatLng(new LatLng(-22.901457, -43.182540));
            }
            if(i==8) {
                location.setTitle("OLD TRAFFORD "+i);
                location.setLatLng(new LatLng(-22.902657, -43.177279));
            }
            if(i==9) {
                location.setTitle("WEMBLEY STADIUM "+i);
                location.setLatLng(new LatLng(-22.902706, -43.178519));
            }
            if(i==10) {
                location.setTitle("CELTIC PARK "+i);
                location.setLatLng(new LatLng(-22.903286, -43.179802));
            }

            location.setCapacity((int) (i*6 + i));
            location.setPrice(new BigDecimal(7.10 + i*2.3));
            location.setStartTime(new Date());
            location.setEndTime(new Date(new Date().getTime() + 123456789 + i*123));

			locations.add(location);
		}
        close();
		return locations;
	}

}