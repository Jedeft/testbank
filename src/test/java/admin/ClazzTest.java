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

import com.ncu.testbank.base.utils.JWTUtils;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")  
@ContextHierarchy({  
        @ContextConfiguration(name = "parent", locations = "classpath:spring-mybatis.xml"),  
        @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")  
})  
public class ClazzTest {
	
	@Autowired  
    private WebApplicationContext wac;  
	
    private MockMvc mockMvc;
    
    @Before  
    public void setUp() {  
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  
    } 
	
    
    /**
     * url : /admin/classes
     * method : POST
     * @throws Exception
     */
	@Test
	public void insertTest() throws Exception{
		StringBuilder requestBody = new StringBuilder().append("{");
//		requestBody.append("\" \":");
//		requestBody.append("\" \",");pp
//		requestBody.append("\" \":");
//		requestBody.append("\" \"}");
		requestBody.append("\"class_id\":");
		requestBody.append("\"1\",");
		requestBody.append("\"major_id\":");
		requestBody.append("\"123\",");
		requestBody.append("\"name\":");
		requestBody.append("\"软件工程121班\"}");
		
		mockMvc.perform(post("/admin/classes").contentType(MediaType.APPLICATION_JSON)
											   .content(requestBody.toString())
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /admin/classes
	 * method : PATCH
	 * @throws Exception
	 */
	@Test
	public void updateTest() throws Exception{
		StringBuilder requestBody = new StringBuilder().append("{");
		requestBody.append("\"class_id\":");
		requestBody.append("\"1\",");
		requestBody.append("\"name\":");
		requestBody.append("\"计算机软件121班\"}");
		
		mockMvc.perform(patch("/admin/classes").contentType(MediaType.APPLICATION_JSON)
											   .content(requestBody.toString())
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /admin/classes/{class_id}
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void selectOneTest() throws Exception{
		mockMvc.perform(get("/admin/classes/{class_id}", 1).contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /admin/classes/{class_id}
	 * method : DELETE
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws Exception{
		mockMvc.perform(delete("/admin/classes/{class_id}", 1).contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /admin/classes/csv
	 * params : file
	 * method : POST
	 * @throws Exception
	 */
	@Test
	public void loadCsvTest() throws Exception {
		File file = new File("E:/CSV/classes.csv");
		InputStream in = new FileInputStream(file);
		MockMultipartFile mokeFile = new MockMultipartFile("file", "classes.csv", null, in);
		mockMvc.perform(fileUpload("/admin/classes/csv").file(mokeFile))
												    .andDo(print());
	}
	
	/**
	 * url : /admin/classes
	 * params : page=1 , rows=15
	 * 		  : class_id, major_id, name
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void searchTest() throws Exception{
		mockMvc.perform(get("/admin/classes/?page=1&rows=15&name=软件工程121班").contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
}
