package com.oocl.dino_parking_system.service;

import com.oocl.dino_parking_system.entitie.Role;
import com.oocl.dino_parking_system.entitie.User;
import com.oocl.dino_parking_system.repository.RoleRepository;
import com.oocl.dino_parking_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.oocl.dino_parking_system.constant.Constants.ROLE_ADMIN;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
	UserRepository userRepository;

    public Role save(Role role){
        return roleRepository.save(role);
    }

	public RoleService() {
	}

	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public boolean setRoleToUser(Long id, String name) {
		User user = userRepository.findById(id).orElse(null);
		if(name.equals(ROLE_ADMIN))
		if(user!=null){
			Role role = roleRepository.findByName(name);
			if(role!=null){
				user.getRoles().add(role);
				userRepository.save(user);
				return true;
			}
			return false;
		}
		return false;
	}
}
