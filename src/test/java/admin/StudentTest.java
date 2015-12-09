package admin;

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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")  
@ContextHierarchy({  
        @ContextConfiguration(name = "parent", locations = "classpath:spring-mybatis.xml"),  
        @ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml")  
})  
public class StudentTest {
	
	@Autowired  
    private WebApplicationContext wac;  
	
    private MockMvc mockMvc;
    
    @Before  
    public void setUp() {  
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  
    } 
    
    /**
     * url : /admin/students
     * method : POST
     * @throws Exception
     */
	@Test
	public void insertTest() throws Exception{
		String requestBody = "{\"student_id\":\"1\", \"class_id\":\"2\", \"name\":\"Jedeft\"}";
		
		mockMvc.perform(post("/admin/students").contentType(MediaType.APPLICATION_JSON)
											   .content(requestBody)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /admin/students
	 * method : PATCH
	 * @throws Exception
	 */
	@Test
	public void updateTest() throws Exception{
		String requestBody = "{\"student_id\":\"1\", \"name\":\"update!Jedeft\"}";
		
		mockMvc.perform(patch("/admin/students").contentType(MediaType.APPLICATION_JSON)
											   .content(requestBody)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /admin/students/{student_id}
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void selectOneTest() throws Exception{
		mockMvc.perform(get("/admin/students/{student_id}", 1).contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /admin/students/{student_id}
	 * method : DELETE
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws Exception{
		mockMvc.perform(delete("/admin/students/{student_id}", 1).contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /admin/students/csv
	 * params : file
	 * method : POST
	 * @throws Exception
	 */
	@Test
	public void loadCsvTest() throws Exception {
		File file = new File("E:/CSV/students.csv");
		InputStream in = new FileInputStream(file);
		MockMultipartFile mokeFile = new MockMultipartFile("file", "students.csv", null, in);
		mockMvc.perform(fileUpload("/admin/students/csv").file(mokeFile))
												    .andDo(print());
	}
	
	/**
	 * url : /admin/academys
	 * params : page=1 , rows=15
	 * 		  : students_id, class_id, name
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void searchTest() throws Exception{
		mockMvc.perform(get("/admin/students/?page=1&rows=15").contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
}
