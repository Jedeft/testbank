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

import com.ncu.testbank.admin.data.Clazz;
import com.ncu.testbank.admin.service.IClazzService;
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

@Api(value = "class-api", description = "有关于班级的CURD操作", position = 2)
@RestController
@RequestMapping("/admin")
public class ClazzController {

	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private IClazzService clazzService;

	/**
	 * 新增class
	 * 
	 * @param clazz
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/classes", method = RequestMethod.POST)
	@ApiOperation(value = "添加班级", httpMethod = "POST", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg insertClazz(
			@ApiParam(required = true, name = "class", value = "班级信息json数据") @RequestBody Clazz clazz) {
		ResponseMsg msg = new ResponseMsg();
		try {
			clazzService.insertOne(clazz);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = clazz;
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
	 * 更新class
	 * 
	 * @param class
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/classes", method = RequestMethod.PATCH)
	@ApiOperation(value = "更新班级", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg updateClazz(
			@ApiParam(required = true, name = "class", value = "班级信息json数据") @RequestBody Clazz clazz) {
		ResponseMsg msg = new ResponseMsg();
		try {

			clazzService.updateOne(clazz);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = clazzService.getClazz(clazz.getClass_id());
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
	 * 删除class
	 * 
	 * @param class
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/classes/{class_id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除班级", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteClazz(
			@ApiParam(required = true, name = "class_id", value = "班级ID") @PathVariable String class_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			clazzService.deleteOne(class_id);

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
	 * 批量删除class
	 * 
	 * @param class
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/classes/batch", method = RequestMethod.DELETE)
	@ApiOperation(value = "批量删除班级", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteClazzes(
			@ApiParam(required = true, name = "class_id", value = "class_id数组json数据") @RequestBody Map<String, List<String>> map) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (map.get("class_id") != null && !map.get("class_id").equals("")) {
				clazzService.deleteData(map.get("class_id"));
			} else {
				msg.errorCode = 66666;
				msg.msg = "请选择删除班级！";
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
	 * 根据id获取指定class
	 * 
	 * @param class
	 * @return
	 */
	@RequestMapping(value = "/classes/{class_id}", method = RequestMethod.GET)
	@ApiOperation(value = "获取指定班级", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseAdmin权限，请header中携带Token")
	public ResponseMsg getClazz(
			@ApiParam(required = true, name = "class_id", value = "班级ID") @PathVariable String class_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			Clazz data = clazzService.getClazz(class_id);

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
	 * 分页获取class信息
	 * 
	 * @param class
	 * @return
	 */
	@RequestMapping(value = "/classes", method = RequestMethod.GET)
	@ApiOperation(value = "检索班级", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseAdmin权限，请header中携带Token")
	public ResponseQueryMsg searchData(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiParam(required = false, name = "class_id", value = "班级ID信息检索") @RequestParam(value = "class_id", required = false) String class_id,
			@ApiParam(required = false, name = "major_id", value = "专业ID信息检索") @RequestParam(value = "major_id", required = false) String major_id,
			@ApiParam(required = false, name = "name", value = "班级名称信息检索") @RequestParam(value = "name", required = false) String name) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			List<Clazz> clazzList;
			PageInfo pageInfo = new PageInfo(page, rows);
			Clazz clazz = new Clazz(class_id, major_id, name);

			clazzList = clazzService.searchData(pageInfo, clazz);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = clazzList;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = clazzList.size();
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
	 * csv文件批量录入class信息
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/classes/csv", method = RequestMethod.POST)
	@ApiOperation(value = "批量导入班级", httpMethod = "POST", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
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
			clazzService.loadCsv(fileName, path, file);
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
