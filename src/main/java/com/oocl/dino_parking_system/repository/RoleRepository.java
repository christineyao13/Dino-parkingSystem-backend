package com.oocl.dino_parking_system.repository;

import com.oocl.dino_parking_system.entitie.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
	public Role findByName(String name);
}
