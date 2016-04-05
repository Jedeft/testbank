package com.ncu.testbank.background;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ncu.testbank.admin.data.Teacher;
import com.ncu.testbank.admin.service.ITeacherService;
import com.ncu.testbank.base.utils.EmailUtils;
import com.ncu.testbank.base.utils.JSONUtils;
import com.ncu.testbank.teacher.data.Exam;
import com.ncu.testbank.teacher.data.Template;
import com.ncu.testbank.teacher.data.params.ExamParams;
import com.ncu.testbank.teacher.service.IExamService;
import com.ncu.testbank.teacher.service.ITemplateService;

/**
 * 后台智能组卷程序
 * 
 * @author Jedeft
 */
//TODO 后台需要重构，用quartz框架做作业调度
public class ExamExecutor {

	@Autowired
	private static IExamService examService;

	@Autowired
	private static ITemplateService templateService;

	@Autowired
	private static ITeacherService teacherService;

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
					String json = message.getText();
					ExamParams examParams = JSONUtils.convertJson2Object(json,
							ExamParams.class);
					Template template = templateService.getOne(examParams
							.getTemplate_id());
					List<String> students = examParams.getStudent_id();
					List<String> failStudents = new ArrayList<>();
					for (String student_id : students) {
						Exam exam = examService.createExam(template,
								student_id, examParams.getStart_time(),
								examParams.getEnd_time());
						// 若组卷失败，那么返回null
						if (exam == null) {
							failStudents.add(student_id);
						}
					}
					if (failStudents != null && failStudents.size() > 0) {
						Teacher teacher = teacherService.getTeacher(examParams
								.getTeacher_id());
						EmailUtils.sendEmail(teacher.getEmail(), "组卷失败的同学学号为："
								+ failStudents.toString() + "；请及时重新组卷，避免影响考试！");
					}
				} else {
					break;
				}
			}
		} catch (JMSException e) {
			// TODO 后台程序崩溃，邮件通知管理员~
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
