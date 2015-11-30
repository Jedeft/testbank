package com.ncu.testbank.permission.service.imple;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.utils.JWTUtils;
import com.ncu.testbank.base.utils.JedisPoolUtils;
import com.ncu.testbank.permission.dao.IUserDao;
import com.ncu.testbank.permission.data.Permission;
import com.ncu.testbank.permission.data.Role;
import com.ncu.testbank.permission.data.Token;
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

	@Override
	public Token createToken(String username) {
		JedisPool jedisPool = JedisPoolUtils.getPool();
		Jedis jedis = jedisPool.getResource();
		
		
		String code = JWTUtils.createToken(username);
		//默认token保存3个小时
		jedis.setex(username, 3*60*60, code);
		Token token = new Token();
		token.setSub(username);
		token.setToken(code);
		
		//归还连接池
		JedisPoolUtils.returnResource(jedisPool, jedis);
		return token;
	}

	@Override
	public boolean validateToken(String token, String username) {
		JedisPool jedisPool = JedisPoolUtils.getPool();
		Jedis jedis = jedisPool.getResource();
		
		if ( !JWTUtils.validateToken(token, username) ) {
			JedisPoolUtils.returnResource(jedisPool, jedis);
			throw new ServiceException(ErrorCode.TOKEN_INVALID);
		}
		
		//归还连接池
		JedisPoolUtils.returnResource(jedisPool, jedis);
		
		return true;
	}
	
	
}
