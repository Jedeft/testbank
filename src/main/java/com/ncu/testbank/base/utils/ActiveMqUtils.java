package com.ncu.testbank.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

public class ActiveMqUtils {

	private static Logger log = Logger.getLogger("testbankLog");

	/**
	 * 发送消息入队列
	 * @param message
	 */
	public static void sendMessage(String message) {
		Properties properties = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("activemq.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			log.error(e);
		}

		// 链接工厂
		ConnectionFactory connectionFactory;
		// 创建链接
		Connection connection = null;
		// 创建一个session
		Session session;
		// 创建目的地
		Destination destination;
		// 消息提供者
		MessageProducer messageProducer;
		// 构造ConnectionFactory
		connectionFactory = new org.apache.activemq.ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,
				properties.getProperty("url"));

		// 设置用户名和密码，这个用户名和密码在conf目录下的credentials.properties文件中，也可以在activemq.xml中配置，我这里是默认的，所以就注释掉了
		// connectionFactory.setUserName("用户名");
		// connectionFactory.setPassword("密码");
		try {
			// 得到连接对象
			connection = connectionFactory.createConnection();
			// 启动链接
			connection.start();

			// 创建Session，参数解释：
			// 第一个参数是否使用事务:当消息发送者向消息提供者（即消息代理）发送消息时，消息发送者等待消息代理的确认，没有回应则抛出异常，消息发送程序负责处理这个错误。
			// 第二个参数消息的确认模式：
			// AUTO_ACKNOWLEDGE ：
			// 指定消息提供者在每次收到消息时自动发送确认。消息只向目标发送一次，但传输过程中可能因为错误而丢失消息。
			// CLIENT_ACKNOWLEDGE ：
			// 由消息接收者确认收到消息，通过调用消息的acknowledge()方法（会通知消息提供者收到了消息）
			// DUPS_OK_ACKNOWLEDGE ：
			// 指定消息提供者在消息接收者没有确认发送时重新发送消息（这种确认模式不在乎接收者收到重复的消息）。
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			// 获取服务商的消息
			destination = session.createQueue(properties
					.getProperty("queueName"));
			// 得到消息的发送者
			messageProducer = session.createProducer(destination);
			messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			sendMessage(session, messageProducer, message);
			session.commit();
		} catch (JMSException e) {
			// TODO 打印到日志，并且发送邮件给对应教师告诉组卷失败
			log.error(e);
		} finally {
			try {
				if (null != connection) {
					connection.close();
				}
			} catch (JMSException e) {
				log.error(e);
			}
		}

	}

	private static void sendMessage(Session session,
			MessageProducer messageProducer, String messages)
			throws JMSException {
		TextMessage message = session.createTextMessage(messages);
		// 发送消息到服务器
		messageProducer.send(message);
	}

	/**
	 * 接收一条消息
	 * @return
	 */
	public static String receiveMessage() {
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
		String text = null;
		try {
			// 创建链接
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 获取服务器上的消息
			destination = session.createQueue(properties
					.getProperty("queueName"));
			consumer = session.createConsumer(destination);
			TextMessage message = (TextMessage) consumer.receive(100000);
			text = message.getText();
		} catch (JMSException e) {
			log.error(e);
		} finally {
			try {
				if (null != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return text;
	}
}