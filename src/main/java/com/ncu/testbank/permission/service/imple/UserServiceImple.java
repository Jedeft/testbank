package com.ncu.testbank.permission.service.imple;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncu.testbank.permission.dao.IUserDao;
import com.ncu.testbank.permission.data.Permission;
import com.ncu.testbank.permission.data.Role;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.permission.service.IUserService;

@Service("userService")
public class UserServiceImple implements IUserService {
	
	@Autowired
	private IUserDao userDao;

	@Override
	public boolean login(User user) {
		return false;
	}
	
	@Override
	public List<Role> searchRole(String username) {
		List<Role> roleList = new ArrayList<>();
		Role role = new Role();
		if (username.equals(1)) {
			role.setRole_id(1);
			role.setRole_name("admin");
		} else if (username.equals(1)) {
			role.setRole_id(2);
			role.setRole_name("teacher");
		} else {
			role.setRole_id(3);
			role.setRole_name("student");
		}
		roleList.add(role);
		return roleList;
	}

	@Override
	public List<Permission> searchPermission(long role_id) {
		return null;
	}

	@Override
	public User getUser(String username) {
		return userDao.getUser(username);
	}

}
