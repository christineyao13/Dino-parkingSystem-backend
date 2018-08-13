package com.oocl.dino_parking_system.repository;

import com.oocl.dino_parking_system.entitie.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ParkingLotsRepository extends JpaRepository<ParkingLot,Long> {

	@Query("SELECT lot FROM ParkingLot lot where lot.name like %:name%")
	List<ParkingLot> findByName(@Param("name") String name);

	@Query("SELECT lot FROM ParkingLot lot where lot.size >= :size")
	List<ParkingLot> findAllBySizeGreaterThanEqual(@Param("size")Integer size); // 大于等于查询

	@Query("SELECT lot FROM ParkingLot lot where lot.size <= :size")
	List<ParkingLot> findAllBySizeLessThanEqual(@Param("size") Integer size); // 小于等于查询

	@Query("SELECT lot FROM ParkingLot lot where lot.size >= :left and lot.size <= :right")
	List<ParkingLot> findAllBySizeBetween(@Param("left")Integer left, @Param("right")Integer right); // 大于等于小于等于查询

	@Query("SELECT lot FROM ParkingLot lot where lot.name like %:name% and lot.size=:size")
	List<ParkingLot> findAllByNameAndSize(@Param("name")String name,@Param("size") Integer size);	//名字和容量

}
