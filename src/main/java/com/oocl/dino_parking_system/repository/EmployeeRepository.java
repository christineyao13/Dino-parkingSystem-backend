package com.oocl.dino_parking_system.repository;

import com.oocl.dino_parking_system.entitie.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<User,Long> {
}
