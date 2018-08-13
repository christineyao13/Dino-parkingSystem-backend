package com.oocl.dino_parking_system.init;

import com.oocl.dino_parking_system.entitie.LotOrder;
import com.oocl.dino_parking_system.entitie.ParkingLot;
import com.oocl.dino_parking_system.entitie.Role;
import com.oocl.dino_parking_system.entitie.User;
import com.oocl.dino_parking_system.repository.OrderRepository;
import com.oocl.dino_parking_system.repository.ParkingLotsRepository;
import com.oocl.dino_parking_system.repository.RoleRepository;
import com.oocl.dino_parking_system.repository.UserRepository;
import com.oocl.dino_parking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Arrays;

import static com.oocl.dino_parking_system.constant.Constants.*;

@Component
@Order(value = 3)
public class GenerateData implements CommandLineRunner {
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ParkingLotsRepository parkingLotsRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserService userService;

	@Override
	public void run(String... args) throws Exception {
		/*
		 * 角色初始化数据
		 *
		 * */
		Role admin = new Role(1L, ROLE_ADMIN);
		Role manager = new Role(2L, ROLE_MANAGER);
		Role parkingBoy = new Role(3L, ROLE_PARKINGBOY);
		roleRepository.saveAll(Arrays.asList(admin, manager, parkingBoy));
		System.out.println("===============角色初始化完成=============");
		/*
		 * 员工初始化数据
		 *
		 * */

		User user1 = new User("admin", "管理员", "bd72696", "110@qq.com", "13160675789");
		user1.setRoles(Arrays.asList(admin));
		user1.setWorkStatus(STATUS_ONDUTY);
		user1.setLastSignInDate(ZonedDateTime.now());
		User user2 = new User("parkingboy1", "梁之之", "bd72696", "120@qq.com", "13160675789");
		user2.setRoles(Arrays.asList(parkingBoy));
		user2.setWorkStatus(STATUS_ONDUTY);
		user2.setLastSignInDate(ZonedDateTime.now());
		User user3 = new User("manager", "梁键键", "bd72696", "130@qq.com", "13160675789");
		user3.setRoles(Arrays.asList(manager));
		user3.setWorkStatus(STATUS_ONDUTY);
		User user4 = new User("parkingboy2", "张琳琳", "bd72696", "121@qq.com", "13160675789");
		user4.setRoles(Arrays.asList(parkingBoy));
		user4.setWorkStatus(STATUS_ONDUTY);
		User user5 = new User("parkingboy3", "李秋秋", "bd72696", "122@qq.com", "13160675789");
		user5.setRoles(Arrays.asList(parkingBoy));
		User user6 = new User("parkingboy4", "张全雨", "bd72696", "123@qq.com", "13164675789");
		user6.setRoles(Arrays.asList(parkingBoy));
		User user7 = new User("parkingboy5", "李云", "bd72696", "124@qq.com", "13162434789");
		user7.setRoles(Arrays.asList(parkingBoy));
		User user8 = new User("parkingboy6", "葛平", "bd72696", "125@qq.com", "13144475789");
		user8.setRoles(Arrays.asList(parkingBoy));
		User user9 = new User("parkingboy7", "谢璇", "bd72696", "126@qq.com", "13160333789");
		user9.setRoles(Arrays.asList(parkingBoy));
		user9.setWorkStatus(STATUS_ONDUTY);
		User user10 = new User("parkingboy8", "韩冰", "bd72696", "127@qq.com", "13112675789");
		user10.setRoles(Arrays.asList(parkingBoy));
		User user11 = new User("parkingboy9", "梁逸峰", "bd72696", "128@qq.com", "13132175789");
		user11.setRoles(Arrays.asList(parkingBoy));
		User user12 = new User("parkingboy10", "冯中", "bd72696", "129@qq.com", "13160423489");
		user12.setRoles(Arrays.asList(parkingBoy));
		User user13 = new User("parkingboy11", "庄中中", "bd72696", "132@qq.com", "1311234789");
		user13.setRoles(Arrays.asList(parkingBoy));
		User user14 = new User("parkingboy12", "王小川", "bd72696", "133@qq.com", "1611234789");
		user14.setRoles(Arrays.asList(parkingBoy));

		userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
				user8, user9, user10, user11, user12, user13, user14));
		System.out.println("===============用户初始化完成=============");

		/*
		 * 停车场初始化数据
		 *
		 * */
		ParkingLot parkingLot1 = new ParkingLot("oocl-西南停车场", 20);
		parkingLot1.setParkingBoy(user2);
		parkingLot1.setCarNum(10);
		ParkingLot parkingLot2 = new ParkingLot("oocl-东停车场", 10);
		parkingLot2.setParkingBoy(user2);
		parkingLot2.setCarNum(10);
		ParkingLot parkingLot3 = new ParkingLot("oocl-西停车场", 20);
		ParkingLot parkingLot4 = new ParkingLot("oocl-南停车场", 25);
		ParkingLot parkingLot5 = new ParkingLot("oocl-北停车场", 20);
		parkingLot5.setParkingBoy(user5);
		parkingLot5.setCarNum(19);
		ParkingLot parkingLot6 = new ParkingLot("oocl-东北停车场", 14);
		parkingLot6.setParkingBoy(user6);
		parkingLot6.setCarNum(11);
		ParkingLot parkingLot7 = new ParkingLot("oocl-东南停车场", 4);
		parkingLot7.setParkingBoy(user7);
		parkingLot7.setCarNum(3);
		ParkingLot parkingLot8 = new ParkingLot("cargosmart-西停车场", 10);
		ParkingLot parkingLot9 = new ParkingLot("cargosmart-东停车场", 10);
		ParkingLot parkingLot10 = new ParkingLot("cargosmart-南停车场", 10);
		ParkingLot parkingLot11 = new ParkingLot("cargosmart-北停车场", 12);
		ParkingLot parkingLot12 = new ParkingLot("cargosmart-西南停车场", 10);
		ParkingLot parkingLot13 = new ParkingLot("cargosmart-东西停车场", 10);
		parkingLotsRepository.saveAll(Arrays.asList(parkingLot1, parkingLot2, parkingLot3, parkingLot4,
				parkingLot5, parkingLot6, parkingLot7, parkingLot8, parkingLot9, parkingLot10,
				parkingLot11,parkingLot12,parkingLot13));
		System.out.println("===============停车场初始化完成=============");
		/*
		 * 订单初始化数据
		 *
		 * */
		LotOrder order1 = new LotOrder("粤DHC967", "1");
		LotOrder order2 = new LotOrder("粤C11111", "2");
		LotOrder order3 = new LotOrder("粤C23333", "3");
		LotOrder order4 = new LotOrder("粤CH7647", "4");
		LotOrder order5 = new LotOrder("粤AHC767", "5");
		LotOrder order6 = new LotOrder("粤VH7147", "6");
		LotOrder order7 = new LotOrder("粤BHC467", "7");
		LotOrder order8 = new LotOrder("粤NH7347", "8");
		LotOrder order9 = new LotOrder("粤NH8888", "9");
		LotOrder order10 = new LotOrder("京NH7777", "10");
		LotOrder order11 = new LotOrder("川NH3333", "11");
		LotOrder order12 = new LotOrder("粤MH6666", "12");
		LotOrder order13 = new LotOrder("湘NH3747", "13");
		LotOrder order14 = new LotOrder("粤NH1747", "14");
		LotOrder order15 = new LotOrder("粤NH5747", "15");
		order8.setStatus(STATUS_WAITCUSTOMER);
		order8.setType(TYPE_PARKOUTCAR);
		order8.setParkingBoy(user2);
		order8.setPlateNumber("粤C12345");
		order8.setParkingLotName("oocl停车场1");
		order7.setStatus(STATUS_WAITCUSTOMER);
		order7.setType(TYPE_PARKOUTCAR);
		order7.setParkingBoy(user2);
		order7.setPlateNumber("粤C32412");
		order7.setParkingLotName("oocl停车场2");
//		orderRepository.saveAll(Arrays.asList(order1, order2, order3, order4, order5, order6, order7, order8,
//				order9,order10,order11,order12,order13,order14,order15));
		orderRepository.saveAll(Arrays.asList(order1, order2));
		System.out.println("===============未抢订单初始化完成=============");
	}
}
