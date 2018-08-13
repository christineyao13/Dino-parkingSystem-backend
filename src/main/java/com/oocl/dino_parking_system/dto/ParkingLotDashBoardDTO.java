package com.oocl.dino_parking_system.dto;

import com.oocl.dino_parking_system.entitie.ParkingLot;

/**
 * Created by Vito Zhuang on 8/1/2018.
 */
public class ParkingLotDashBoardDTO {
	private Long parkingLotId;
	private String parkingLotName;
	private int size;
	private int carNum;
	private boolean parkingLotStatus;
	private Long parkingBoyId;
	private String parkingBoyName;


	public ParkingLotDashBoardDTO(ParkingLot parkingLot){
		this.parkingLotId = parkingLot.getId();
		this.parkingLotName = parkingLot.getName();
		this.size = parkingLot.getSize();
		this.carNum = parkingLot.getCarNum();
		this.parkingLotStatus = parkingLot.isStatus();
		this.parkingBoyId = parkingLot.getParkingBoy()!=null?parkingLot.getParkingBoy().getId():0;
		this.parkingBoyName = parkingLot.getParkingBoy()!=null?parkingLot.getParkingBoy().getNickname():null;
	}

	public Long getParkingLotId() {
		return parkingLotId;
	}

	public String getParkingLotName() {
		return parkingLotName;
	}

	public int getSize() {
		return size;
	}

	public int getCarNum() {
		return carNum;
	}

	public Long getParkingBoyId() {
		return parkingBoyId;
	}

	public String getParkingBoyName() {
		return parkingBoyName;
	}

	public boolean isParkingLotStatus() {
		return parkingLotStatus;
	}
}
