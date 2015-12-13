package com.ncu.testbank.admin.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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

@RestController
@RequestMapping("/admin")
public class MajorController {
	
	private Logger log = Logger.getLogger("testbankLog");
	
	@Autowired
	private IMajorService majorService;
	
	/**
	 * 新增major
	 * @param major
	 * @return
	 */
	@RequestMapping(value = "/majors", method = RequestMethod.POST)
	public ResponseMsg insertMajor(@RequestBody Major major){
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
	 * @param major
	 * @return
	 */
	@RequestMapping(value = "/majors", method = RequestMethod.PATCH)
	public ResponseMsg updateMajor(@RequestBody Major major){
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
	 * @param major
	 * @return
	 */
	@RequestMapping(value = "/majors/{major_id}", method = RequestMethod.DELETE)
	public ResponseMsg deleteMajor(@PathVariable String major_id){
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
	 * @param major
	 * @return
	 */
	@RequestMapping(value = "/majors/batch", method = RequestMethod.DELETE)
	public ResponseMsg deleteMajors(@RequestBody Map<String, List<String>> map){
		ResponseMsg msg = new ResponseMsg();
        try {
        	if (map.get("major_id")!= null) {
        		majorService.deleteData(map.get("major_id"));
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
	 * @param major
	 * @return
	 */
	@RequestMapping(value = "/majors/{major_id}", method = RequestMethod.GET)
	public ResponseMsg getMajor(@PathVariable String major_id){
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
	 * @param major
	 * @return
	 */
	@RequestMapping(value = "/majors", method = RequestMethod.GET)
	public ResponseQueryMsg searchData(PageInfo page, Major major){
		ResponseQueryMsg msg = new ResponseQueryMsg();
        try {
        	List<Major> majorList;
			majorList = majorService.searchData(page, major);
        	
        	msg.errorCode = ErrorCode.CALL_SUCCESS.code;
            msg.msg = ErrorCode.CALL_SUCCESS.name;
            msg.data = majorList;
            
            msg.total = page.getTotal();
            msg.totalPage = page.getTotalPage();
            msg.currentPage = page.getPage();
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
        } catch (IllegalAccessException | InstantiationException
				| InvocationTargetException | IntrospectionException e) {
			msg.errorCode = ErrorCode.MAP_CONVERT_ERROR.code;
			msg.msg = ErrorCode.MAP_CONVERT_ERROR.name;
			log.error(e.getMessage());
		}
        return msg;
	}
	
	/**
	 * csv文件批量录入major信息
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/majors/csv", method = RequestMethod.POST)
	public ResponseMsg loadCsv(@RequestParam(value = "file", required = false)MultipartFile file, HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
        try {
        	
        	String fileName = new Date().getTime() + "_" + file.getOriginalFilename();
        	String path = request.getSession().getServletContext().getRealPath("upload");
        	
        	if ( !fileName.endsWith(".csv") ) {
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
