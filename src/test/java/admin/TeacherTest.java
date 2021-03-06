package admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayOutputStream;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ncu.testbank.base.utils.JSONUtils;
import com.ncu.testbank.base.utils.JWTUtils;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;

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
    
    @Before  
    public void setUp() {  
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  
    } 
	
    
    /**
     * url : /admin/teachers
     * method : POST
     * @throws Exception
     */
	@Test
	public void insertTest() throws Exception{
		StringBuilder requestBody = new StringBuilder().append("{");
//		requestBody.append("\" \":");
//		requestBody.append("\" \",");
//		requestBody.append("\" \":");
//		requestBody.append("\" \"}");
		requestBody.append("\"teacher_id\":");
		requestBody.append("\"123\",");
		requestBody.append("\"academy_id\":");
		requestBody.append("\"1\",");
		requestBody.append("\"name\":");
		requestBody.append("\"Jedeft\",");
		requestBody.append("\"email\":");
		requestBody.append("\"jedeft@163.com\",");
		requestBody.append("\"phone\":");
		requestBody.append("\"18720081904\",");
		requestBody.append("\"qq\":");
		requestBody.append("\"470913537\"}");
		
		mockMvc.perform(post("/admin/teachers").contentType(MediaType.APPLICATION_JSON)
											   .content(requestBody.toString())
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /admin/teachers
	 * method : PATCH
	 * @throws Exception
	 */
	@Test
	public void updateTest() throws Exception{
		StringBuilder requestBody = new StringBuilder().append("{");
//		requestBody.append("\" \":");
//		requestBody.append("\" \",");
//		requestBody.append("\" \":");
//		requestBody.append("\" \"}");
		requestBody.append("\"teacher_id\":");
		requestBody.append("\"123\",");
//		requestBody.append("\"academy_id\":");
//		requestBody.append("\"1\",");
//		requestBody.append("\"name\":");
//		requestBody.append("\"Jerry\",");
//		requestBody.append("\"email\":");
//		requestBody.append("\"jedeft@163.com\",");
//		requestBody.append("\"phone\":");
//		requestBody.append("\"18720081904\",");
		requestBody.append("\"qq\":");
		requestBody.append("\"49999888\"}");
		
		mockMvc.perform(patch("/admin/teachers").contentType(MediaType.APPLICATION_JSON)
											   .content(requestBody.toString())
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /admin/teachers/{teacher_id}
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void selectOneTest() throws Exception{
		mockMvc.perform(get("/admin/teachers/{teacher_id}", 123).contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /admin/teachers/{teacher_id}
	 * method : DELETE
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws Exception{
		mockMvc.perform(delete("/admin/teachers/{teacher_id}", "test").contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /admin/teachers/batch
	 * method : DELETE
	 * @throws Exception
	 */
	@Test
	public void batchDeleteTest() throws Exception{
		Map<String, List<String>> map = new HashMap<>();
		List<String> list = new ArrayList<>();
		list.add("TEST001");
		list.add("TEST002");
		map.put("teacher_id", list);
		mockMvc.perform(delete("/admin/teachers/batch").contentType(MediaType.APPLICATION_JSON)
											   .content(JSONUtils.convertObject2Json(map))
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /admin/teachers/csv
	 * params : file
	 * method : POST
	 * @throws Exception
	 */
	@Test
	public void loadCsvTest() throws Exception {
		File file = new File("E:/CSV/teacher.csv");
		InputStream in = new FileInputStream(file);
		MockMultipartFile mokeFile = new MockMultipartFile("file", "teacher.csv", null, in);
		mockMvc.perform(fileUpload("/admin/teachers/csv").file(mokeFile))
												    .andDo(print());
	}
	
	/**
	 * url : /admin/teachers
	 * params : page=1 , rows=15
	 * 		  : teacher_id, academy_id, name
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void searchTest() throws Exception{
		mockMvc.perform(get("/admin/teachers/?page=1&rows=15").contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
}
