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
import org.junit.After;
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
import com.ncu.testbank.teacher.data.Point;
import com.ncu.testbank.teacher.data.params.PointParams;

/**
 * 对于需要rootAdmin权限的操作需要关掉@RequiresRoles("rootAdmin")注解才可执行
 * 
 * @author Jedeft
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextHierarchy({
		@ContextConfiguration(name = "parent", locations = "classpath:spring-mybatis.xml"),
		@ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml") })
public class PointTest {

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
	 * url : /teacher/points method : POST
	 * 
	 * @throws Exception
	 */
	@Test
	public void insertTest() throws Exception {
		Point point = new Point();
		point.setCourse_id("1");
		point.setName("单元测试！");

		mockMvc.perform(
				post("/teacher/points").session(mockSession)
						.contentType(MediaType.APPLICATION_JSON)
						.content(JSONUtils.convertObject2Json(point))
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
				.andExpect(jsonPath("$.errorCode").value(0)).andDo(print());
	}

	/**
	 * url : /teacher/points/{course_id} method : GET
	 * 
	 * @throws Exception
	 */
	@Test
	public void searchTest() throws Exception {
		mockMvc.perform(
				get("/teacher/points/{course_id}", "1")
						.contentType(MediaType.TEXT_HTML)
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
				.andExpect(status().isOk()).andDo(print());
	}

	/**
	 * url : /teacher/points method : PATCH
	 * 
	 * @throws Exception
	 */
	@Test
	public void updateTest() throws Exception {
		PointParams point = new PointParams();
		point.setPoint_id(145067967143701L);
		point.setName("update ");

		mockMvc.perform(
				patch("/teacher/points")
						.content(JSONUtils.convertObject2Json(point))
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
				.andExpect(status().isOk()).andDo(print());
	}

	/**
	 * url : /teacher/points method : DELETE
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws Exception {
		Map<String, List<Long>> map = new HashMap<>();
		List<Long> point_id = new ArrayList<>();
		point_id.add(145067958396301L);
		map.put("point_id", point_id);

		mockMvc.perform(
				delete("/teacher/points")
						.content(JSONUtils.convertObject2Json(map))
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
				.andExpect(status().isOk()).andDo(print());
	}

}
