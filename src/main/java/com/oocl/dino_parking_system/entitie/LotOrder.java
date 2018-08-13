package com.oocl.dino_parking_system.entitie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;

import java.time.ZonedDateTime;

import static com.oocl.dino_parking_system.constant.Constants.STATUS_NOROB;
import static com.oocl.dino_parking_system.constant.Constants.TYPE_PARKCAR;

@Table(name = "car_order")
@Entity
public class LotOrder implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String type;
    @CreatedDate
    private ZonedDateTime parkDate = ZonedDateTime.now();
    private ZonedDateTime unParkDate;
    @ManyToOne
    @JoinColumn(name = "parking_boy_id")
    @JsonIgnore
    private User parkingBoy;
    private String parkingLotName;
    private String plateNumber;
    private String status;
    private String receiptId;
    private boolean read = false;

    public LotOrder(String type, String plateNumber, String status, String receiptId) {
        this.type = type;
        this.plateNumber = plateNumber;
        this.status = status;
        this.receiptId = receiptId;
    }

    public LotOrder(String plateNumber, String receiptId) {
        this.plateNumber = plateNumber;
        this.receiptId = receiptId;
        this.status = STATUS_NOROB;
        this.type = TYPE_PARKCAR;
    }

    public LotOrder(){}

	@Override
	public Object clone() {
		LotOrder order = null;
		try{
			order = (LotOrder) super.clone();
		}catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return order;
	}


	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getParkingBoy() {
        return parkingBoy;
    }

    public void setParkingBoy(User parkingBoy) {
        this.parkingBoy = parkingBoy;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

	public ZonedDateTime getParkDate() {
		return parkDate;
	}

	public void setParkDate(ZonedDateTime parkDate) {
		this.parkDate = parkDate;
	}

	public ZonedDateTime getUnParkDate() {
		return unParkDate;
	}

	public void setUnParkDate(ZonedDateTime unParkDate) {
		this.unParkDate = unParkDate;
	}

	public String getParkingLotName() {
		return parkingLotName;
	}

	public void setParkingLotName(String parkingLotName) {
		this.parkingLotName = parkingLotName;
	}

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LotOrder{");
        sb.append("id=").append(id);
        sb.append(", type='").append(type).append('\'');
        sb.append(", parkDate=").append(parkDate);
        sb.append(", unParkDate=").append(unParkDate);
        sb.append(", parkingBoy=").append(parkingBoy);
        sb.append(", parkingLotName='").append(parkingLotName).append('\'');
        sb.append(", plateNumber='").append(plateNumber).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", receiptId='").append(receiptId).append('\'');
        sb.append('}');
        return sb.toString();
    }


	public boolean getRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
}
