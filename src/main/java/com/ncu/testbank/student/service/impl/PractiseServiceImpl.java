package com.ncu.testbank.student.service.impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.ncu.testbank.student.service.IPractiseService;
import com.ncu.testbank.teacher.data.Exam;
import com.ncu.testbank.teacher.data.Template;

@Service
public class PractiseServiceImpl implements IPractiseService {

	@Override
	public Exam createPractise(Template template, String student_id,
			Timestamp start, Timestamp end) {
		// TODO Auto-generated method stub
		return null;
	}

}
