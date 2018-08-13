package com.oocl.dino_parking_system.service;

import com.alibaba.fastjson.JSONObject;
import com.oocl.dino_parking_system.dto.OrderDTO;
import com.oocl.dino_parking_system.dto.ParkingLotTinyDTO;
import com.oocl.dino_parking_system.entitie.ParkingLot;
import com.oocl.dino_parking_system.entitie.User;
import com.oocl.dino_parking_system.repository.OrderRepository;
import com.oocl.dino_parking_system.repository.ParkingLotsRepository;
import com.oocl.dino_parking_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.oocl.dino_parking_system.constant.Constants.*;

/**
 * Created by Vito Zhuang on 8/2/2018.
 */
@Service
public class ParkingBoyService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	ParkingLotsRepository parkingLotsRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderService orderService;


	public List<ParkingLotTinyDTO> findAllNotFullParkingLots(Long id) {
		try {
			User parkingBoy = userRepository.findById(id).orElse(null);
			return parkingBoy.getParkingLots().stream()
					.filter(parkingLot -> parkingLot.getSize() > parkingLot.getCarNum()
							&& parkingLot.isStatus() == (STATUS_NORMAL))
					.map(ParkingLotTinyDTO::new)
					.collect(Collectors.toList());
		} catch (Exception e) {
			return null;
		}
	}


	public User findParkingBoyById(Long parkingBoyId) {
		Optional<User> optional = userRepository.findById(parkingBoyId);
		return optional.orElse(null);
	}

	public boolean parCar(Long parkingBoyId, Long parkingLotId, Long orderId) {
		try {
			User parkingBoy = userRepository.findById(parkingBoyId).orElse(null);
			if (parkingBoyHasThisParkingLot(parkingBoy, parkingLotId)) {
				ParkingLot parkingLot = parkingLotsRepository.findById(parkingLotId).orElse(null);
				parkingLot.setCarNum(parkingLot.getCarNum() + 1);
				parkingLotsRepository.save(parkingLot);
				return orderService.changeOrderStatus(orderId, parkingBoy, STATUS_PARKED, parkingLot.getName(), false);
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private boolean parkingBoyHasThisParkingLot(User parkingBoy, Long parkingLotId) {
		try {
			for (ParkingLot parkingLot : parkingBoy.getParkingLots()) {
				if (parkingLot.getId() == parkingLotId)
					return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public List<User> findAllParkingBoys() {
		List<User> parkingBoys = userRepository.findAll().stream().
				filter(parkingBoy -> parkingBoy.getRoles().stream().anyMatch(role -> role.getName().equals(ROLE_PARKINGBOY))).
				collect(Collectors.toList());
		return parkingBoys;
	}

	public List<OrderDTO> findAllFinishOrderByParkingBoyId(Long parkingBoyId) {
		User parkingBoy = userRepository.findById(parkingBoyId).orElse(null);
		if (parkingBoy != null) {
			return parkingBoy.getLotOrders().stream()
					.filter(lotOrder -> lotOrder.getStatus().equals(STATUS_FINISH) || lotOrder.getStatus().equals(STATUS_PARKED))
					.map(OrderDTO::new)
					.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	public List<ParkingLot> findAllManagedParkingLots(Long id) {
		User parkingBoy = userRepository.findById(id).orElse(null);
		return parkingBoy.getParkingLots();

	}

	public boolean arrangeParkingLots(Long id, String operation, List<Long> parkingLotIds) {
		try {
			User parkingBoy = userRepository.findById(id).orElse(null);
			List<ParkingLot> newParkingLotList = parkingBoy.getParkingLots();
			for (Long parkingLotId : parkingLotIds) {
				ParkingLot parkingLot = parkingLotsRepository.findById(parkingLotId).orElse(null);
				if (operation.equals("add")) {// 增加停车场
					if (parkingLot != null
							&& parkingLot.getCarNum() == 0 // 未停车
							&& parkingLot.getParkingBoy() == null) {// 未被管理
						newParkingLotList.add(parkingLot);
						parkingLot.setParkingBoy(parkingBoy);
					} else {
						return false;
					}
				} else { // 移除停车场
					for (int i = 0; i <= newParkingLotList.size(); i++) {
						ParkingLot repealParkingLot = newParkingLotList.get(i);
						if (repealParkingLot.getId() == parkingLotId && repealParkingLot.getCarNum() == 0) {
							repealParkingLot.setParkingBoy(null);
							parkingLotsRepository.save(repealParkingLot);
							newParkingLotList.remove(i);
							break;
						}
					}
				}
			}
			parkingLotsRepository.saveAll(newParkingLotList);
			parkingBoy.setParkingLots(newParkingLotList);
			userRepository.save(parkingBoy);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public JSONObject findUnReadOrderNum(String parkingBoyName) {
		try {
			JSONObject json = new JSONObject();
			User parkingBoy = userRepository.findByUsername(parkingBoyName);
			json.put("parkingBoyId",parkingBoy.getId());
			json.put("type","unRead");
			if(parkingBoy.getLotOrders().size()==0){
				json.put("unReadNum",0);
			}else {
				json.put("unReadNum", parkingBoy.getLotOrders().stream()
						.filter(lotOrder -> !lotOrder.getRead())
						.count());
			}
			return json;
		} catch (Exception e) {
			return null;
		}
	}
}
