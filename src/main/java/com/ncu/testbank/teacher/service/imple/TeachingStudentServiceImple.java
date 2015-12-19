package com.ncu.testbank.teacher.service.imple;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncu.testbank.admin.dao.IStudentDao;
import com.ncu.testbank.admin.dao.ISyllabusDao;
import com.ncu.testbank.admin.data.Student;
import com.ncu.testbank.admin.data.Syllabus;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;
import com.ncu.testbank.teacher.dao.ITeachingStudentDao;
import com.ncu.testbank.teacher.data.TeachingStudent;
import com.ncu.testbank.teacher.data.view.TeachingStudentView;
import com.ncu.testbank.teacher.service.ITeachingStudentService;

@Service("teachingStudentService")
public class TeachingStudentServiceImple implements ITeachingStudentService {

	@Autowired
	private ISyllabusDao syllabusDao;

	@Autowired
	private ITeachingStudentDao teachingStudentDao;

	@Autowired
	private IStudentDao studentDao;

	@Override
	public void insertStudents(String teacher_id, String course_id,
			List<String> list) {
		// 1.通过教师ID、课程ID以及当下时间来确定出课程表syllabus_id
		Map<String, Object> params = new HashMap<>();
		params.put("teacher_id", teacher_id);
		params.put("course_id", course_id);
		params.put("today", new Date(new java.util.Date().getTime()));
		Syllabus syllabus = syllabusDao.getSyllabusNow(params);

		List<String> failList = new ArrayList<>();
		// syllabus_id和student_id插入到授课表中
		for (String student_id : list) {
			TeachingStudent teachingStudent = new TeachingStudent(
					syllabus.getSyllabus_id(), student_id);
			if (teachingStudentDao.insertOne(teachingStudent) < 1) {
				// 插入失败的学生ID
				failList.add(student_id);
			}
		}
		if (failList != null && failList.size() > 0) {
			List<Student> studentList = studentDao.getStudents(failList);
			StringBuilder sb = new StringBuilder()
					.append("以下学生加入课程失败，请联系管理人员：");
			for (int i = 0; i < studentList.size(); i++) {
				if (i != studentList.size() - 1) {
					sb.append(studentList.get(i).getName() + ",");
				} else {
					sb.append(studentList.get(i).getName());
				}
			}
			throw new ServiceException(new ErrorCode(30006, sb.toString()));
		}
	}

	@Override
	public List<TeachingStudentView> searchData(PageInfo page,
			TeachingStudentView teachingStudentView, String teacher_id)
			throws IllegalAccessException, InvocationTargetException,
			IntrospectionException {
		
		// 1.通过教师ID、课程ID以及当下时间来确定出课程表syllabus_id
		Map<String, Object> params = new HashMap<>();
		params.put("teacher_id", teacher_id);
		params.put("course_id", teachingStudentView.getCourse_id());
		params.put("today", new Date(new java.util.Date().getTime()));
		Syllabus syllabus = syllabusDao.getSyllabusNow(params);
		
		params.clear();
		params.put("syllabus_id", syllabus.getSyllabus_id());
		params.put("student_id", teachingStudentView.getStudent_id());
		params.put("student_name", teachingStudentView.getStudent_name());
		
		//2.通过syllabus_id和当下时间以及其他检索信息查询授课学生
		int count = teachingStudentDao.getCount(params);
		
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		}
		page.setTotalPage(count / page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的学生！"));
		}
		// 数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return teachingStudentDao.searchData(params);
	}

}
