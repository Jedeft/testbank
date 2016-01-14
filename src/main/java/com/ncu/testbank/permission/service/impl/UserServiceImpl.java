package com.ncu.testbank.permission.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.utils.JSONUtils;
import com.ncu.testbank.base.utils.JWTUtils;
import com.ncu.testbank.base.utils.JedisPoolUtils;
import com.ncu.testbank.permission.dao.IRoleDao;
import com.ncu.testbank.permission.dao.IUserDao;
import com.ncu.testbank.permission.data.Authen;
import com.ncu.testbank.permission.data.Permission;
import com.ncu.testbank.permission.data.Role;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.permission.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;
	@Autowired
	private IRoleDao roleDao;

	@Override
	public List<Role> searchRole(String username) {
		return roleDao.searchRole(username);
	}

	@Override
	public List<Role> searchAllRole(String username) {
		return roleDao.searchAllRole(username);
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
	public Authen createToken(String username) {
		JedisPool jedisPool = JedisPoolUtils.getPool();
		Jedis jedis = jedisPool.getResource();

		String code = JWTUtils.createToken(username);
		Authen authen = new Authen();
		authen.setToken(code);
		authen.setReAuth(false);
		String json = JSONUtils.convertObject2Json(authen);
		// 默认token保存3个小时
		jedis.setex(username, 3 * 60 * 60, json);
		// 归还连接池
		JedisPoolUtils.returnResource(jedisPool, jedis);

		return authen;
	}

	@Override
	public void reAuth(User user) {
		User DBUser = userDao.getUser(user.getUsername());
		if (DBUser == null) {
			throw new ServiceException(ErrorCode.USERNAME_MISSING);
		}
		if (!DBUser.getSecond_pwd().equals(user.getSecond_pwd())) {
			throw new ServiceException(ErrorCode.REAUTHEN_FAIL);
		}
		JedisPool jedisPool = JedisPoolUtils.getPool();
		Jedis jedis = jedisPool.getResource();
		// 缓存中设置二级认证通过
		String json = jedis.get(user.getUsername());
		if (json == null) {
			throw new ServiceException(ErrorCode.USER_UNLOGIN);
		}
		Authen authen = JSONUtils.convertJson2Object(json, Authen.class);
		authen.setReAuth(true);
		json = JSONUtils.convertObject2Json(authen);
		// 默认token保存3个小时
		jedis.setex(user.getUsername(), 3 * 60 * 60, json);
		// 归还连接池
		JedisPoolUtils.returnResource(jedisPool, jedis);
	}

	@Override
	public void logout(String username) {
		JedisPool jedisPool = JedisPoolUtils.getPool();
		Jedis jedis = jedisPool.getResource();
		// 删除缓存中用户信息
		jedis.del(username);
		// 归还连接池
		JedisPoolUtils.returnResource(jedisPool, jedis);
	}

	@Override
	public void updatePassword(User user) {
		if (userDao.updateOne(user) < 1) {
			throw new ServiceException(new ErrorCode(70001, "修改密码失败，请重试！"));
		}
	}

}
