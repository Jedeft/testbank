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
import com.ncu.testbank.teacher.data.ShortAnswer;
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
public class ShortAnswerTest {
	
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
     * url : /teacher/shortAnswers/writing
     * method : POST
     * @throws Exception
     */
	@Test
	public void insertWritingTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		ShortAnswer judge = new ShortAnswer();
		judge.setPoint_id(145076563281901L);
		judge.setQuestion("这是一道大题！！！！！");
		judge.setLevel(3);
		judge.setAnswer("这是大题的答案！！！！！");
		
		mockMvc.perform(post("/teacher/shortAnswers/writing").session(mockSession)
											   .contentType(MediaType.APPLICATION_JSON)
											   .content(JSONUtils.convertObject2Json(judge))
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
     * url : /teacher/shortAnswers/writing
     * method : PATCH
     * @throws Exception
     */
	@Test
	public void updateWritingTest() throws Exception{
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);
		ShortAnswer shortAnswer = new ShortAnswer();
		shortAnswer.setQuestion_id(145096077232801L);
		shortAnswer.setQuestion("update question!");
		shortAnswer.setLevel(5);
		
		mockMvc.perform(patch("/teacher/shortAnswers/writing")
											   .session(mockSession)
											   .contentType(MediaType.APPLICATION_JSON)
											   .content(JSONUtils.convertObject2Json(shortAnswer))
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(jsonPath("$.errorCode").value(0))
										  .andDo(print());
	}
	
	/**
	 * url : /teacher/shortAnswers
	 * params : page=1 , rows=15, point_id
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void searchShortAnswerTest() throws Exception{
		mockMvc.perform(get("/teacher/shortAnswers/?page=1&rows=15&point_id=145076563281901").contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
	 * url : /teacher/shortAnswers/{question_id}
	 * params : question_id
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void getShortAnswerTest() throws Exception{
		mockMvc.perform(get("/teacher/shortAnswers/{question_id}", 145096077232801L).contentType(MediaType.TEXT_HTML)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
	
	/**
     * url : /teacher/shortAnswers/img
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
		single.setAnswer("Y");
		
		File questionFile = new File("D:/img/demo.png");
		File answerFile = new File("D:/img/answer.png");
		InputStream inQuestion = new FileInputStream(questionFile);
		InputStream inAnswer = new FileInputStream(answerFile);
		MockMultipartFile mockQuestionFile = new MockMultipartFile("questionFile", "demo.png", null, inQuestion);
		MockMultipartFile mockAnswerFile = new MockMultipartFile("answerFile", "answer.png", null, inAnswer);
		mockMvc.perform(fileUpload("/teacher/shortAnswers/img").file(mockQuestionFile).file(mockAnswerFile)
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
//     * url : /teacher/shortAnswers/upimg
//     * method : POST
//     * @throws Exception
//     */
//	@Test
//	public void updateImgTest() throws Exception{
//		User user = new User();
//		user.setUsername("Jerry");
//		mockSession.setAttribute("currentUser", user);
//		ShortAnswer shortAnswer = new ShortAnswer();
//		shortAnswer.setQuestion_id(145096200375801L);
//		shortAnswer.setPoint_id(145076563281901L);
//		shortAnswer.setLevel(5);
//		
//		File questionFile = new File("D:/img/answer.png");
//		File answerFile = new File("D:/img/demo.png");
//		InputStream inQuestion = new FileInputStream(questionFile);
//		InputStream inAnswer = new FileInputStream(answerFile);
//		MockMultipartFile mockQuestionFile = new MockMultipartFile("questionFile", "demo.png", null, inQuestion);
//		MockMultipartFile mockAnswerFile = new MockMultipartFile("answerFile", "answer.png", null, inAnswer);
//		
//		mockMvc.perform(fileUpload("/teacher/shortAnswers/upimg")
//				   .file(mockQuestionFile)
//				   .file(mockAnswerFile)
//				   .session(mockSession)
//				   .contentType(MediaType.MULTIPART_FORM_DATA)
//				   .param("question_id", shortAnswer.getQuestion_id()+"")
//				   .param("level", shortAnswer.getLevel()+"")
//				   .param("answer", shortAnswer.getAnswer())
//				   .characterEncoding(CharEncoding.UTF_8)
//				   .accept(MediaType.APPLICATION_JSON)
//				   .characterEncoding(CharEncoding.UTF_8)) 
//					   .andExpect(jsonPath("$.errorCode").value(0))
//					   .andDo(print());
//	}
//	
	/**
	 * url : /teacher/shortAnswers
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
		list.add(new DELQuestionParams(145096200375801L, 2));
		list.add(new DELQuestionParams(145096077232801L, 1));
		map.put("question", list);
		mockMvc.perform(delete("/teacher/shortAnswers").session(mockSession)
											   .content(JSONUtils.convertObject2Json(map))
											   .contentType(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8)
											   .accept(MediaType.APPLICATION_JSON)
											   .characterEncoding(CharEncoding.UTF_8))
										  .andExpect(status().isOk())
										  .andDo(print());
	}
}
