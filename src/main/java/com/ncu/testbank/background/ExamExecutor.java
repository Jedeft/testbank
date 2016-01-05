package com.ncu.testbank.background;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 后台智能组卷程序
 * @author Jedeft
 */
public class ExamExecutor {

	public static ExamExecutor executor = null;

	private static Logger log = Logger.getLogger("testbankLog");

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"spring4background.xml");
		executor = (ExamExecutor) ac.getBean("examExecutor");

		Properties properties = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("activemq.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			log.error(e);
		}

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
				properties.getProperty("url"));
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
			// TODO 组卷失败通知
			log.error(e);
		} finally {
			try {
				if (null != null) {
					connection.close();
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

}
