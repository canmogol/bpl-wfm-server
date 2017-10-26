package com.bplsoft.wfm.model;

import com.bplsoft.common.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "LOCATION_MODEL")
public class LocationModel extends BaseModel<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PM_SEQ")
    @SequenceGenerator(name = "PM_SEQ", sequenceName = "PM_SEQ", allocationSize = 1)
    @Column(name = "PM_ID", updatable = false, nullable = false)
    private Integer id;

    private String name;
    private String address;
    private String owner;
    private double lat;
    private double lon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
