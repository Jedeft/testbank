package com.ncu.testbank.base.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.ncu.testbank.base.utils.JSONUtils;
import com.ncu.testbank.base.utils.JedisPoolUtils;
import com.ncu.testbank.permission.data.Authen;
import com.ncu.testbank.permission.data.User;

public class TokenFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("currentUser");
		if (null == user) {
			response.setCharacterEncoding("UTF-8");
			response.sendError(HttpStatus.UNAUTHORIZED.value(),
					"太长时间没有操作,请重新登录");
			return;
		} else {
			// 如果session中存在登录者实体，则继续
			JedisPool jedisPool = JedisPoolUtils.getPool();
			Jedis jedis = jedisPool.getResource();
			String json = jedis.get(user.getUsername());
			if (null == json || json.equals("")) {
				response.setCharacterEncoding("UTF-8");
				response.sendError(HttpStatus.UNAUTHORIZED.value(),
						"太长时间没有操作,请重新登录");
			}
			Authen authen = JSONUtils.convertJson2Object(json, Authen.class);

			String token = request.getHeader("Token");
			if (token == null) {
				response.setCharacterEncoding("UTF-8");
				response.sendError(HttpStatus.FORBIDDEN.value(),
						"Token已过期，请重新登录!");
				return;
			}

			if (!token.equals(authen.getToken())) {
				response.setCharacterEncoding("UTF-8");
				response.sendError(HttpStatus.FORBIDDEN.value(), "Token无法匹配！");
				return;
			}

			jedis.setex(user.getUsername(), 3 * 60 * 60, json);
			// 归还连接池
			JedisPoolUtils.returnResource(jedisPool, jedis);

			filterChain.doFilter(request, response);
		}
	}
}
