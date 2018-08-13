package com.oocl.dino_parking_system.repository;

import com.oocl.dino_parking_system.entitie.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
	User findByUsername(String username);

	User findByUsernameAndPassword(String name, String password);

	List<User> findAllByUsernameLikeAndNicknameLikeAndWorkStatusLikeAndEmailLikeAndPhoneLike(String username,
	                                                                                         String nickname,
	                                                                                         String workStatus,
	                                                                                         String email,
	                                                                                         String phone);

}
