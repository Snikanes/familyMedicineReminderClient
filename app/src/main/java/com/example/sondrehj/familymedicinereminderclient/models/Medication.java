package com.example.sondrehj.familymedicinereminderclient.models;

import java.io.Serializable;

/**
 * A model for representing a medication. A medication object is used for storing necessary information
 * such as name, ownerId, unit and the localId and serverId.
 */
public class Medication implements Serializable {
    int medId;
    int serverId;
    String ownerId;
    String name;
    Double count;
    String unit;

    public Medication(int medId, int serverId, String ownerId, String name, Double count, String unit) {
        this.medId = medId;
        this.serverId = serverId;
        this.ownerId = ownerId;
        this.name = name;
        this.count = count;
        this.unit = unit;
    }

    public int getMedId() {
        return medId;
    }

    public void setmedId(int medId) {
        this.medId = medId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        if(count < 0){
            this.count = 0.0;
        } else {
            this.count = count;
        }

    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }


    public String toString() {
        return "Med id: " + this.medId + "\n" +
                "ServerID: " + this.serverId + "\n" +
                " Name: " + this.name + "\n" +
                " OwnerId: " + this.ownerId + "\n" +
                " Count: " + this.count + "\n" +
                " Unit: " + this.unit;
    }
}
