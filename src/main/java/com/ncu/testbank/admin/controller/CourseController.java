package com.ncu.testbank.admin.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.data.Course;
import com.ncu.testbank.admin.service.ICourseService;
import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.base.response.ResponseQueryMsg;

@RestController
@RequestMapping("/admin")
public class CourseController {

	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private ICourseService courseService;

	/**
	 * 新增course
	 * 
	 * @param course
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/courses", method = RequestMethod.POST)
	public ResponseMsg insertCourse(@RequestBody Course course) {
		ResponseMsg msg = new ResponseMsg();
		try {
			courseService.insertOne(course);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = course;
		} catch (ShiroException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (ServiceException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (DaoException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		}
		return msg;
	}

	/**
	 * 更新course
	 * 
	 * @param course
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/courses", method = RequestMethod.PATCH)
	public ResponseMsg updateCourse(@RequestBody Course course) {
		ResponseMsg msg = new ResponseMsg();
		try {

			courseService.updateOne(course);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = courseService.getCourse(course.getCourse_id());
		} catch (ShiroException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (ServiceException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (DaoException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		}
		return msg;
	}

	/**
	 * 删除course
	 * 
	 * @param course
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/courses/{course_id}", method = RequestMethod.DELETE)
	public ResponseMsg deleteCourse(@PathVariable String course_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			courseService.deleteOne(course_id);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;

		} catch (ShiroException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (ServiceException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (DaoException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		}
		return msg;
	}

	/**
	 * 批量删除course
	 * 
	 * @param course
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/courses/batch", method = RequestMethod.DELETE)
	public ResponseMsg deleteCourses(@RequestBody Map<String, List<String>> map) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (map.get("course_id") != null) {
				courseService.deleteData(map.get("course_id"));
			}
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
		} catch (ShiroException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (ServiceException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (DaoException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		}
		return msg;
	}

	/**
	 * 根据id获取指定course
	 * 
	 * @param course
	 * @return
	 */
	@RequestMapping(value = "/courses/{course_id}", method = RequestMethod.GET)
	public ResponseMsg getCourse(@PathVariable String course_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			Course data = courseService.getCourse(course_id);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = data;
		} catch (ShiroException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (ServiceException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (DaoException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		}
		return msg;
	}

	/**
	 * 分页获取course信息
	 * 
	 * @param course
	 * @return
	 */
	@RequestMapping(value = "/courses", method = RequestMethod.GET)
	public ResponseQueryMsg searchData(PageInfo page, Course course) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			List<Course> courseList;
			courseList = courseService.searchData(page, course);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = courseList;

			msg.total = page.getTotal();
			msg.totalPage = page.getTotalPage();
			msg.currentPage = page.getPage();
			msg.pageCount = courseList.size();
		} catch (ShiroException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (ServiceException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (DaoException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (IllegalAccessException | InstantiationException
				| InvocationTargetException | IntrospectionException e) {
			msg.errorCode = ErrorCode.MAP_CONVERT_ERROR.code;
			msg.msg = ErrorCode.MAP_CONVERT_ERROR.name;
			log.error(e.getMessage());
		}
		return msg;
	}

	/**
	 * csv文件批量录入course信息
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/courses/csv", method = RequestMethod.POST)
	public ResponseMsg loadCsv(
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		try {

			String fileName = new Date().getTime() + "_"
					+ file.getOriginalFilename();
			String path = request.getSession().getServletContext()
					.getRealPath("upload");

			if (!fileName.endsWith(".csv")) {
				msg.errorCode = ErrorCode.FILE_TYPE_ERROR.code;
				msg.msg = ErrorCode.FILE_TYPE_ERROR.name;
				return msg;
			}
			courseService.loadCsv(fileName, path, file);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
		} catch (ShiroException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (ServiceException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (DaoException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		} catch (IOException e) {
			msg.errorCode = ErrorCode.FILE_IO_ERROR.code;
			msg.msg = ErrorCode.FILE_IO_ERROR.name;
			log.error(e.getMessage());
		} catch (IllegalStateException e) {
			msg.errorCode = ErrorCode.FILE_IO_ERROR.code;
			msg.msg = ErrorCode.FILE_IO_ERROR.name;
			log.error(e.getMessage());
		}
		return msg;
	}
}
