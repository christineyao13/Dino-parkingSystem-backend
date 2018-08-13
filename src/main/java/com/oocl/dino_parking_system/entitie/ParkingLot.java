package com.oocl.dino_parking_system.entitie;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import static com.oocl.dino_parking_system.constant.Constants.STATUS_NORMAL;

@Table(name = "parking_lot")
@Entity
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int size;
    private int carNum = 0;
    private boolean status = STATUS_NORMAL;// 停车场开放状态：默认开放NORMAL

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "parking_boy_id")
    @JsonIgnore
    private User parkingBoy;

	public ParkingLot() {
	}

	public ParkingLot(String name, int size) {
		this.name = name;
		this.size = size;
	}
  
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public User getParkingBoy() {
        return parkingBoy;
    }

    public void setParkingBoy(User parkingBoy) {
        this.parkingBoy = parkingBoy;
    }

	public int getCarNum() {
		return carNum;
	}

	public void setCarNum(int carNum) {
		this.carNum = carNum;
	}
}
