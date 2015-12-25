package teacher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.CharEncoding;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ncu.testbank.base.utils.JSONUtils;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.params.TemplateParams;

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
public class TemplateTest {
	
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
     * url : /teacher/templates
     * method : POST
     * @throws Exception
     */
	@Test
	public void insertTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		
		TemplateParams template = new TemplateParams();
		template.setCourse_id("1");
		template.setEasy_ratio(0.3);
		template.setHard_ratio(0.5);
		template.setMedium_ratio(0.2);
		template.setJudge_num(15);
		template.setJudge_score(30);
		template.setLevel(5);
		template.setMultiple_num(15);
		template.setMultiple_score(30);
		template.setName("demo");
		List<Long> point_id = new ArrayList<>();
		point_id.add(123L);
		point_id.add(345L);
		point_id.add(1460L);
		template.setPoint_id(point_id);
		template.setShortAnswer_num(16);
		template.setShortAnswer_score(50);
		template.setSingle_num(15);
		template.setSingle_score(50);
		template.setType(1);
		
		mockMvc.perform(post("/teacher/templates").session(mockSession)
											   .contentType(MediaType.APPLICATION_JSON)
											   .content(JSONUtils.convertObject2Json(template))
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
     * url : /teacher/templates
     * method : PATCH
     * @throws Exception
     */
	@Test
	public void updateTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		TemplateParams template = new TemplateParams();
		template.setTemplate_id(145102983504701L);
		template.setCourse_id("78");
		template.setMultiple_num(15);
		template.setMultiple_score(30);
		template.setName("update");
		template.setShortAnswer_num(16);
		template.setShortAnswer_score(50);
		template.setSingle_num(15);
		template.setSingle_score(50);
		template.setType(1);
		
		mockMvc.perform(patch("/teacher/templates")
											   .session(mockSession)
											   .contentType(MediaType.APPLICATION_JSON)
											   .content(JSONUtils.convertObject2Json(template))
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /teacher/templates
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void searchTest() throws Exception{
		mockMvc.perform(get("/teacher/templates?course_id=1").contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /teacher/templates/{template_id}
	 * params : question_id
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void getTest() throws Exception{
		mockMvc.perform(get("/teacher/templates/{template_id}", 145102983504701L).contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
     * url : /teacher/templates
     * method : DELETE
     * @throws Exception
     */
	@Test
	public void deleteTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		
		Map<String,List<Long>> map = new HashMap<>();
		List<Long> list = new ArrayList<>();
		list.add(145102983504701L);
		map.put("tempalte_id", list);
		mockMvc.perform(delete("/teacher/templates").session(mockSession)
											   .content(JSONUtils.convertObject2Json(map))
											   .contentType(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
}
