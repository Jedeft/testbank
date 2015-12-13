package permission;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ContextConfiguration({
// "classpath:beans.xml",
		"classpath:springTest.xml", "classpath:spring-mvc.xml" })
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ShiroUnitTest extends AbstractShiroTest {

	@Autowired
	private WebApplicationContext ctx;

	private MockMvc mockMvc;

	private Subject subjectUnderTest;

	protected MockHttpSession mockSession;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
		// final Authenticate bean = (Authenticate) ctx.getBean("authenticate");
		// 1. Create a mock authenticated Subject instance for the test to run:
		subjectUnderTest = new Subject.Builder(getSecurityManager())
				.buildSubject();// Mockito.mock(Subject.class);
		// expect(subjectUnderTest.isAuthenticated()).andReturn(true);

		mockSession = new MockHttpSession(ctx.getServletContext(),
				subjectUnderTest.getSession().getId().toString());
		SecurityManager securityManger = getSecurityManager();
		ThreadContext.bind(securityManger);

		// 2. Bind the subject to the current thread:
		setSubject(subjectUnderTest);
		// bean.logon("User01", "User01", mockSession.getId());

		// 2. Bind the subject to the current thread:
		setSubject(subjectUnderTest);

		// perform test logic here. Any call to
		// SecurityUtils.getSubject() directly (or nested in the
		// call stack) will work properly.
	}

	@Test
	public void testShowHome() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/permission/json_web_token")
						.session(mockSession)).andExpect(status().isOk());
	}

	@After
	public void detachSubject() {
		subjectThreadState.clear();
	}
}
