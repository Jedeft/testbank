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
public class SyllabusTest {
	
	@Autowired  
    private WebApplicationContext wac;  
	
    private MockMvc mockMvc;
    
    @Before  
    public void setUp() {  
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  
    } 
	
    
    /**
     * url : /admin/syllabuses
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
		requestBody.append("\"1\",");
		requestBody.append("\"course_id\":");
		requestBody.append("\"1\",");
		requestBody.append("\"start\":");
		requestBody.append("\"2015-01-01\",");
		requestBody.append("\"end\":");
		requestBody.append("\"2016-12-31\"}");
		
		mockMvc.perform(post("/admin/syllabuses").contentType(MediaType.APPLICATION_JSON)
											   .content(requestBody.toString())
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /admin/syllabuses
	 * method : PATCH
	 * @throws Exception
	 */
	@Test
	public void updateTest() throws Exception{
		StringBuilder requestBody = new StringBuilder().append("{");
		requestBody.append("\"syllabus_id\":");
		requestBody.append("\"144966941493001\",");
		requestBody.append("\"start\":");
		requestBody.append("\"2015-06-01\"}");
		
		mockMvc.perform(patch("/admin/syllabuses").contentType(MediaType.APPLICATION_JSON)
											   .content(requestBody.toString())
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /admin/syllabuses/{syllabuses_id}
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void selectOneTest() throws Exception{
		mockMvc.perform(get("/admin/syllabuses/{syllabuses_id}", "144966941493001").contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /admin/syllabuses/{syllabus_id}
	 * method : DELETE
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws Exception{
		mockMvc.perform(delete("/admin/syllabuses/{syllabus_id}", "144966941493001").contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /admin/syllabuses/batch
	 * method : DELETE
	 * @throws Exception
	 */
	@Test
	public void batchDeleteTest() throws Exception{
		Map<String, List<String>> map = new HashMap<>();
		List<String> list = new ArrayList<>();
		list.add("144967342319101");
		list.add("144967342319102");
		map.put("syllabus_id", list);
		mockMvc.perform(delete("/admin/syllabuses/batch").contentType(MediaType.APPLICATION_JSON)
											   .content(JSONUtils.convertObject2Json(map))
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /admin/syllabuses/csv
	 * params : file
	 * 			csv文件不用包含syllabus_id
	 * method : POST
	 * @throws Exception
	 */
	@Test
	public void loadCsvTest() throws Exception {
		File file = new File("E:/CSV/syllabuses.csv");
		InputStream in = new FileInputStream(file);
		MockMultipartFile mokeFile = new MockMultipartFile("file", "syllabuses.csv", null, in);
		mockMvc.perform(fileUpload("/admin/syllabuses/csv").file(mokeFile))
												    .andDo(print());
	}
	
	/**
	 * url : /admin/syllabuses
	 * params : page=1 , rows=15
	 * 		  : start, end
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void searchTest() throws Exception{
		mockMvc.perform(get("/admin/syllabuses/?page=1&rows=15").contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
}
