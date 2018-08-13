package com.oocl.dino_parking_system.repository;

import com.oocl.dino_parking_system.entitie.LotOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<LotOrder, Long> {
    List<LotOrder> findByStatus(String status);

    List<LotOrder> findAllByTypeLikeAndPlateNumberLikeAndStatusLike(String type, String plateNumber, String status);
}
