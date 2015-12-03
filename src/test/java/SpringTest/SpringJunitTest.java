package SpringTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ncu.testbank.base.utils.JWTUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")  
@ContextHierarchy({  
        @ContextConfiguration(name = "parent", locations = "classpath:spring-mybatis.xml"),  
        @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")  
})  
public class SpringJunitTest {
	
	@Autowired  
    private WebApplicationContext wac;  
    private MockMvc mockMvc;
    
    @Before  
    public void setUp() {  
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  
    } 
	
	@Test
	public void test() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/1"))  
	            .andExpect(MockMvcResultMatchers.view().name("user/view"))  
	            .andExpect(MockMvcResultMatchers.model().attributeExists("user"))  
	            .andDo(MockMvcResultHandlers.print())  
	            .andReturn();  
	      
	    Assert.assertNotNull(result.getModelAndView().getModel().get("user"));  
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
//		userService.createToken("admin");
	}
	     
}
