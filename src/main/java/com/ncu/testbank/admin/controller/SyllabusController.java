package com.ncu.testbank.admin.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.data.Syllabus;
import com.ncu.testbank.admin.data.view.SyllabusView;
import com.ncu.testbank.admin.service.ISyllabusService;
import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.base.response.ResponseQueryMsg;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "syllabus-api", description = "有关于课程表的CURD操作", position = 2)
@RestController
@RequestMapping("/admin")
public class SyllabusController {
	@Autowired
	private ISyllabusService syllabusService;

	/**
	 * 新增syllabus
	 * 
	 * @param syllabus
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/syllabuses", method = RequestMethod.POST)
	@ApiOperation(value = "添加一条课程表", httpMethod = "POST", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token,日期类型精确到天")
	public ResponseMsg insertSyllabus(
			@ApiParam(required = true, name = "syllabus", value = "课程表信息json数据,日期类型精确到天，此处可不带ID参数，后台生成") @RequestBody Syllabus syllabus) {
		ResponseMsg msg = new ResponseMsg();
		try {
			syllabusService.insertOne(syllabus);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = syllabus;
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
	 * 更新syllabus
	 * 
	 * @param syllabus
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/syllabuses", method = RequestMethod.PATCH)
	@ApiOperation(value = "更新课程表", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token，日期类型精确到天")
	public ResponseMsg updatesyllabus(
			@ApiParam(required = true, name = "syllabus", value = "课程表信息json数据，日期类型精确到天") @RequestBody Syllabus syllabus) {
		ResponseMsg msg = new ResponseMsg();
		try {

			syllabusService.updateOne(syllabus);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = syllabusService.getSyllabus(syllabus.getSyllabus_id());
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
	 * 根据id获取指定syllabus
	 * 
	 * @param syllabus
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/syllabuses/{syllabus_id}", method = RequestMethod.GET)
	@ApiOperation(value = "获取指定课程表", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseAdmin权限，请header中携带Token")
	public ResponseMsg getSyllabus(
			@ApiParam(required = true, name = "syllabus_id", value = "课程表ID") @PathVariable String syllabus_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			SyllabusView data = syllabusService.getSyllabus(syllabus_id);

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
	 * 删除syllabus
	 * 
	 * @param syllabus
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/syllabuses/{syllabuses_id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除课程表", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteSyllabus(
			@ApiParam(required = true, name = "syllabus_id", value = "课程表ID") @PathVariable String syllabuses_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			syllabusService.deleteOne(syllabuses_id);

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
	 * 批量删除syllabus
	 * 
	 * @param syllabus
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/syllabuses/batch", method = RequestMethod.DELETE)
	@ApiOperation(value = "批量删除课表", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteSyllabuses(
			@ApiParam(required = true, name = "syllabuses_id", value = "syllabuses_id数组json数据") @RequestBody Map<String, List<String>> map) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (map.get("syllabus_id") != null
					&& !map.get("syllabus_id").equals("")) {
				syllabusService.deleteData(map.get("syllabus_id"));
			} else {
				msg.errorCode = 66666;
				msg.msg = "请选择删除课表！";
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
	 * 分页获取syllabus信息
	 * 
	 * @param syllabus
	 * @return
	 */
	@RequestMapping(value = "/syllabuses", method = RequestMethod.GET)
	@ApiOperation(value = "检索课表", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseAdmin权限，请header中携带Token，日期精确到天")
	public ResponseQueryMsg searchData(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiParam(required = false, name = "start", value = "起始时间（精确到天）") @RequestParam(value = "start", required = false) java.sql.Date start,
			@ApiParam(required = false, name = "end", value = "结束时间（精确到天）") @RequestParam(value = "end", required = false) java.sql.Date end) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			List<SyllabusView> syllabusViewList;
			PageInfo pageInfo = new PageInfo(page, rows);
			Syllabus syllabus = new Syllabus(start, end);

			syllabusViewList = syllabusService.searchData(pageInfo, syllabus);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = syllabusViewList;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = syllabusViewList.size();
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
	 * csv文件批量录入syllabus信息
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/syllabuses/csv", method = RequestMethod.POST)
	@ApiOperation(value = "批量导入课程表", httpMethod = "POST", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token，csv文件中可不含ID")
	public ResponseMsg loadCsv(
			@ApiParam(required = true, name = "file", value = "csv文件") @RequestParam(value = "file", required = true) MultipartFile file,
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
			syllabusService.loadCsv(fileName, path, file);
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
}
