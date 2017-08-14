package com.park.allparking.business;

import com.park.allparking.dao.ParkingDAO;
import com.park.allparking.vo.Parking;

import java.util.List;

/**
 * Created by areis on 14/08/2017.
 */

public class ParkingBusiness {


    private static ParkingBusiness instance;

    public static ParkingBusiness getInstance(){
        if(instance == null){
            instance = new ParkingBusiness();
        }
        return instance;
    }


    public List<Parking> findParkingByTitle(String text){
        List<Parking> parkings = ParkingDAO.getInstance().getAllTest();
        return parkings;
    }

    public List<Parking> getAllTest(String text){
        List<Parking> parkings = ParkingDAO.getInstance().getAllTest();
        return parkings;
    }
    public List<Parking> getAllTest(){
        List<Parking> parkings = ParkingDAO.getInstance().getAllTest();
        return parkings;
    }


}
