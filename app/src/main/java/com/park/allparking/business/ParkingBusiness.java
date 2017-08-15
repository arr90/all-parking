package com.park.allparking.business;

import android.content.Context;
import android.support.annotation.NonNull;

import com.park.allparking.dao.ParkingDAO;
import com.park.allparking.vo.Parking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by areis on 14/08/2017.
 */

public class ParkingBusiness {

    private static ParkingBusiness parkingBusinessInstance;
    private static ParkingDAO parkingDAOinstance;
    private static Context context;

    private static ParkingDAO getParkingDAOinstance(@NonNull Context context){
        if(parkingDAOinstance == null){
            parkingDAOinstance = new ParkingDAO(context);
        }
        return parkingDAOinstance;
    }

    /**
     * <b>For business without DATABASE</b>
     * **/
    public static ParkingBusiness getInstance(){
        if(parkingBusinessInstance == null){
            parkingBusinessInstance = new ParkingBusiness();
        }
        return parkingBusinessInstance;
    }

    /**
     * <b>Context must be passed for access DATABASE</b>
     * */
    public static ParkingBusiness getInstance(@NonNull Context newContext){
        context = newContext;
        if(parkingBusinessInstance == null){
            parkingBusinessInstance = new ParkingBusiness();
        }
        return parkingBusinessInstance;
    }

    public List<Parking> findParkingByTitle(@NonNull String text) {

        List<Parking> parkings    = getParkingDAOinstance(context).getAllTest();
        List<Parking> newParkings = new ArrayList<Parking>();

        for (Parking p : parkings ) {
            if (p.getTitle().toUpperCase().contains(text.toUpperCase()))
                newParkings.add(p);
        }
        return newParkings;
    }

    public Parking getParkingByID(String text) {
        List<Parking> parkings = getParkingDAOinstance(context).getAllTest();
        Parking parking = new Parking();
        for (Parking p : parkings ) {
            if (p.getTitle().equalsIgnoreCase(text))
                return p;
        }
        return parking;
    }

    public List<Parking> getAllParkings()  {
        List<Parking> parkings = getParkingDAOinstance(context).getAllTest();
        return parkings;
    }

}