package com.ncu.testbank.student.data.params;

public class StudentAnswerParams {
	// 考试、练习ID
	private Long test_id;
	private QuestionParams shortAnswerParams;

	public Long getTest_id() {
		return test_id;
	}

	public void setTest_id(Long test_id) {
		this.test_id = test_id;
	}

	public QuestionParams getShortAnswerParams() {
		return shortAnswerParams;
	}

	public void setShortAnswerParams(QuestionParams shortAnswerParams) {
		this.shortAnswerParams = shortAnswerParams;
	}

}
