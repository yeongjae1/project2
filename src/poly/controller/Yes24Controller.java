package poly.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.service.IYes24Service;


@Controller
public class Yes24Controller {
	private Logger log = Logger.getLogger(this.getClass());
	/*
	 * 비즈니스 로직(중요 로직을 수행하기 위해 사용되는 서비스를 메모리에 적재(싱글톤패턴 적용됨)	 * 
	 */
	@Resource(name = "Yes24Service")
	private IYes24Service yes24Service;
	
	
	@RequestMapping(value = "yes24/collectYes24")
	@ResponseBody
	public String collectYes24(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info(this.getClass().getName() + ".collectYes24 Start!");
		
		yes24Service.collectYes24book();		
		log.info(this.getClass().getName() + ".collectYes24 End!");
		
		return "success";
	}
	
}
