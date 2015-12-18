package admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.codec.CharEncoding;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ncu.testbank.admin.data.BankBuilder;
import com.ncu.testbank.base.utils.JSONUtils;

/**
 * 对于需要rootAdmin权限的操作需要关掉@RequiresRoles("rootAdmin")注解才可执行
 * @author Jedeft
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextHierarchy({
		@ContextConfiguration(name = "parent", locations = "classpath:springTest.xml"),
		@ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml") 
})
public class BankBuilderTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	/**
	 * url : /admin/bankBuilders method : POST
	 * 
	 * @throws Exception
	 */
	@Test
	public void insertTest() throws Exception {
		BankBuilder bankBuilder = new BankBuilder();
		bankBuilder.setTeacher_id("1");
		bankBuilder.setCourse_id("1");
		

		mockMvc.perform(
				post("/admin/bankBuilders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(JSONUtils.convertObject2Json(bankBuilder))
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
				.andExpect(jsonPath("$.errorCode").value(0)).andDo(print());
	}

	/**
	 * url : /admin/bankBuilders method : DELETE
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws Exception {
		BankBuilder bankBuilder = new BankBuilder();
		bankBuilder.setTeacher_id("1");
		bankBuilder.setCourse_id("1");
		
		mockMvc.perform(
				delete("/admin/bankBuilders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(JSONUtils.convertObject2Json(bankBuilder))
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
				.andExpect(status().isOk()).andDo(print());
	}


	/**
	 * url : /admin/bankBuilders 
	 * params : page=1, rows=15, academy_id, teacher_id, teacher_name, course_id, course_name 
	 * method : GET
	 * @throws Exception
	 */
	@Test
	public void searchTest() throws Exception {
		mockMvc.perform(get("/admin/bankBuilders/?page=1&rows=15")
						.contentType(MediaType.TEXT_HTML)
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
				.andExpect(status().isOk()).andDo(print());
	}

}
