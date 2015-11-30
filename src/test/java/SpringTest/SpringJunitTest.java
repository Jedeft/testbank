package SpringTest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.ncu.testbank.admin.dao.IAcademyDao;
import com.ncu.testbank.admin.data.Academy;
import com.ncu.testbank.base.utils.JSONUtils;
import com.ncu.testbank.base.utils.JWTUtils;
import com.ncu.testbank.base.utils.JedisPoolUtils;
import com.ncu.testbank.permission.data.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:springTest.xml"})
public class SpringJunitTest {
	
	@Autowired
	private IAcademyDao dataDao;
	
	
	@Test
	public void test() {
		Academy academy = new Academy();
		academy.setAcademy_id("1");
		academy.setName("demo");
		int a = dataDao.updateOne(academy);
		System.out.println(a);
	}
	
	
	// 第一部分 {alg=HS256} 第二部分{sub=Joe}
	// 先验证token的合法性，之后再做用户信息验证
	// 每个token放在redis里面做缓存
	// 若没有请求，那么定期清理redis缓存
	@Test
	public void testJWT(){
		String token = JWTUtils.createToken("Jedeft");
		System.out.println(JWTUtils.validateToken(token, "Jedeft"));
	}
	
	@Test
	public void testJedis() {
		JedisPool jedisPool = JedisPoolUtils.getPool();
		Jedis jedis = jedisPool.getResource();
		
		User user = new User();
		user.setName("jedeft");
		user.setUsername("admin");
		user.setPassword("admin");
		user.setRole_id(1);
		
		String json = JSONUtils.convertObject2Json(user);
		jedis.setex("user", 20, json);
		
		//归还连接池
		JedisPoolUtils.returnResource(jedisPool, jedis);
	}
	     
}
