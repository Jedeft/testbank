package com.ncu.testbank.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

@Controller
@RequestMapping("/admin")
public class AcademyController {
	
	@Autowired
	private IAcademyService academyService;
	
	/**
	 * 新增academy
	 * @param academy
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/academys", method = RequestMethod.POST)
	public ResponseMsg insertAcademy(Academy academy){
		ResponseMsg msg = new ResponseMsg();
        try {
        	academyService.insertOne(academy);
        	
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
	 * 更新academy
	 * @param academy
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/academys", method = RequestMethod.PUT)
	public ResponseMsg updateAcademy(Academy academy){
		ResponseMsg msg = new ResponseMsg();
        try {
        	
        	academyService.updateOne(academy);
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
	 * 删除academy
	 * @param academy
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/academys", method = RequestMethod.DELETE)
	public ResponseMsg deleteAcademy(Academy academy){
		ResponseMsg msg = new ResponseMsg();
        try {
        	academyService.deleteOne(academy.getAcademy_id());
        	
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
	 * @param academy
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/academys/ID", method = RequestMethod.GET)
	public ResponseMsg getAcademy(Academy academy){
		ResponseMsg msg = new ResponseMsg();
        try {
        	Academy data = academyService.getAcademy(academy.getAcademy_id());
        	
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
	 * 根据id获取指定academy
	 * @param academy
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/academys", method = RequestMethod.GET)
	public ResponseQueryMsg searchData(PageInfo page){
		ResponseQueryMsg msg = new ResponseQueryMsg();
        try {
        	List<Academy> academyList = academyService.searchData(page);
        	
        	msg.errorCode = ErrorCode.CALL_SUCCESS.code;
            msg.msg = ErrorCode.CALL_SUCCESS.name;
            msg.data = academyList;
            
            msg.total = page.getTotal();
            msg.totalPage = page.getTotalPage();
            msg.currentPage = page.getPage();
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
	
	
	@ResponseBody
	@RequestMapping(value = "/academys/csv", method = RequestMethod.POST)
	public ResponseQueryMsg loadCsv(@RequestParam(value = "file", required = false)MultipartFile file){
		ResponseQueryMsg msg = new ResponseQueryMsg();
        try {
        	
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
