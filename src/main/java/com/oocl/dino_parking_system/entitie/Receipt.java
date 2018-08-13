package com.oocl.dino_parking_system.entitie;

import java.util.UUID;

import static com.oocl.dino_parking_system.constant.Constants.STATUS_INUSE;

public class Receipt {
    private String id;
    private String status;

    public Receipt(){
	    this.id = UUID.randomUUID().toString();
	    this.status = STATUS_INUSE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
