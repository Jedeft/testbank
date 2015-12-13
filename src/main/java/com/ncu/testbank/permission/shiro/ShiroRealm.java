package com.ncu.testbank.permission.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.utils.JSONUtils;
import com.ncu.testbank.base.utils.JWTUtils;
import com.ncu.testbank.base.utils.JedisPoolUtils;
import com.ncu.testbank.permission.data.Authen;
import com.ncu.testbank.permission.data.Role;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.permission.service.IUserService;

public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private IUserService userService;

	/**
	 * 为当前登录的Subject授予角色和权限
	 * 
	 * @see 经测试:本例中该方法的调用时机为需授权资源被访问时
	 * @see 经测试:并且每次访问需授权资源时都会执行该方法中的逻辑,这表明本例中默认并未启用AuthorizationCache
	 * @see 个人感觉若使用了Spring3
	 *      .1开始提供的ConcurrentMapCache支持,则可灵活决定是否启用AuthorizationCache
	 * @see 比如说这里从数据库获取权限信息时,先去访问Spring3.1提供的缓存,而不使用Shior提供的AuthorizationCache
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principal) {
		// 获取当前用户的登录名
		String currentUsername = (String) super
				.getAvailablePrincipal(principal);
		if (null == currentUsername) {
			// 用户信息丢失
			throw new ShiroException(ErrorCode.USERINFO_MISSING);
		}

		JedisPool jedisPool = JedisPoolUtils.getPool();
		Jedis jedis = jedisPool.getResource();

		String json = jedis.get(currentUsername);
		Authen authen = JSONUtils.convertJson2Object(json, Authen.class);
		if (!JWTUtils.validateToken(authen.getToken(), currentUsername)) {
			JedisPoolUtils.returnResource(jedisPool, jedis);
			throw new ServiceException(ErrorCode.TOKEN_INVALID);
		}
		jedis.setex(currentUsername, 3 * 60 * 60, json);
		// 归还连接池
		JedisPoolUtils.returnResource(jedisPool, jedis);

		List<Role> roleList = new ArrayList<>();
		// 判断用户是否做了二级权限认证
		if (!authen.isReAuth()) {
			// 未做二级权限认证
			roleList = userService.searchRole(currentUsername);
		} else {
			// 已做二级权限认证
			roleList = userService.searchAllRole(currentUsername);
		}
		if (null == roleList || roleList.size() <= 0) {
			throw new AuthorizationException();
		}
		// 为当前用户设置角色
		SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
		// 添加角色
		List<String> roleNameList = new ArrayList<>();
		for (Role role : roleList) {
			// 目前只做了角色认证，权限认证由于业务复杂度未到不采取
			// List<Permission> permissionList =
			// userService.searchPermission(role.getRole_id());
			// for (Permission permission : permissionList) {
			// permissionNameList.add(role.getRole_name() + ":" +
			// permission.getName());
			// }
			roleNameList.add(role.getRole_name());
		}
		simpleAuthorInfo.addRoles(roleNameList);
		System.out.println("已为用户赋予了 " + roleNameList.toString() + " 权限");
		return simpleAuthorInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		// 获取基于用户名和密码的令牌
		// 实际上这个authcToken是从LoginController里面currentUser.login(token)传过来的
		// 两个token的引用都是一样的
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = userService.getUser(token.getUsername());
		if (null == user) {
			throw new ShiroException(ErrorCode.USERINFO_MISSING);
		}
		// 这里的密码没有加密，实际应该有加密操作的
		char[] c = token.getPassword();
		String password = new String(c);
		if (user.getPassword().equals(password)) {
			AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(
					user.getUsername(), user.getPassword(), user.getName());
			this.setSession("currentUser", user);
			return authcInfo;
		} else {
			throw new ShiroException(ErrorCode.PASSWORD_ERROR);
		}
		// 没有返回登录用户名对应的SimpleAuthenticationInfo对象时,就会在LoginController中抛出UnknownAccountException异常
	}

	/**
	 * 将一些数据放到ShiroSession中,以便于其它地方使用
	 * 
	 * @see 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
	 */
	private void setSession(Object key, Object value) {
		Subject currentUser = SecurityUtils.getSubject();
		if (null != currentUser) {
			Session session = currentUser.getSession();
			System.out
					.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
			if (null != session) {
				session.setAttribute(key, value);
			}
		}
	}

}
