package permission;

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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
// @WebAppConfiguration(value = "src/main/webapp")
// @ContextHierarchy({
// @ContextConfiguration(name = "parent", locations =
// "classpath:spring-mybatis.xml"),
// @ContextConfiguration(name = "child", locations =
// "classpath:spring-mvc.xml"),
// @ContextConfiguration(locations =
// "file:webapp/WEB-INF/springMVCForm-servlet.xml")
// })
@WebAppConfiguration
@ContextConfiguration({
// "file:src/main/webapp/WEB-INF/web.xml",
		"classpath:springTest.xml", "classpath:spring-mvc.xml" })
public class LoginTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	protected MockHttpSession mockSession;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

		// mockSession = new MockHttpSession();
	}

	/**
	 * url : /permission/json_web_token method : POST
	 * 
	 * @throws Exception
	 */
	@Test
	public void loginTest() throws Exception {
		// shiro无法运行
		// Factory<SecurityManager> factory = new
		// IniSecurityManagerFactory("classpath:shiro.ini");
		// // 创建SecurityManager (根据配置创建SecurityManager实例)
		// SecurityManager security = factory.getInstance();
		// SecurityUtils.setSecurityManager(security);
		StringBuilder requestBody = new StringBuilder().append("{");
		requestBody.append("\"username\":");
		requestBody.append("\"Jedeft\",");
		requestBody.append("\"password\":");
		requestBody.append("\"admin\"}");

		mockMvc.perform(
				post("/permission/json_web_token")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody.toString()).session(mockSession)
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
		// .andExpect(jsonPath("$.errorCode").value(0))
				.andDo(print());
	}

	/**
	 * url : /admin/majors method : PATCH
	 * 
	 * @throws Exception
	 */
	@Test
	public void updateTest() throws Exception {
		StringBuilder requestBody = new StringBuilder().append("{");
		requestBody.append("\"major_id\":");
		requestBody.append("\"1\",");
		// requestBody.append("\"academy_id\":");
		// requestBody.append("\"123\",");
		requestBody.append("\"name\":");
		requestBody.append("\"计算机软件\"}");

		mockMvc.perform(
				patch("/admin/majors").contentType(MediaType.APPLICATION_JSON)
						.content(requestBody.toString())
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
				.andExpect(jsonPath("$.errorCode").value(0)).andDo(print());
	}

	/**
	 * url : /admin/majors/{major_id} method : GET
	 * 
	 * @throws Exception
	 */
	@Test
	public void selectOneTest() throws Exception {
		mockMvc.perform(
				get("/admin/majors/{major_id}", 1)
						.contentType(MediaType.TEXT_HTML)
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
				.andExpect(status().isOk()).andDo(print());
	}

	/**
	 * url : /admin/majors/{major_id} method : DELETE
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws Exception {
		mockMvc.perform(
				delete("/admin/majors/{major_id}", 1)
						.contentType(MediaType.TEXT_HTML)
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
				.andExpect(status().isOk()).andDo(print());
	}

	/**
	 * url : /admin/majors/csv params : file method : POST
	 * 
	 * @throws Exception
	 */
	@Test
	public void loadCsvTest() throws Exception {
		File file = new File("E:/CSV/major.csv");
		InputStream in = new FileInputStream(file);
		MockMultipartFile mokeFile = new MockMultipartFile("file", "major.csv",
				null, in);
		mockMvc.perform(fileUpload("/admin/majors/csv").file(mokeFile)).andDo(
				print());
	}

	/**
	 * url : /admin/majors params : page=1 , rows=15 : major_id, academy_id,
	 * name method : GET
	 * 
	 * @throws Exception
	 */
	@Test
	public void searchTest() throws Exception {
		mockMvc.perform(
				get("/admin/majors/?page=1&rows=15&name=软件工程")
						.contentType(MediaType.TEXT_HTML)
						.characterEncoding(CharEncoding.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding(CharEncoding.UTF_8))
				.andExpect(status().isOk()).andDo(print());
	}

}
