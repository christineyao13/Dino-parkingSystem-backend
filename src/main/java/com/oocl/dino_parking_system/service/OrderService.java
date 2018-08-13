package com.oocl.dino_parking_system.service;

import com.alibaba.fastjson.JSONObject;
import com.oocl.dino_parking_system.config.WebSocketServer;
import com.oocl.dino_parking_system.dto.OrderDTO;
import com.oocl.dino_parking_system.entitie.LotOrder;
import com.oocl.dino_parking_system.entitie.ParkingLot;
import com.oocl.dino_parking_system.entitie.User;
import com.oocl.dino_parking_system.repository.OrderRepository;
import com.oocl.dino_parking_system.repository.ParkingLotsRepository;
import com.oocl.dino_parking_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.oocl.dino_parking_system.constant.Constants.*;

@Component
public class OrderService {


	@Autowired
	private ParkingBoyService parkingBoyService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ParkingLotsRepository parkingLotsRepository;


	public LotOrder generateOrder(String plateNumber, String receiptId) {
		LotOrder lotOrder = new LotOrder(TYPE_PARKCAR, plateNumber, STATUS_NOROB, receiptId);
		return orderRepository.save(lotOrder);
	}

	public boolean generateOrder(LotOrder lotOrder) {
		lotOrder.setStatus(STATUS_NOROB);
		lotOrder.setType(TYPE_PARKCAR);
		orderRepository.save(lotOrder);
		return true;
	}

	public List<LotOrder> getAllOrders() {
		return orderRepository.findAll();
	}


	public List<LotOrder> getOrdersByStatus(String status) {
		return orderRepository.findByStatus(status);
	}

	public boolean changeOrderStatus(Long orderId, User parkingBoy, String status, String parkingLotName, boolean appoint) {
		try {
			LotOrder order = orderRepository.findById(orderId).orElse(null);
			switch (status) {
				case STATUS_WAITPARK:
					if (order.getStatus().equals(STATUS_NOROB)
							&& checkBoyHaveEnoughParkingSpace(parkingBoy)
							&& parkingBoy.getWorkStatus().equals(STATUS_ONDUTY)) {
						order.setStatus(STATUS_WAITPARK);// 等待停车
						order.setType(TYPE_PARKCAR);// 存车订单
						order.setParkingBoy(parkingBoy);
						if(!appoint) {
							order.setRead(true);
						}
						parkingBoy.addOrder(order);
						orderRepository.save(order);
						userRepository.save(parkingBoy);
						return true;
					} else {
						return false;
					}
				case STATUS_PARKED:
					if (checkBoyPermisson(parkingBoy, order)
							&& order.getStatus().equals(STATUS_WAITPARK)) {
						order.setStatus(STATUS_PARKED);// 停车成功
						order.setParkingLotName(parkingLotName);
						order.setRead(true);
						orderRepository.save(order);
						LotOrder waitUnparkOrder = new LotOrder();
						Long id = waitUnparkOrder.getId();
						waitUnparkOrder = (LotOrder) order.clone();
						waitUnparkOrder.setRead(false);
						waitUnparkOrder.setId(id);
						waitUnparkOrder.setStatus(STATUS_WAITCUSTOMER);
						waitUnparkOrder.setType(TYPE_PARKOUTCAR);
						orderRepository.save(waitUnparkOrder);
						parkingBoy.addOrder(waitUnparkOrder);
						userRepository.save(parkingBoy);
						return true;
					} else {
						return false;
					}
				case STATUS_WAITUNPARK:
					if (order.getStatus().equals(STATUS_WAITCUSTOMER)) {
						order.setStatus(STATUS_WAITUNPARK);// 等待取车
						order.setType(TYPE_PARKOUTCAR); // 取车订单
						orderRepository.save(order);
						//websocket推送给小弟
						JSONObject message = new JSONObject();
						message.put("id", order.getId());
						message.put("type","newOrder");
						message.put("message", "你有一个新的取车订单!");
						WebSocketServer.sendInfo(message.toJSONString(), order.getParkingBoy().getId().toString());
						return true;
					} else {
						return false;
					}
				case STATUS_FINISH:
					if (checkBoyPermisson(parkingBoy, order)
							&& order.getStatus().equals(STATUS_WAITUNPARK)) {
						order.setStatus(STATUS_FINISH);// 取车完成
						order.setUnParkDate(ZonedDateTime.now());
						ParkingLot parkingLot = parkingLotsRepository.findByName(order.getParkingLotName()).get(0);
						parkingLot.setCarNum(parkingLot.getCarNum()-1);
						parkingLotsRepository.save(parkingLot);
						orderRepository.save(order);
						return true;
					} else {
						return false;
					}
				default:
					return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean checkBoyHaveEnoughParkingSpace(User parkingBoy) {
		int parkingSpace = 0;
		for (ParkingLot parkingLot : parkingBoy.getParkingLots()) {
			parkingSpace += parkingLot.getSize() - parkingLot.getCarNum();
		}
		return parkingSpace > parkingBoy.getLotOrders().size();
	}

	private boolean checkBoyPermisson(User parkingBoy, LotOrder order) {
		if (order != null && order.getParkingBoy() != null)
			return order.getParkingBoy().getId().equals(parkingBoy.getId());
		else
			return false;
	}

	public List<OrderDTO> findOrderByParkingBoyId(Long parkingBoyId) {
		User parkingBoy = parkingBoyService.findParkingBoyById(parkingBoyId);
		if (parkingBoy != null) {
			return parkingBoy.getLotOrders().stream()
					.map(OrderDTO::new)
					.collect(Collectors.toList());
		} else {
			return null;
		}
	}

	public LotOrder findOrderByReceiptId(String receiptId) {
		return orderRepository.findAll().stream()
				.filter(lotOrder -> lotOrder.getReceiptId().equals(receiptId) && lotOrder.getStatus().equals(STATUS_WAITCUSTOMER))
				.findFirst().orElse(null);
	}

	public OrderDTO getOrderById(Long id) {
		LotOrder order = orderRepository.findById(id).get();
		try {
			return new OrderDTO(order);
		} catch (Exception e) {
			return null;
		}
	}

	public List<OrderDTO> findByConditions(String type, String plateNumber, String status) {
		try {
			return orderRepository.findAllByTypeLikeAndPlateNumberLikeAndStatusLike("%" + type + "%", "%" + plateNumber + "%", "%" + status + "%").stream()
					.map(OrderDTO::new)
					.collect(Collectors.toList());
		} catch (Exception e) {
			return null;
		}

	}

	public boolean readOrder(Long orderId, Long parkingBoyId
	) {
		try {
			LotOrder order = orderRepository.findById(orderId).orElse(null);
			if(order.getParkingBoy().getId()==parkingBoyId) {
				order.setRead(true);
				orderRepository.save(order);
				return true;
			}else{
				return false;
			}
		}catch (Exception e){
			return false;
		}
	}
}
