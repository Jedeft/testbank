package common;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ncu.testbank.base.utils.ActiveMqUtils;
import com.ncu.testbank.base.utils.EmailUtils;
import com.ncu.testbank.base.utils.HttpClientUtils;
import com.ncu.testbank.base.utils.JWTUtils;
import com.ncu.testbank.teacher.service.IExamService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-mybatis.xml" })
public class SpringTest {

	// 第一部分 {alg=HS256} 第二部分{sub=Joe}
	// 先验证token的合法性，之后再做用户信息验证
	// 每个token放在redis里面做缓存
	// 若没有请求，那么定期清理redis缓存
	@Test
	public void testJWT() {
		String token = JWTUtils.createToken("Jedeft");
		System.out.println(JWTUtils.validateToken(token, "Jedeft"));
		List<String> list = new ArrayList<>();
		list.toString();
	}

	@Test
	public void testJedis() {
		// JedisPool jedisPool = JedisPoolUtils.getPool();
		// Jedis jedis = jedisPool.getResource();
		//
		// User user = new User();
		// user.setName("jedeft");
		// user.setUsername("admin");
		// user.setPassword("admin");
		// user.setRole_id(1);
		//
		// String json = JSONUtils.convertObject2Json(user);
		// jedis.setex("user", 20, json);
		//
		// //归还连接池
		// JedisPoolUtils.returnResource(jedisPool, jedis);
		// userService.createToken("admin");
	}

	@Test
	public void testActiveMq() {
		for (int i = 0; i < 100; i++) {
			ActiveMqUtils.sendMessage("这是第" + i + "条消息！");
		}
	}

	@Test
	public void testReciver() {
		// 创建工厂
		ConnectionFactory connectionFactory;
		// 创建connection
		Connection connection = null;
		// 创建session
		Session session;
		// 创建目的地
		Destination destination;
		// 消费者
		MessageConsumer consumer;
		// 得到工厂
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,
				"tcp://121.42.216.103:61616");
		try {
			// 创建链接
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 获取服务器上的消息
			destination = session.createQueue("examQueue");
			consumer = session.createConsumer(destination);
			while (true) {
				TextMessage message = (TextMessage) consumer.receive(100000);
				if (null != message) {
					System.out.println(message.getText());
				} else {
					break;
				}
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Autowired
	private IExamService examService;

	@Test
	public void createExamTest() {
		examService.createExam(123L, "Jerry",
				new Timestamp(new Date().getTime()),
				new Timestamp(new Date().getTime()));
	}
	
	@Test
	public void httpclientTest() {
		System.out.println(EmailUtils.sendEmail("Jedeft@163.com", "测试邮件1"));
		System.out.println(EmailUtils.sendEmail("Jedeft@163.com", "测试邮件2"));
	}
}
