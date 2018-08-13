package com.oocl.dino_parking_system.service;

import com.alibaba.fastjson.JSONObject;
import com.oocl.dino_parking_system.config.WebSocketServer;
import com.oocl.dino_parking_system.dto.UserDTO;
import com.oocl.dino_parking_system.entitie.Role;
import com.oocl.dino_parking_system.repository.RoleRepository;
import com.oocl.dino_parking_system.util.MD5Util;
import com.oocl.dino_parking_system.entitie.User;
import com.oocl.dino_parking_system.repository.UserRepository;
import com.oocl.dino_parking_system.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.oocl.dino_parking_system.constant.Constants.*;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserService() {
	}

	public User findByUsername(String username){
		return userRepository.findByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		UserDetails user = userRepository.findByUsername(s);

		if (user == null) {
			throw new UsernameNotFoundException("用户名不存在");
		}
		if(((User) user).getStatus()) {
			return user;
		}else{
			return null;
		}
	}


	public boolean validate(User user) {
		User entity = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
		if (entity != null) {
			return true;
		}
		return false;
	}

	public String createUser(User user) {
		try {
			String password = UUID.randomUUID()
					.toString()
					.substring(0, 7);
			user.setPassword(new PasswordEncoder(SALT_STRING, "MD5")
					.encode(password)
					.substring(0, 7));
			user.getRoles().add(roleRepository.findByName(ROLE_PARKINGBOY));
			userRepository.save(user);
			return password;
		} catch (Exception e) {
			return null;
		}
	}

	public List<User> getAllUser() {
		try {
			return userRepository.findAll();
		} catch (Exception e) {
			return null;
		}
	}

	public JSONObject changeUserStatus(Long id, boolean status) {
		JSONObject result = new JSONObject();
		try {
			User one = userRepository.findById(id).orElse(null);
			if (one.getRoles().get(0).equals(ROLE_ADMIN)) {
				result.put("result", "failed");
				result.put("cause", "管理员账号不能冻结");
				return result;
			}
			if (one.getLotOrders().size() == 0 || status == STATUS_NORMAL || one.getParkingLots().size() == 0) {
				one.setStatus(status);
				userRepository.save(one);
				result.put("result", "success");
				if (status == STATUS_FREEZE) {
					JSONObject websockeMessage = new JSONObject();
					websockeMessage.put("type", "freeze");
					WebSocketServer.sendInfo(websockeMessage.toJSONString(), id.toString());
				}

			} else {
				result.put("result", "failed");
				result.put("cause", "该停车员手下还有管理的停车场");
			}
			return result;
		} catch (Exception e) {
			result.put("result", "failed");
			result.put("cause", "用户不存在");
			return result;
		}
	}

	public User getUserById(Long id) {
		try {
			User one = userRepository.findById(id).get();
			return one;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean updateUser(Long id, User user) {
		try {
			User one = userRepository.findById(id).get();
			one.setNickname(user.getNickname() != null ? user.getNickname() : one.getNickname());
			one.setUsername(user.getUsername() != null ? user.getUsername() : one.getUsername());
			one.setEmail(user.getEmail() != null ? user.getEmail() : one.getEmail());
			one.setPhone(user.getPhone() != null ? user.getPhone() : one.getPhone());
			one.setWorkStatus(!user.getWorkStatus().equals("")?user.getWorkStatus():one.getWorkStatus());
			System.out.println(user.getRoleName());
			if(user.getRoleName()!=null && one.getParkingLots().size()==0) {
				Role role = roleRepository.findByName(user.getRoleName());
				one.setRoles(new ArrayList<>());
				one.getRoles().add(role);
			}else if(user.getRoleName()!=null && one.getParkingLots().size()!=0){
				return false;
			}

			userRepository.save(one);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean save(User user) {
		userRepository.save(user);
		return true;
	}

	public boolean delete(Long id) {
		userRepository.deleteById(id);
		return true;
	}

	public boolean edit(User user) {
		userRepository.save(user);
		return true;
	}

	public boolean exits(User user) {
		User entity = userRepository.findByUsername(user.getUsername());
		if (entity != null) {
			return true;
		}
		return false;
	}

	public boolean changeWorkStatus(Long id, String workStatus) {
		try {
			User user = userRepository.findById(id).orElse(null);
			if (!workStatus.trim().equals("")) {
				user.setWorkStatus(workStatus);
				user.setLastSignInDate(ZonedDateTime.now());
				userRepository.save(user);
				return true;
			} else {
				boolean result = false;
				switch (user.getWorkStatus()) {
					case STATUS_LATE:
					case STATUS_ONDUTY:
						if (!isYesterDay(user.getLastSignInDate())) {
							user.setWorkStatus(STATUS_OFFDUTY);
							user.setLastSignInDate(ZonedDateTime.now());
							userRepository.save(user);
							result = true;
						}
					case STATUS_OFFDUTY:
						if(!result) {
							if (isLate()) {
								user.setWorkStatus(STATUS_LATE);
							} else {
								user.setWorkStatus(STATUS_ONDUTY);
							}
							user.setLastSignInDate(ZonedDateTime.now());
							userRepository.save(user);
							result = true;
						}
				}
				return result;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isLate() {
		ZonedDateTime now = ZonedDateTime.now();
		return now.getHour()>9 || (now.getHour()==9 && now.getMinute()>0);
	}

	private boolean isYesterDay(ZonedDateTime lastSignInDate) {
		return ZonedDateTime.now().getDayOfYear() - lastSignInDate.getDayOfYear() > 0;
	}

	public List<UserDTO> findByConditions(String username, String nickname, Boolean status, String workStatus, String email, String phone) {
		List<User> users = userRepository.findAllByUsernameLikeAndNicknameLikeAndWorkStatusLikeAndEmailLikeAndPhoneLike("%" + username + "%", "%" + nickname + "%", "%" + workStatus + "%", "%" + email + "%", "%" + phone + "%");
		List<UserDTO> userDTOS = users.stream().map(UserDTO::new)
				.collect(Collectors.toList());
		if (status == null) {
			return users.stream().map(UserDTO::new)
					.collect(Collectors.toList());
		} else if (status) {
			return users.stream().filter(user -> user.getStatus()).map(UserDTO::new)
					.collect(Collectors.toList());
		} else {
			return users.stream().filter(user -> user.getStatus() == false).map(UserDTO::new)
					.collect(Collectors.toList());
		}

	}
}