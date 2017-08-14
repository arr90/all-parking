package com.park.allparking.vo;

import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by arr90 on 12/08/2017.
 */
public class Parking {

    private long id;
    private Date createDate;
    private String title;
    private String description;
    private LatLng latLng;
    private Integer capacity;
    private BigDecimal price;
    private Date startTime;
    private Date endTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
