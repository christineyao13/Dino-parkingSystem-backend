package com.oocl.dino_parking_system.controller;

import com.alibaba.fastjson.JSONObject;
import com.oocl.dino_parking_system.config.WebSocketServer;
import com.oocl.dino_parking_system.dto.OrderDTO;
import com.oocl.dino_parking_system.dto.UserDTO;
import com.oocl.dino_parking_system.entitie.LotOrder;
import com.oocl.dino_parking_system.entitie.User;
import com.oocl.dino_parking_system.service.OrderService;
import com.oocl.dino_parking_system.service.ParkingBoyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	ParkingBoyService parkingBoyService;

//	@Transactional
//	@GetMapping(path = "")
//	public ResponseEntity getAllOrders(){
//		return new ResponseEntity<>(orderService.getAllOrders().stream()
//				.map(OrderDTO::new)
//				.collect(Collectors.toList()), HttpStatus.OK);
//	}

	//根据status搜索所有订单
	@Transactional
	@GetMapping(path = "/{status}")
	public List<OrderDTO> getOrdersByStatus(@PathVariable String status) {
		return orderService.getOrdersByStatus(status).stream()
				.map(OrderDTO::new)
				.collect(Collectors.toList());
	}

	// 修改订单状态
	@Transactional
	@PutMapping(path = "/{orderId}")
	public ResponseEntity changeStatus(@PathVariable("orderId") Long orderId,
	                                   @RequestBody JSONObject request,
	                                   @RequestParam(value = "appoint", required = false) boolean appoint) {
		Long parkingBoyId = Long.valueOf(request.get("parkingBoyId").toString());
		String status = request.get("status").toString();
		User parkingBoy = parkingBoyService.findParkingBoyById(parkingBoyId);
		if (parkingBoy != null
				&& parkingBoyService.findAllNotFullParkingLots(parkingBoyId).size() > 0) {
			if (orderService.changeOrderStatus(orderId, parkingBoy, status, null,appoint)) {
				if(appoint) {
					//websocket推送指派订单提醒给小弟
					JSONObject message = new JSONObject();
					message.put("id", orderId);
					message.put("type","newOrder");
					message.put("message", "你被指派了新的停车订单!");
					try {
						WebSocketServer.sendInfo(message.toJSONString(),parkingBoyId.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return ResponseEntity.status(HttpStatus.OK).build();
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@Transactional
	@PatchMapping(path = "/{orderId}")
	public ResponseEntity readOrder(@PathVariable("orderId") Long id,
	                                @RequestBody JSONObject req) {
		if (orderService.readOrder(id,
				Long.valueOf(req.get("parkingBoyId").toString()))) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

//	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
	@Transactional
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getAllOrders(@RequestParam(value = "id", required = false) Long id,
	                                   @RequestParam(value = "type", required = false) String type,
	                                   @RequestParam(value = "plateNumber", required = false) String plateNumber,
	                                   @RequestParam(value = "status", required = false) String status) {
		if (type == null) {
			type = "";
		}
		if (plateNumber == null) {
			plateNumber = "";
		}
		if (status == null) {
			status = "";
		}
		if (id != null) {
			// 根据ID查找
			return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
		}
		List<OrderDTO> orderDTOS = orderService.findByConditions(type, plateNumber, status);
		return new ResponseEntity<>(orderDTOS, HttpStatus.OK);

	}
}
