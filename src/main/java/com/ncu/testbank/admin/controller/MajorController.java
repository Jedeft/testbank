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

import com.ncu.testbank.admin.data.Major;
import com.ncu.testbank.admin.service.IMajorService;
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

@Api(value = "major-api", description = "有关于专业的CURD操作", position = 2)
@RestController
@RequestMapping("/admin")
public class MajorController {

	@Autowired
	private IMajorService majorService;

	/**
	 * 新增major
	 * 
	 * @param major
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/majors", method = RequestMethod.POST)
	@ApiOperation(value = "添加专业", httpMethod = "POST", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg insertMajor(
			@ApiParam(required = true, name = "major", value = "专业信息json数据") @RequestBody Major major) {
		ResponseMsg msg = new ResponseMsg();
		try {
			majorService.insertOne(major);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = major;
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
	 * 更新major
	 * 
	 * @param major
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/majors", method = RequestMethod.PATCH)
	@ApiOperation(value = "更新专业", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg updateMajor(
			@ApiParam(required = true, name = "major", value = "专业信息json数据") @RequestBody Major major) {
		ResponseMsg msg = new ResponseMsg();
		try {

			majorService.updateOne(major);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = majorService.getMajor(major.getMajor_id());
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
	 * 删除major
	 * 
	 * @param major
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/majors/{major_id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除专业", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteMajor(
			@ApiParam(required = true, name = "major_id", value = "专业ID") @PathVariable String major_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			majorService.deleteOne(major_id);

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
	 * 批量删除major
	 * 
	 * @param major
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/majors/batch", method = RequestMethod.DELETE)
	@ApiOperation(value = "批量删除专业", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteMajors(
			@ApiParam(required = true, name = "major_id", value = "major_id数组json数据") @RequestBody Map<String, List<String>> map) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (map.get("major_id") != null && !map.get("major_id").equals("")) {
				majorService.deleteData(map.get("major_id"));
			} else {
				msg.errorCode = 66666;
				msg.msg = "请选择删除专业！";
				return msg;
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
	 * 根据id获取指定major
	 * 
	 * @param major
	 * @return
	 */
	@RequestMapping(value = "/majors/{major_id}", method = RequestMethod.GET)
	@ApiOperation(value = "获取指定专业", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseAdmin权限，请header中携带Token")
	public ResponseMsg getMajor(
			@ApiParam(required = true, name = "major_id", value = "专业ID") @PathVariable String major_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			Major data = majorService.getMajor(major_id);

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
	 * 分页获取major信息
	 * 
	 * @param major
	 * @return
	 */
	@RequestMapping(value = "/majors", method = RequestMethod.GET)
	@ApiOperation(value = "检索专业", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseAdmin权限，请header中携带Token")
	public ResponseQueryMsg searchData(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiParam(required = false, name = "major_id", value = "专业ID信息检索") @RequestParam(value = "major_id", required = false) String major_id,
			@ApiParam(required = false, name = "academy_id", value = "学院ID信息检索") @RequestParam(value = "academy_id", required = false) String academy_id,
			@ApiParam(required = false, name = "name", value = "专业名称信息检索") @RequestParam(value = "name", required = false) String name) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			List<Major> majorList;
			PageInfo pageInfo = new PageInfo(page, rows);
			Major major = new Major(major_id, academy_id, name);

			majorList = majorService.searchData(pageInfo, major);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = majorList;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = majorList.size();
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
	 * csv文件批量录入major信息
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/majors/csv", method = RequestMethod.POST)
	@ApiOperation(value = "批量导入专业", httpMethod = "POST", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
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
			majorService.loadCsv(fileName, path, file);
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
