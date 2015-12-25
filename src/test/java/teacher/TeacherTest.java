package teacher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.ncu.testbank.teacher.data.params.TeacherInfoParams;

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
public class TeacherTest {
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
	 * url : /teacher/teachers/{teacher_id}
	 * 
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void getTeacherTest() throws Exception{
		mockMvc.perform(get("/teacher/teachers/{teacher_id}", 1).contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /teacher/teachers
	 * 
	 * method : PATCH
	 * @throws Exception
	 */
	@Test
	public void updateTeacherTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		TeacherInfoParams teacher = new TeacherInfoParams();
		teacher.setTeacher_id("Jerry");
		teacher.setEmail("foxMail@163.com");
		mockMvc.perform(patch("/teacher/teachers").session(mockSession)
											   .content(JSONUtils.convertObject2Json(teacher))
											   .contentType(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
}
