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
import com.ncu.testbank.teacher.data.Multiple;
import com.ncu.testbank.teacher.data.Single;
import com.ncu.testbank.teacher.data.params.DELQuestionParams;
import com.ncu.testbank.teacher.data.params.ImgSingleParams;

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
public class MultipleTest {
	
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
     * url : /teacher/multiples/writing
     * method : POST
     * @throws Exception
     */
	@Test
	public void insertWritingTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		Multiple multiple = new Multiple();
		multiple.setPoint_id(145076563281901L);
		multiple.setQuestion("demo question222!");
		multiple.setA("answer one");
		multiple.setB("answer two");
		multiple.setC("answer three");
		multiple.setD("answer four");
		multiple.setLevel(3);
		multiple.setAnswer("C");
		
		mockMvc.perform(post("/teacher/multiples/writing").session(mockSession)
											   .contentType(MediaType.APPLICATION_JSON)
											   .content(JSONUtils.convertObject2Json(multiple))
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
     * url : /teacher/multiples/writing
     * method : PATCH
     * @throws Exception
     */
	@Test
	public void updateWritingTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		Multiple multiple = new Multiple();
		multiple.setQuestion_id(145092909244601L);
		multiple.setQuestion("update question!");
		multiple.setLevel(5);
		multiple.setAnswer("D");
		
		mockMvc.perform(patch("/teacher/multiples/writing")
											   .session(mockSession)
											   .contentType(MediaType.APPLICATION_JSON)
											   .content(JSONUtils.convertObject2Json(multiple))
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /teacher/multiples
	 * params : page=1 , rows=15, point_id
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void searchMultipleTest() throws Exception{
		mockMvc.perform(get("/teacher/multiples/?page=1&rows=15&point_id=145076563281901").contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /teacher/multiples/{question_id}
	 * params : question_id
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void getMultipleTest() throws Exception{
		mockMvc.perform(get("/teacher/multiples/{question_id}", 145092909244601L).contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
     * url : /teacher/multiples/img
     * method : POST
     * @throws Exception
     */
	@Test
	public void insertImgTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		ImgSingleParams single = new ImgSingleParams();
		single.setPoint_id(145076563281901L);
		single.setLevel(3);
		single.setAnswer("C");
		
		File file = new File("D:/img/demo.png");
		InputStream in = new FileInputStream(file);
		MockMultipartFile mockFile = new MockMultipartFile("questionFile", "demo.png", null, in);
		mockMvc.perform(fileUpload("/teacher/multiples/img").file(mockFile)
				   .session(mockSession)
				   .contentType(MediaType.MULTIPART_FORM_DATA)
				   .param("point_id", single.getPoint_id()+"")
				   .param("level", single.getLevel()+"")
				   .param("answer", single.getAnswer())
				   .characterEncoding(CharEncoding.UTF_8)
				   .accept(MediaType.APPLICATION_JSON)
				   .characterEncoding(CharEncoding.UTF_8)) 
					   .andExpect(jsonPath("$.errorCode").value(0))
					   .andDo(print());
	}
	
//	/**
//	 * 由于测试mock upload只支持post请求，只能测试post接口，实际环境为patch请求
//     * url : /teacher/multiples/upimg
//     * method : POST
//     * @throws Exception
//     */
//	@Test
//	public void updateImgTest() throws Exception{
//		User user = new User();
//		user.setUsername("Jerry");
//		mockSession.setAttribute("currentUser", user);
//		Multiple multiple = new Multiple();
//		multiple.setQuestion_id(145092967519101L);
//		multiple.setPoint_id(145076563281901L);
//		multiple.setLevel(5);
//		multiple.setAnswer("D");
//		
//		File file = new File("D:/img/demo.png");
//		InputStream in = new FileInputStream(file);
//		MockMultipartFile mockFile = new MockMultipartFile("questionFile", "demo.png", null, in);
//		
//		mockMvc.perform(fileUpload("/teacher/multiples/upimg")
//				   .file(mockFile)
//				   .session(mockSession)
//				   .contentType(MediaType.MULTIPART_FORM_DATA)
//				   .param("question_id", multiple.getQuestion_id()+"")
//				   .param("level", multiple.getLevel()+"")
//				   .param("answer", multiple.getAnswer())
//				   .characterEncoding(CharEncoding.UTF_8)
//				   .accept(MediaType.APPLICATION_JSON)
//				   .characterEncoding(CharEncoding.UTF_8)) 
//					   .andExpect(jsonPath("$.errorCode").value(0))
//					   .andDo(print());
//	}
	
	/**
	 * url : /teacher/multiples
	 * method : DELETE
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		Map<String,List<DELQuestionParams>> map = new HashMap<>();
		List<DELQuestionParams> list = new ArrayList<>();
		list.add(new DELQuestionParams(145092967519101L, 2));
		list.add(new DELQuestionParams(145092915956101L, 1));
		map.put("question", list);
		mockMvc.perform(delete("/teacher/multiples").session(mockSession)
											   .content(JSONUtils.convertObject2Json(map))
											   .contentType(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
}
