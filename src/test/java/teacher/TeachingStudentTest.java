package teacher;

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
public class TeachingStudentTest {
	
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
     * url : /teacher/teachingStudents
     * method : POST
     * @throws Exception
     */
	@Test
	public void insertTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("course_id", "1");
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add("3");
		requestBody.put("student_id", list);
		
		mockMvc.perform(post("/teacher/teachingStudents").session(mockSession)
											   .contentType(MediaType.APPLICATION_JSON)
											   .content(JSONUtils.convertObject2Json(requestBody))
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /teacher/students
	 * params : page=1 , rows=15
	 * 		  : academy_id, name
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void searchSutdentsTest() throws Exception{
		mockMvc.perform(get("/teacher/students/?page=1&rows=15&class_id=1").contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /teacher/teachingStudents
	 * params : page=1 , rows=15
	 * 		  : academy_id, name
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void searchTeachingStudentsTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		
		mockMvc.perform(get("/teacher/teachingStudents/?page=1&rows=15&class_id=1&course_id=1").session(mockSession)
											   .contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /admin/academys
	 * method : PATCH
	 * @throws Exception
	 */
	@Test
	public void updateTest() throws Exception{
		String requestBody = "{\"academy_id\":\"2\", \"name\":\"update!moke\"}";
		
		mockMvc.perform(patch("/admin/academys").contentType(MediaType.APPLICATION_JSON)
											   .content(requestBody)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /admin/academys/{academy_id}
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void selectOneTest() throws Exception{
		mockMvc.perform(get("/admin/academys/{academy_id}", 1).contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /admin/academys/{academy_id}
	 * method : DELETE
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws Exception{
		mockMvc.perform(delete("/admin/academys/{academy_id}", 123).contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /admin/academys/batch
	 * method : DELETE
	 * @throws Exception
	 */
	@Test
	public void batchDeleteTest() throws Exception{
		String temp = "{\"academy_id\":[\"1\",\"2\"]}";
		Map<String, List<String>> map = new HashMap<>();
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		map.put("academy_id", list);
		mockMvc.perform(delete("/admin/academys/batch").contentType(MediaType.APPLICATION_JSON)
											   .content(JSONUtils.convertObject2Json(map))
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /admin/academys/csv
	 * params : file
	 * method : POST
	 * @throws Exception
	 */
	@Test
	public void loadCsvTest() throws Exception {
		File file = new File("E:/CSV/academy.csv");
		InputStream in = new FileInputStream(file);
		MockMultipartFile mokeFile = new MockMultipartFile("file", "academy.csv", null, in);
		mockMvc.perform(fileUpload("/admin/academys/csv").file(mokeFile))
												    .andDo(print());
	}
	
	/**
	 * url : /admin/academys
	 * params : page=1 , rows=15
	 * 		  : academy_id, name
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void searchTest() throws Exception{
		mockMvc.perform(get("/admin/academys/?page=1&rows=15&academy_id=1").contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
}
