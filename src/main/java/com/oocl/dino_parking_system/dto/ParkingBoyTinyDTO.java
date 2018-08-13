package com.oocl.dino_parking_system.dto;

import com.oocl.dino_parking_system.entitie.User;

public class ParkingBoyTinyDTO {
    private Long id;
    private String nickname;
    private String username;
    private String phone;
    private String workStatus;
    private boolean status;

    public ParkingBoyTinyDTO(User parkingBoy) {
        this.id = parkingBoy.getId();
        this.nickname = parkingBoy.getNickname();
        this.username = parkingBoy.getUsername();
        this.phone = parkingBoy.getPhone();
        this.workStatus = parkingBoy.getWorkStatus();
        this.status = parkingBoy.getStatus();
    }

    public ParkingBoyTinyDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

	public String getNickname() {
		return nickname;
	}

	public String getPhone() {
		return phone;
	}

	public String getWorkStatus() {
		return workStatus;
	}

	public boolean getStatus() {
		return status;
	}
}
