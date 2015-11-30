package SpringTest;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ncu.testbank.admin.data.Academy;
import com.ncu.testbank.admin.service.IAcademyService;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.JWTUtils;
import com.ncu.testbank.permission.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-mybatis.xml"})
public class SpringJunitTest {
	
	@Autowired
	private IAcademyService dataService;
	
	@Autowired
	private IUserService userService;
	
	
	@Test
	public void test() {
		PageInfo page = new PageInfo();
		page.setRows(3);
		page.setPage(0);
		List<Academy> list = dataService.searchData(page);
		System.out.println(page.getTotal());
		System.out.println(list);
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
//		JedisPool jedisPool = JedisPoolUtils.getPool();
//		Jedis jedis = jedisPool.getResource();
//		
//		User user = new User();
//		user.setName("jedeft");
//		user.setUsername("admin");
//		user.setPassword("admin");
//		user.setRole_id(1);
//		
//		String json = JSONUtils.convertObject2Json(user);
//		jedis.setex("user", 20, json);
//		
//		//归还连接池
//		JedisPoolUtils.returnResource(jedisPool, jedis);
		userService.createToken("admin");
	}
	     
}
