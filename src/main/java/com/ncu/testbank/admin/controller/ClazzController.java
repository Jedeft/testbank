package com.ncu.testbank.admin.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

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

import com.ncu.testbank.admin.data.Clazz;
import com.ncu.testbank.admin.service.IClazzService;
import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.base.response.ResponseQueryMsg;

@RestController
@RequestMapping("/admin")
public class ClazzController {
	
	private Logger log = Logger.getLogger("testbankLog");
	
	@Autowired
	private IClazzService clazzService;
	
	/**
	 * 新增class
	 * @param clazz
	 * @return
	 */
	@RequestMapping(value = "/classes", method = RequestMethod.POST)
	public ResponseMsg insertClazz(@RequestBody Clazz clazz){
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
	 * @param class
	 * @return
	 */
	@RequestMapping(value = "/classes", method = RequestMethod.PATCH)
	public ResponseMsg updateClazz(@RequestBody Clazz clazz){
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
	 * @param class
	 * @return
	 */
	@RequestMapping(value = "/classes/{class_id}", method = RequestMethod.DELETE)
	public ResponseMsg deleteClazz(@PathVariable String class_id){
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
	 * 根据id获取指定class
	 * @param class
	 * @return
	 */
	@RequestMapping(value = "/classes/{class_id}", method = RequestMethod.GET)
	public ResponseMsg getClazz(@PathVariable String class_id){
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
	 * @param class
	 * @return
	 */
	@RequestMapping(value = "/classes", method = RequestMethod.GET)
	public ResponseQueryMsg searchData(PageInfo page, Clazz clazz){
		ResponseQueryMsg msg = new ResponseQueryMsg();
        try {
        	List<Clazz> clazzList;
        	clazzList = clazzService.searchData(page, clazz);
        	
        	msg.errorCode = ErrorCode.CALL_SUCCESS.code;
            msg.msg = ErrorCode.CALL_SUCCESS.name;
            msg.data = clazzList;
            
            msg.total = page.getTotal();
            msg.totalPage = page.getTotalPage();
            msg.currentPage = page.getPage();
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
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/classes/csv", method = RequestMethod.POST)
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