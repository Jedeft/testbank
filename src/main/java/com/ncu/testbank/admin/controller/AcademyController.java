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

import com.ncu.testbank.admin.data.Academy;
import com.ncu.testbank.admin.service.IAcademyService;
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

@Api(value = "academy-api", description = "有关于学院的CURD操作", position = 2)
@RestController
@RequestMapping("/admin")
public class AcademyController {

	@Autowired
	private IAcademyService academyService;

	/**
	 * 新增academy
	 * 
	 * @param academy
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/academys", method = RequestMethod.POST)
	@ApiOperation(value = "添加学院", httpMethod = "POST", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg insertAcademy(
			@ApiParam(required = true, name = "academy", value = "学院信息json数据") @RequestBody Academy academy) {
		ResponseMsg msg = new ResponseMsg();
		try {
			academyService.insertOne(academy);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = academy;
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
	 * 更新academy
	 * 
	 * @param academy
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/academys", method = RequestMethod.PATCH)
	@ApiOperation(value = "更新学院", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg updateAcademy(
			@ApiParam(required = true, name = "academy", value = "学院信息json数据") @RequestBody Academy academy) {
		ResponseMsg msg = new ResponseMsg();
		try {
			academyService.updateOne(academy);
			
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = academyService.getAcademy(academy.getAcademy_id());
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
	 * 删除academy
	 * 
	 * @param academy
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/academys/{academy_id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除学院", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteAcademy(
			@ApiParam(required = true, name = "academy_id", value = "学院ID") @PathVariable String academy_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			academyService.deleteOne(academy_id);
			
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
	 * 批量删除academy
	 * 
	 * @param academy
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/academys/batch", method = RequestMethod.DELETE)
	@ApiOperation(value = "批量删除学院", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteAcademys(
			@ApiParam(required = true, name = "academy_id", value = "academy_id数组json数据") @RequestBody Map<String, List<String>> map) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (map.get("academy_id") != null
					&& !map.get("academy_id").equals("")) {
				academyService.deleteData(map.get("academy_id"));
			} else {
				msg.errorCode = 66666;
				msg.msg = "请选择删除学院！";
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
	 * 根据id获取指定academy
	 * 
	 * @param academy
	 * @return
	 */
	@RequestMapping(value = "/academys/{academy_id}", method = RequestMethod.GET)
	@ApiOperation(value = "获取指定学院", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseAdmin权限，请header中携带Token")
	public ResponseMsg getAcademy(
			@ApiParam(required = true, name = "academy_id", value = "学院ID") @PathVariable String academy_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			Academy data = academyService.getAcademy(academy_id);

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
	 * 分页获取academy信息
	 * 
	 * @param academy
	 * @return
	 */
	@RequestMapping(value = "/academys", method = RequestMethod.GET)
	@ApiOperation(value = "检索学院", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseAdmin权限，请header中携带Token")
	public ResponseQueryMsg searchData(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiParam(required = false, name = "academy_id", value = "学院ID信息检索") @RequestParam(value = "academy_id", required = false) String academy_id,
			@ApiParam(required = false, name = "name", value = "学院名称信息检索") @RequestParam(value = "name", required = false) String name) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			List<Academy> academyList;
			PageInfo pageInfo = new PageInfo(page, rows);
			Academy academy = new Academy(academy_id, name);
			academyList = academyService.searchData(pageInfo, academy);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = academyList;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = academyList.size();
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
	 * csv文件批量录入academy信息
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/academys/csv", method = RequestMethod.POST)
	@ApiOperation(value = "批量导入学院", httpMethod = "POST", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
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
			academyService.loadCsv(fileName, path, file);
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
