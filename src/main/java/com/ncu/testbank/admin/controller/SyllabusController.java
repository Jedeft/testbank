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

import com.ncu.testbank.admin.data.Syllabus;
import com.ncu.testbank.admin.data.Teacher;
import com.ncu.testbank.admin.data.view.SyllabusView;
import com.ncu.testbank.admin.service.ISyllabusService;
import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.base.response.ResponseQueryMsg;

@RestController
@RequestMapping("/admin")
public class SyllabusController {
private Logger log = Logger.getLogger("testbankLog");
	
	@Autowired
	private ISyllabusService syllabusService;
	
	/**
	 * 新增syllabus
	 * @param syllabus
	 * @return
	 */
	@RequestMapping(value = "/syllabuses", method = RequestMethod.POST)
	public ResponseMsg insertSyllabus(@RequestBody Syllabus syllabus){
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
	 * @param syllabus
	 * @return
	 */
	@RequestMapping(value = "/syllabuses", method = RequestMethod.PATCH)
	public ResponseMsg updatesyllabus(@RequestBody Syllabus syllabus){
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
	 * @param syllabus
	 * @return
	 */
	@RequestMapping(value = "/syllabuses/{syllabus_id}", method = RequestMethod.GET)
	public ResponseMsg getSyllabus(@PathVariable String syllabus_id){
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
	 * @param syllabus
	 * @return
	 */
	@RequestMapping(value = "/syllabuses/{syllabuses_id}", method = RequestMethod.DELETE)
	public ResponseMsg deleteSyllabus(@PathVariable String syllabuses_id){
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
	 * 分页获取syllabus信息
	 * @param syllabus
	 * @return
	 */
	@RequestMapping(value = "/syllabuses", method = RequestMethod.GET)
	public ResponseQueryMsg searchData(PageInfo page, Syllabus syllabus){
		ResponseQueryMsg msg = new ResponseQueryMsg();
        try {
        	List<SyllabusView> syllabusViewList;
        	syllabusViewList = syllabusService.searchData(page, syllabus);
        	
        	msg.errorCode = ErrorCode.CALL_SUCCESS.code;
            msg.msg = ErrorCode.CALL_SUCCESS.name;
            msg.data = syllabusViewList;
            
            msg.total = page.getTotal();
            msg.totalPage = page.getTotalPage();
            msg.currentPage = page.getPage();
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
        } catch (IllegalAccessException | InstantiationException
				| InvocationTargetException | IntrospectionException e) {
			msg.errorCode = ErrorCode.MAP_CONVERT_ERROR.code;
			msg.msg = ErrorCode.MAP_CONVERT_ERROR.name;
			log.error(e.getMessage());
		}
        return msg;
	}
	
	/**
	 * csv文件批量录入syllabus信息
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/syllabuses/csv", method = RequestMethod.POST)
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
