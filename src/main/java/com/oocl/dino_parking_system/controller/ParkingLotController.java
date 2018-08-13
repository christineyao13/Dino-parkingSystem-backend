package com.oocl.dino_parking_system.controller;

import com.oocl.dino_parking_system.dto.ParkingLotDashBoardDTO;
import com.oocl.dino_parking_system.dto.ParkingLotTinyDTO;
import com.oocl.dino_parking_system.entitie.ParkingLot;
import com.oocl.dino_parking_system.service.ParkingLotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/parkingLots")
public class ParkingLotController {
	private final ParkingLotsService parkingLotsService;

	@Autowired
	public ParkingLotController(ParkingLotsService parkingLotsService) {
		this.parkingLotsService = parkingLotsService;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
	@Transactional
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity createParkingLots(@RequestBody ParkingLot parkingLot) {
		if (parkingLotsService.createParkingLots(parkingLot)) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
	@Transactional
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getAllParkingLots(@RequestParam(value = "id", required = false) Long id,
	                                        @RequestParam(value = "name", required = false) String name,
	                                        @RequestParam(value = "eq", required = false) Integer eq,
	                                        @RequestParam(value = "gt", required = false) Integer gt,
	                                        @RequestParam(value = "lt", required = false) Integer lt,
	                                        @RequestParam(value = "noParkingBoy", required = false) boolean noParkingBoy) {
		if (noParkingBoy) {
			// 返回所有未安排小弟的停车场，并且未停车的停车场
			return new ResponseEntity<>(parkingLotsService.findAll().stream()
					.filter(parkingLot -> parkingLot.getParkingBoy() == null && parkingLot.getCarNum() == 0)
					.map(ParkingLotTinyDTO::new)
					.collect(Collectors.toList()), HttpStatus.OK);
		}
            List<ParkingLotTinyDTO> parkingLotTinyDTOS = new ArrayList<>();

            if (id != null) {
                // 根据ID查找
                return new ResponseEntity<>(parkingLotsService.findParkingLotById(id), HttpStatus.OK);
            }
            // TODO:组合查询
            if (name != null && eq != null) {
                parkingLotTinyDTOS = parkingLotsService.findAllParkingLotsByNameAndSize(name, eq);
            } else if (name != null) {
//            System.out.println(name);
                // TODO:根据名字查询
                parkingLotTinyDTOS = parkingLotsService.findAllParkingLotsByName(name);
            } else if (gt != null && lt != null) {
                // 根据大小范围查找
                parkingLotTinyDTOS = parkingLotsService.findAllParkingLotsBySizeBetween(gt, lt);
            } else if (gt != null) {
                // 大于等于
                parkingLotTinyDTOS = parkingLotsService.findAllParkingLotsBySizeGreaterThanEqual(gt);
            } else if (lt != null) {
                // TODO: 小于等于
                parkingLotTinyDTOS = parkingLotsService.findAllParkingLotsBySizeLessThanEqual(lt);
            } else {
                parkingLotTinyDTOS = parkingLotsService.getAllParkingLots();
            }

            return new ResponseEntity<>(parkingLotTinyDTOS, HttpStatus.OK);


        }

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
	@Transactional
	@PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity updateParkingLots(@PathVariable long id, @RequestBody ParkingLot parkingLot) {
		if (parkingLotsService.updateParkingLots(id, parkingLot)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
	@Transactional
	@PatchMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity changeParkingLotStatus(@PathVariable long id) {
		if (parkingLotsService.changeParkingLotStatus(id)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
	@Transactional
	@GetMapping(path = "/dashboard")
	public List<ParkingLotDashBoardDTO> findAllParkingLotDashBoard() {
		return parkingLotsService.findAllParkingLotDashBoard();
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
	@Transactional
	@GetMapping(path = "/dashboard/page/{page}/pageSize/{size}")
	public List<ParkingLotDashBoardDTO> findAllParkingLotDashBoard(@PathVariable int page,
	                                                               @PathVariable int size) {
		return parkingLotsService.findAllParkingLotDashBoardByPaging(new PageRequest(page, size));
	}

}
