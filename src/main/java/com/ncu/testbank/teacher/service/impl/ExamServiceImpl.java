package com.ncu.testbank.teacher.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ncu.testbank.base.utils.ExamUtils;
import com.ncu.testbank.base.utils.RandomID;
import com.ncu.testbank.base.utils.RandomUtils;
import com.ncu.testbank.teacher.dao.IExamDao;
import com.ncu.testbank.teacher.dao.IJudgeDao;
import com.ncu.testbank.teacher.dao.IMultipleDao;
import com.ncu.testbank.teacher.dao.IShortAnswerDao;
import com.ncu.testbank.teacher.dao.ISingleDao;
import com.ncu.testbank.teacher.dao.ITemplateDao;
import com.ncu.testbank.teacher.data.Exam;
import com.ncu.testbank.teacher.data.Question;
import com.ncu.testbank.teacher.data.Template;
import com.ncu.testbank.teacher.service.IExamService;

public class ExamServiceImpl implements IExamService{
	
	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private ITemplateDao templateDao;
	
	@Autowired
	private IExamDao examDao;
	
	@Autowired
	private ISingleDao singleDao;
	
	@Autowired
	private IMultipleDao multipleDao;
	
	@Autowired
	private IJudgeDao judgeDao;
	
	@Autowired
	private IShortAnswerDao shortAnswerDao;
	
	@Override
	public void createExam(long template_id, String user_id, Timestamp start,
			Timestamp end) {
		Template template = templateDao.getOne(template_id);
		Exam exam = new Exam(RandomID.getID(), template_id, start, end, user_id);
		
		if (examDao.insertOne(exam) < 1) {
			log.error(user_id+"使用者组卷失败！原因：插入exam试卷表出错！");
			//TODO 短信或邮件通知教师组卷失败
			return;
		}
		//对应难度题目数量
		Integer hardCount = 0;
		Integer mediumCount = 0;
		Integer easyCount = 0;
		
		//对应档次难度等级
		Integer hard = 0;
		Integer medium = 0;
		Integer easy = 0;
		ExamUtils.initLevel(template.getLevel(), hard, medium, easy);
		
		if (template.getSingle_num() > 0) {
			hardCount = 0;
			mediumCount = 0;
			easyCount = 0;
			ExamUtils.initCount(template.getSingle_num(), template, hardCount, mediumCount, easyCount);
			
			List<Question> list = singleDao.searchByCourse(template.getCourse_id());
			Map<Long, List<Question>> hardMap = new HashMap<>();
			Map<Long, List<Question>> mediumMap = new HashMap<>();
			Map<Long, List<Question>> easyMap = new HashMap<>();
			
			for (Question question : list) {
				question.setTest_id(exam.getExam_id());
				if (question.getLevel() == hard) {
					if ( !hardMap.containsKey(question.getPoint_id()) ) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						hardMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = hardMap.get(question.getPoint_id());
						questions.add(question);
						hardMap.put(question.getPoint_id(), questions);
					}
				} else if (question.getLevel() == medium) {
					if ( !mediumMap.containsKey(question.getPoint_id()) ) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						mediumMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = mediumMap.get(question.getPoint_id());
						questions.add(question);
						mediumMap.put(question.getPoint_id(), questions);
					}
				} else if (question.getLevel() <= easy) { 
					if ( !easyMap.containsKey(question.getPoint_id()) ) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						easyMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = easyMap.get(question.getPoint_id());
						questions.add(question);
						easyMap.put(question.getPoint_id(), questions);
					}
				}
			}
			
			List<Long> hardPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : hardMap.entrySet()) {
				hardPoints.add(entry.getKey());
			}
			List<Long> mediumPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : mediumMap.entrySet()) {
				mediumPoints.add(entry.getKey());
			}
			List<Long> easyPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				easyPoints.add(entry.getKey());
			}
			
			//最高难度题目组卷
			int total = 0; //对应难度题目总数量
			for (Entry<Long, List<Question>> entry : hardMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			//最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (hardCount > total) {
				mediumCount += (total - hardCount);
				hardCount = total;
			}
			if (hardCount == 1) {
				//只有一道题目时，随机出题
				int random = RandomUtils.random(0, hardPoints.size());
				long point_id = hardPoints.get(random);
				List<Question> questions = hardMap.get(point_id);
				Question question = questions.get(RandomUtils.random(0, questions.size()));
				if (examDao.insertSingle(question) < 1) {
					log.error(user_id + "组卷失败！原因：题目录入single_exam表中失败。");
					//TODO 发送邮件告知教师组卷失败
					examDao.deleteOne(exam.getExam_id());
					return;
				}
			} else if (hardCount > 1 && hardCount <= hardPoints.size()) {
				//出题数量大于1，但是小于知识点数量，保证每个题目考点不同
				for (int i = 0; i < hardCount; i++) {
					List<Question> questions = hardMap.get(hardPoints.get(i));
					Question question = questions.get(RandomUtils.random(0, questions.size()));
					if (examDao.insertSingle(question) < 1) {
						log.error(user_id + "组卷失败！原因：题目录入single_exam表中失败。");
						//TODO 发送邮件告知教师组卷失败
						examDao.deleteOne(exam.getExam_id());
						return;
					}
				}
			} else if (hardCount > hardPoints.size()) {
				//出题数量大于题目考点数量，需要检测是否有重题，反复组卷。
				//TODO 三次依然出现重题失败组卷，那么发送邮件告知教师组卷失败
				int flag = 0;
				//已经添加的题目数量
				int count = 0;
				List<Long> question_id = new ArrayList<>();
				while (flag > 3 || count == hardCount) {
					//循环出卷
					for (int i = 0; i < hardPoints.size(); i++) {
						List<Question> questions = hardMap.get(hardPoints.get(i));
						Question question = questions.get(RandomUtils.random(0, questions.size()));
						if (count == 0) {
							if ( examDao.insertSingle(question) < 1) {
								log.error(user_id + "组卷失败！原因：题目录入single_exam表中失败。");
								//TODO 发送邮件告知教师组卷失败
								examDao.deleteOne(exam.getExam_id());
								return;
							}
							question_id.add(question.getQuestion_id());
						} else {
							//查找已经完成组卷的题目是否出现重复
							if (!question_id.contains(question.getQuestion_id())) {
								if ( examDao.insertSingle(question) < 1) {
									log.error(user_id + "组卷失败！原因：题目录入single_exam表中失败。");
									//TODO 发送邮件告知教师组卷失败
									examDao.deleteOne(exam.getExam_id());
									return;
								}
								question_id.add(question.getQuestion_id());
							}
						}
						count++;
					}
					flag++;
				}
				if (flag > 3) {
					log.error(user_id + "组卷失败！原因：可能由于题库题目数量太少，导致重组试卷超过3次以上！");
					//TODO 发送邮件告知教师组卷失败
					examDao.deleteOne(exam.getExam_id());
					return;
				}
			}
			
			//次之难度题目组卷
			total = 0; //对应难度题目总数量
			for (Entry<Long, List<Question>> entry : mediumMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			//最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (mediumCount > total) {
				easyCount += (total - mediumCount);
				mediumCount = total;
			}
			if (mediumCount == 1) {
				//只有一道题目时，随机出题
				int random = RandomUtils.random(0, mediumPoints.size());
				long point_id = mediumPoints.get(random);
				List<Question> questions = mediumMap.get(point_id);
				Question question = questions.get(RandomUtils.random(0, questions.size()));
				if (examDao.insertSingle(question) < 1) {
					log.error(user_id + "组卷失败！原因：题目录入single_exam表中失败。");
					//TODO 发送邮件告知教师组卷失败
					examDao.deleteOne(exam.getExam_id());
					return;
				}
			} else if (mediumCount > 1 && mediumCount <= mediumPoints.size()) {
				//出题数量大于1，但是小于知识点数量，保证每个题目考点不同
				for (int i = 0; i < mediumCount; i++) {
					List<Question> questions = mediumMap.get(mediumPoints.get(i));
					Question question = questions.get(RandomUtils.random(0, questions.size()));
					if (examDao.insertSingle(question) < 1) {
						log.error(user_id + "组卷失败！原因：题目录入single_exam表中失败。");
						//TODO 发送邮件告知教师组卷失败
						examDao.deleteOne(exam.getExam_id());
						return;
					}
				}
			} else if (mediumCount > mediumPoints.size()) {
				//出题数量大于题目考点数量，需要检测是否有重题，反复组卷。
				//TODO 三次依然出现重题失败组卷，那么发送邮件告知教师组卷失败
				int flag = 0;
				//已经添加的题目数量
				int count = 0;
				List<Long> question_id = new ArrayList<>();
				while (flag > 3 || count == mediumCount) {
					//循环出卷
					for (int i = 0; i < mediumPoints.size(); i++) {
						List<Question> questions = mediumMap.get(hardPoints.get(i));
						Question question = questions.get(RandomUtils.random(0, questions.size()));
						if (count == 0) {
							if ( examDao.insertSingle(question) < 1) {
								log.error(user_id + "组卷失败！原因：题目录入single_exam表中失败。");
								//TODO 发送邮件告知教师组卷失败
								examDao.deleteOne(exam.getExam_id());
								return;
							}
							question_id.add(question.getQuestion_id());
						} else {
							//查找已经完成组卷的题目是否出现重复
							if (!question_id.contains(question.getQuestion_id())) {
								if ( examDao.insertSingle(question) < 1) {
									log.error(user_id + "组卷失败！原因：题目录入single_exam表中失败。");
									//TODO 发送邮件告知教师组卷失败
									examDao.deleteOne(exam.getExam_id());
									return;
								}
								question_id.add(question.getQuestion_id());
							}
						}
						count++;
					}
					flag++;
				}
				if (flag > 3) {
					log.error(user_id + "组卷失败！原因：可能由于题库题目数量太少，导致重组试卷超过3次以上！");
					//TODO 发送邮件告知教师组卷失败
					examDao.deleteOne(exam.getExam_id());
					return;
				}
			}
			
			//最低难度题目组卷
			total = 0; //对应难度题目总数量
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			//最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (easyCount > total) {
				log.error(user_id + "组卷失败！原因：题库题目数量过少，无法完成组卷！");
				//TODO 发送邮件告知教师组卷失败
				examDao.deleteOne(exam.getExam_id());
				return;
			}
			if (easyCount == 1) {
				//只有一道题目时，随机出题
				int random = RandomUtils.random(0, easyPoints.size());
				long point_id = easyPoints.get(random);
				List<Question> questions = easyMap.get(point_id);
				Question question = questions.get(RandomUtils.random(0, questions.size()));
				if (examDao.insertSingle(question) < 1) {
					log.error(user_id + "组卷失败！原因：题目录入single_exam表中失败。");
					//TODO 发送邮件告知教师组卷失败
					examDao.deleteOne(exam.getExam_id());
					return;
				}
			} else if (easyCount > 1 && easyCount <= easyPoints.size()) {
				//出题数量大于1，但是小于知识点数量，保证每个题目考点不同
				for (int i = 0; i < easyCount; i++) {
					List<Question> questions = easyMap.get(easyPoints.get(i));
					Question question = questions.get(RandomUtils.random(0, questions.size()));
					if (examDao.insertSingle(question) < 1) {
						log.error(user_id + "组卷失败！原因：题目录入single_exam表中失败。");
						//TODO 发送邮件告知教师组卷失败
						examDao.deleteOne(exam.getExam_id());
						return;
					}
				}
			} else if (easyCount > easyPoints.size()) {
				//出题数量大于题目考点数量，需要检测是否有重题，反复组卷。
				//TODO 三次依然出现重题失败组卷，那么发送邮件告知教师组卷失败
				int flag = 0;
				//已经添加的题目数量
				int count = 0;
				List<Long> question_id = new ArrayList<>();
				while (flag > 3 || count == easyCount) {
					//循环出卷
					for (int i = 0; i < easyPoints.size(); i++) {
						List<Question> questions = easyMap.get(hardPoints.get(i));
						Question question = questions.get(RandomUtils.random(0, questions.size()));
						if (count == 0) {
							if ( examDao.insertSingle(question) < 1) {
								log.error(user_id + "组卷失败！原因：题目录入single_exam表中失败。");
								//TODO 发送邮件告知教师组卷失败
								examDao.deleteOne(exam.getExam_id());
								return;
							}
							question_id.add(question.getQuestion_id());
						} else {
							//查找已经完成组卷的题目是否出现重复
							if (!question_id.contains(question.getQuestion_id())) {
								if ( examDao.insertSingle(question) < 1) {
									log.error(user_id + "组卷失败！原因：题目录入single_exam表中失败。");
									//TODO 发送邮件告知教师组卷失败
									examDao.deleteOne(exam.getExam_id());
									return;
								}
								question_id.add(question.getQuestion_id());
							}
						}
						count++;
					}
					flag++;
				}
				if (flag > 3) {
					log.error(user_id + "组卷失败！原因：可能由于题库题目数量太少，导致重组试卷超过3次以上！");
					//TODO 发送邮件告知教师组卷失败
					examDao.deleteOne(exam.getExam_id());
					return;
				}
			}
		} 
		if (template.getMultiple_num() > 0) {
			//TODO 多选题智能组卷过程
		} 
		if (template.getJudge_num() >0) {
			//TODO 判断题智能组卷过程
		} 
		if (template.getShortAnswer_num() > 0) {
			//TODO 简答题智能组卷过程
		}
		
		
	}

}
