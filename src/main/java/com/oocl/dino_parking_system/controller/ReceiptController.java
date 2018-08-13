package com.oocl.dino_parking_system.controller;

import com.alibaba.fastjson.JSONObject;
import com.oocl.dino_parking_system.entitie.LotOrder;
import com.oocl.dino_parking_system.entitie.Receipt;
import com.oocl.dino_parking_system.service.OrderService;
import com.oocl.dino_parking_system.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

import static com.oocl.dino_parking_system.constant.Constants.STATUS_WAITUNPARK;

@RestController
public class ReceiptController {

	@Autowired
    private OrderService orderService;
    @Autowired
	private ReceiptService receiptService;

    @Transactional
    @PostMapping("/receipts")
    public ResponseEntity park(@RequestBody JSONObject request) {
    	try {
		    Receipt receipt = receiptService.generateReceipt();
		    orderService.generateOrder(request.get("plateNum").toString(), receipt.getId());
		    return new ResponseEntity(receipt, HttpStatus.CREATED);
	    }catch (Exception e){
    		return ResponseEntity.badRequest().build();
	    }
    }

    @Transactional
	@PutMapping("/receipts/{receiptId}")
	public ResponseEntity unPark(@PathVariable String receiptId){
	    try {
		    LotOrder order = orderService.findOrderByReceiptId(receiptId);
		    orderService.changeOrderStatus(order.getId(), null, STATUS_WAITUNPARK, null, false );
		    return ResponseEntity.ok().build();
	    }catch (Exception e){
	    	return ResponseEntity.badRequest().build();
	    }
    }
}
