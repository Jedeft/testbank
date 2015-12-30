package common;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.CharEncoding;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ncu.testbank.base.utils.JSONUtils;
import com.ncu.testbank.common.data.params.MessageParams;
import com.ncu.testbank.permission.data.User;

/**
 * 对于需要rootAdmin权限的操作需要关掉@RequiresRoles("rootAdmin")注解才可执行
 * @author Jedeft
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")  
@ContextHierarchy({  
        @ContextConfiguration(name = "parent", locations = "classpath:spring-mybatis.xml"),  
        @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")  
})  
public class MessageTest {
	
	@Autowired  
    private WebApplicationContext wac;  
	
    private MockMvc mockMvc;
    
    private MockHttpSession mockSession;
    @Before  
    public void setUp() {  
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  
        mockSession = new MockHttpSession();
    } 
	
    
    /**
     * url : /common/messages
     * method : POST
     * @throws Exception
     */
	@Test
	public void insertTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		MessageParams mp = new MessageParams();
		mp.setMessage("这是一个通知！");
		mp.setTitle("这是一个主题！");
		List<String> receive_id = new ArrayList<>();
		receive_id.add("Tom");
		receive_id.add("Huli");
		mp.setReceive_id(receive_id);
		
		mockMvc.perform(post("/common/messages").session(mockSession)
											   .contentType(MediaType.APPLICATION_JSON)
											   .content(JSONUtils.convertObject2Json(mp))
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
     * url : /common/messages/title
     * method : GET
     * @throws Exception
     */
	@Test
	public void searchTileTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		
		mockMvc.perform(get("/common/messages/title?page=1&rows=15").session(mockSession)
											   .contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
//	/**
//	 * 该发送消息方法被舍弃，采用websocket
//     * url : /common/messages/sending
//     * method : GET
//     * @throws Exception
//     */
//	@Test
//	public void searchSendingTest() throws Exception{
//		User user = new User();
//		user.setUsername("Jerry");
//		mockSession.setAttribute("currentUser", user);
//		
//		mockMvc.perform(get("/common/messages/sending?page=1&rows=15&title_id=145129260141401").session(mockSession)
//											   .contentType(MediaType.TEXT_HTML)
//											   .characterEncoding(CharEncoding.UTF_8)
//											   .accept(MediaType.APPLICATION_JSON)
//											   .characterEncoding(CharEncoding.UTF_8))
//										  .andExpect(jsonPath("$.errorCode").value(0))
//										  .andDo(print());
//	}
	
	/**
     * url : /common/messages/receiving
     * method : GET
     * @throws Exception
     */
	@Test
	public void searchReceivingTest() throws Exception{
		User user = new User();
		user.setUsername("Tom");
		mockSession.setAttribute("currentUser", user);
		
		mockMvc.perform(get("/common/messages/receiving?page=1&rows=15").session(mockSession)
											   .contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	
	/**
     * url : /common/messages/{message_id}
     * method : PATCH
     * @throws Exception
     */
	@Test
	public void seeMessageTest() throws Exception{
		mockMvc.perform(patch("/common/messages/{message_id}",145129260203803L)
											   .contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	
	/**
     * url : /common/messages
     * method : GET
     * @throws Exception
     */
	@Test
	public void getMessageTest() throws Exception{
		mockMvc.perform(get("/common/messages?title_id=145129260141401")
											   .contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
     * url : /common/messages
     * method : DELETE
     * @throws Exception
     */
	@Test
	public void deleteMessageTest() throws Exception{
		Map<String, List<Long>> params = new HashMap<>();
		List<Long> list = new ArrayList<>();
		list.add(145129260141401L);
		params.put("title_id", list);
		
		mockMvc.perform(delete("/common/messages").content(JSONUtils.convertObject2Json(params))
											   .contentType(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
}
