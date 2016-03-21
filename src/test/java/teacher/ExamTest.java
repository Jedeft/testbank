package teacher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.Date;

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
import com.ncu.testbank.teacher.data.params.ExamParams;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextHierarchy({
		@ContextConfiguration(name = "parent", locations = "classpath:spring-mybatis.xml"),
		@ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml") })
public class ExamTest {
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
	 * url : /teacher/exam method : POST
	 * 
	 * @throws Exception
	 */
	@Test
	public void createExamTest() throws Exception {
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);

		ExamParams params = new ExamParams();
		params.setTemplate_id(145802406445401L);
		params.setStart_time(new Timestamp(new Date().getTime()));
		params.setEnd_time(new Timestamp(new Date().getTime()));

		mockMvc.perform(
				post("/teacher/exam").session(mockSession)
						.contentType(MediaType.APPLICATION_JSON)
						.content(JSONUtils.convertObject2Json(params))
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
				.andExpect(jsonPath("$.errorCode").value(0)).andDo(print());
	}
	
	/**
	 * url : /teacher/exam/online method : GET
	 * 
	 * @throws Exception
	 */
	@Test
	public void searchOnlineTest() throws Exception {
		User user = new User();
		user.setUsername("Jerry");
		mockSession.setAttribute("currentUser", user);

		mockMvc.perform(
				get("/teacher/exam/online?page=1&rows=15&course_id=1")
						.session(mockSession)
						.contentType(MediaType.TEXT_HTML)
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
						.andExpect(status().isOk())
						.andDo(print());
	}
}
