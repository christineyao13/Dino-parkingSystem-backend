package com.oocl.dino_parking_system.constant;

public class Constants {
    public final static boolean STATUS_NORMAL = true; // 停车场开放(parkingBoy可用)
    public final static boolean STATUS_FREEZE = false;// 停车场关闭(parkingBoy不可用)
    public final static String ROLE_ADMIN = "ROLE_ADMIN";
    public final static String ROLE_MANAGER = "ROLE_MANAGER";
    public final static String ROLE_PARKINGBOY = "ROLE_PARKINGBOY";
    public final static String TYPE_PARKCAR = "parkCar";//停车类型
    public final static String TYPE_PARKOUTCAR = "parkOutCar";//取车类型
    public final static String STATUS_INUSE = "inUse";// 小票有效
    public final static String STATUS_DISABLED = "disabled";// 小票无效

	public final static String STATUS_NOROB = "noRob";// 存车订单无人处理
	public final static String STATUS_WAITPARK = "waitPark";// 存车订单被抢成功，等待停到停车场
	public final static String STATUS_PARKED = "parked";// 存车订单的车被停到停车场
	public final static String STATUS_WAITCUSTOMER = "waitCustomer";// 等待顾客来取车
	public final static String STATUS_WAITUNPARK = "waitUnPark";// 取车订单等待取车
	public final static String STATUS_FINISH = "finish";//存取车订单完成

	public static final String SALT_STRING = "DINO-PARKING-SYSTEM";

	public static final String STATUS_ONDUTY = "onduty"; //上班
	public static final String STATUS_OFFDUTY = "offduty"; //下班
	public static final String STATUS_LATE = "late"; //迟到
	public static final String STATUS_LEAVE = "leave"; //请假
}
