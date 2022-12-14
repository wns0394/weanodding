package com.ssafy.api.service;

import com.ssafy.api.request.UserUpdatePostReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserRepositorySupport;

import java.util.Optional;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserRepositorySupport userRepositorySupport;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public User createUser(UserRegisterPostReq userRegisterInfo) {
		User user = new User();
		user.setUserId(userRegisterInfo.getId());
		// 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
		user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
		user.setName(userRegisterInfo.getName());
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(String userId) {
		userRepository.delete(getUserByUserId(userId));
	}

	@Override
	public User getUserByUserId(String userId) {
		// 디비에 유저 정보 조회 (userId 를 통한 조회).
		Optional<User> optUser = userRepositorySupport.findUserByUserId(userId);
		return optUser.isPresent() ? optUser.get() : null;
	}

	@Override
	public User updateUser(String userId, UserUpdatePostReq userUpdateInfo) {
		User user = getUserByUserId(userId);
		user.setName(userUpdateInfo.getName());
		user.setPassword(passwordEncoder.encode(userUpdateInfo.getPassword()));
		userRepository.save(user);

		return user;
	}

}
