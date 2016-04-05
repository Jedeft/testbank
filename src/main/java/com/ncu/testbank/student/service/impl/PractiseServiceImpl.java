package com.ncu.testbank.student.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ncu.testbank.student.service.IPractiseService;
import com.ncu.testbank.teacher.data.Exam;
import com.ncu.testbank.teacher.data.Template;
import com.ncu.testbank.teacher.data.view.ExamPaperView;
import com.ncu.testbank.teacher.data.view.OnlineExamView;

@Service
public class PractiseServiceImpl implements IPractiseService {

	@Override
	public Exam createPractise(Template template, String student_id,
			Timestamp start, Timestamp end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OnlineExamView> searchBySID(String student_id, String course_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExamPaperView getExamByID(Long exam_id) {
		// TODO Auto-generated method stub
		return null;
	}

}
