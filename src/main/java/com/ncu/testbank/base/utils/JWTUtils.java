package com.ncu.testbank.base.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;

public class JWTUtils {

	private static Key key = null;

	/**
	 * 生成Token，默认HS256算法
	 * 
	 * @param sub
	 *            : 即为username
	 * @return
	 */
	public static String createToken(String sub) {
		if (null == key) {
			key = MacProvider.generateKey();
		}
		return Jwts.builder().setSubject(sub)
				.signWith(SignatureAlgorithm.HS256, key).compact();
	}

	public static boolean validateToken(String token, String sub) {
		if (null == key) {
			key = MacProvider.generateKey();
		}
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody()
				.getSubject().equals(sub);
	}
}
