package com.ncu.testbank.teacher.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.teacher.data.Point;
import com.ncu.testbank.teacher.data.params.PointParams;
import com.ncu.testbank.teacher.service.IPointService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "points-api", description = "课程考点管理操作，需要bankBuilder权限", position = 3)
@RequiresRoles("bankBuilder")
@RestController
@RequestMapping("/teacher")
public class PointController {

	@Autowired
	private IPointService pointService;

	/**
	 * 新增考点
	 * 
	 * @param point
	 * @return
	 */
	@RequestMapping(value = "/points", method = RequestMethod.POST)
	@ApiOperation(value = "添加考点", httpMethod = "POST", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg insertPoints(
			@ApiParam(required = true, name = "point", value = "考点json数据，ID由后台生成") @RequestBody Point point) {
		ResponseMsg msg = new ResponseMsg();
		try {
			pointService.insertPoint(point);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = point;
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
	 * 查询考点
	 * 
	 * @param course_id
	 * @return
	 */
	@RequestMapping(value = "/points/{course_id}", method = RequestMethod.GET)
	@ApiOperation(value = "根据课程ID查找考点", httpMethod = "GET", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg searchPoints(
			@ApiParam(required = true, name = "course_id", value = "课程ID") @PathVariable String course_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			List<Point> list = pointService.searchPoint(course_id);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = list;
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
	 * 修改考点
	 * 
	 * @param pointParams
	 * @return
	 */
	@RequestMapping(value = "/points", method = RequestMethod.PATCH)
	@ApiOperation(value = "修改考点", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg updatePoints(
			@ApiParam(required = true, name = "point", value = "考点json数据") @RequestBody PointParams pointParams) {
		ResponseMsg msg = new ResponseMsg();
		try {
			Point point = pointParams.toPoint();
			pointService.updatePoint(point);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = pointService.getPoint(point.getPoint_id());
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
	 * 删除考点
	 * 
	 * @param course_id
	 * @return
	 */
	@RequestMapping(value = "/points", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除考点", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg deletePoints(
			@ApiParam(required = true, name = "point_id", value = "考点json数据") @RequestBody Map<String, List<Long>> map) {
		ResponseMsg msg = new ResponseMsg();
		try {
			List<Long> point_id = null;
			if (map != null && map.get("point_id") != null) {
				point_id = map.get("point_id");
			} else {
				msg.errorCode = 66666;
				msg.msg = "请选择删除考点！";
			}
			pointService.deletePoint(point_id);
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
