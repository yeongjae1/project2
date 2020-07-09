package poly.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.dto.UserInfoDTO;
import poly.service.IUserInfoService;
import poly.util.CmmUtil;
import poly.util.EncryptUtil;

/*
 * Controller 선언해야만 Spring 프레임워크에서 Controller인지 인식 가능
 * 자바 서블릿 역할 수행
 * */
@Controller
public class UserInfoController {
	private Logger log = Logger.getLogger(this.getClass());
	
	/*
	 * 비즈니스 로직(중요 로직을 수행하기 위해 사용되는 서비스를 메모리에 적재(싱글톤패턴 적용됨)
	 * */
	@Resource(name = "UserInfoService")
	private IUserInfoService userInfoService;
	
	
	/**
	 * 회원가입 화면으로 이동
	 * */	
	@RequestMapping(value="user/userRegForm")
	public String userRegForm() {
		log.info(this.getClass().getName() + ".user/userRegForm ok!");
		
		return "/user/UserRegForm";
	}
	
	
	/**
	 * 회원가입 로직 처리
	 * */
	@RequestMapping(value="user/insertUserInfo")
	public String insertUserInfo(HttpServletRequest request, HttpServletResponse response, 
			ModelMap model) throws Exception {
		
		log.info(this.getClass().getName() + ".insertUserInfo start!");
		
		//회원가입 결과에 대한 메시지를 전달할 변수
		String msg = "";
		String url = "";
		
		//웹(회원정보 입력화면)에서 받는 정보를 저장할 변수
		UserInfoDTO pDTO = null;
		
		try{
			
			/*
			 * #######################################################
			 *        웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장 시작!!
			 * 
			 *    무조건 웹으로 받은 정보는 DTO에 저장하기 위해 임시로 String 변수에 저장함
			 * #######################################################
			 */			
			String user_id = CmmUtil.nvl(request.getParameter("user_id")); //아이디
			String user_name = CmmUtil.nvl(request.getParameter("user_name")); //이름
			String password = CmmUtil.nvl(request.getParameter("password")); //비밀번호
			String email = CmmUtil.nvl(request.getParameter("email")); //이메일
			
			/*
			 * #######################################################
			 *        웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장 끝!!
			 * 
			 *    무조건 웹으로 받은 정보는 DTO에 저장하기 위해 임시로 String 변수에 저장함
			 * #######################################################
			 */	
			
			/*
			 * #######################################################
			 * 	 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함
			 * 						반드시 작성할 것
			 * #######################################################
			 * */
			log.info("user_id : "+ user_id);
			log.info("user_name : "+ user_name);
			log.info("password : "+ password);
			log.info("email : "+ email);
				
			
			
			/*
			 * #######################################################
			 *        웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기 시작!!
			 * 
			 *        무조건 웹으로 받은 정보는 DTO에 저장해야 한다고 이해하길 권함
			 * #######################################################
			 */
			
			
			//웹(회원정보 입력화면)에서 받는 정보를 저장할 변수를 메모리에 올리기 
			pDTO = new UserInfoDTO();
			
			pDTO.setUser_id(user_id);
			pDTO.setUser_name(user_name);
			
			//비밀번호는 절대로 복호화되지 않도록 해시 알고리즘으로 암호화함
			pDTO.setPassword(EncryptUtil.encHashSHA256(password)); 
			
			//민감 정보인 이메일은 AES128-CBC로 암호화함
			pDTO.setEmail(EncryptUtil.encAES128CBC(email));
			
			
			/*
			 * #######################################################
			 *        웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기 끝!!
			 * 
			 *        무조건 웹으로 받은 정보는 DTO에 저장해야 한다고 이해하길 권함
			 * #######################################################
			 */
			
			/*
			 * 회원가입
			 * */
			int res = userInfoService.insertUserInfo(pDTO);
			
			log.info("회원가입 결과(res) : "+ res);
			
			if (res==1) {
				msg = "회원가입되었습니다.";
				url = "/index.do";
			//추후 회원가입 입력화면에서 ajax를 활용해서 아이디 중복, 이메일 중복을 체크하길 바람 
			}else if (res==2){
				msg = "이미 가입된 아이디입니다.";
				url = "/index.do";
				
			}else {
				msg = "오류로 인해 회원가입이 실패하였습니다.";
				url = "/index.do";
			}

		}catch(Exception e){
			//저장이 실패되면 사용자에게 보여줄 메시지			
			msg = "실패하였습니다. : "+ e.toString();
			log.info(e.toString());
			e.printStackTrace();
			
		}finally{
			log.info(this.getClass().getName() + ".insertUserInfo end!");
			
			//변수 초기화(메모리 효율화 시키기 위해 사용함)
			model.addAttribute("msg", msg);
			model.addAttribute("url", url);
			pDTO = null;
			
		}
		
		return "/user/LoginResult2";
	}	
		
	/**
	 * 로그인을 위한 입력 화면으로 이동
	 * */	
	@RequestMapping(value="user/loginForm")
	public String loginForm() {
		log.info(this.getClass().getName() + ".user/loginForm ok!");
		
		return "/user/LoginForm";
	}
	@RequestMapping(value="user/logOut")
	public String logOut(HttpSession session,ModelMap model) {
		log.info(this.getClass().getName() + ".user/logOut ok!");
		
		session.invalidate();
	
		model.addAttribute("msg","로그아웃되었습니다.");
		model.addAttribute("url","/index.do");
		
		return "/user/LoginResult2";
	}
	
	/**
	 * 로그인 처리 및 결과 알려주는 화면으로 이동
	 * */	
	@RequestMapping(value="user/getUserLoginCheck")
	public String getUserLoginCheck(HttpSession session, HttpServletRequest request, HttpServletResponse response, 
			ModelMap model) throws Exception {
		log.info(this.getClass().getName() + ".getUserLoginCheck start!");
		
		//로그인 처리 결과를 저장할 변수 (로그인 성공 : 1, 아이디, 비밀번호 불일치로인한 실패 : 0, 시스템 에러 : 2)
		int res = 0; 
				
		//웹(회원정보 입력화면)에서 받는 정보를 저장할 변수
		UserInfoDTO pDTO = null;
		
		try{
			
			/*
			 * #######################################################
			 *        웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장 시작!!
			 * 
			 *    무조건 웹으로 받은 정보는 DTO에 저장하기 위해 임시로 String 변수에 저장함
			 * #######################################################
			 */			
			String user_id = CmmUtil.nvl(request.getParameter("user_id")); //아이디
			String password = CmmUtil.nvl(request.getParameter("password")); //비밀번호
			/*
			 * #######################################################
			 *        웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장 끝!!
			 * 
			 *    무조건 웹으로 받은 정보는 DTO에 저장하기 위해 임시로 String 변수에 저장함
			 * #######################################################
			 */	
			
			/*
			 * #######################################################
			 * 	 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함
			 * 						반드시 작성할 것
			 * #######################################################
			 * */
			log.info("user_id : "+ user_id);
			log.info("password : "+ password);
			
			/*
			 * #######################################################
			 *        웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기 시작!!
			 * 
			 *        무조건 웹으로 받은 정보는 DTO에 저장해야 한다고 이해하길 권함
			 * #######################################################
			 */
			
			
			//웹(회원정보 입력화면)에서 받는 정보를 저장할 변수를 메모리에 올리기 
			pDTO = new UserInfoDTO();
			
			pDTO.setUser_id(user_id);
			
			//비밀번호는 절대로 복호화되지 않도록 해시 알고리즘으로 암호화함
			pDTO.setPassword(EncryptUtil.encHashSHA256(password)); 
			
			/*
			 * #######################################################
			 *        웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기 끝!!
			 * 
			 *        무조건 웹으로 받은 정보는 DTO에 저장해야 한다고 이해하길 권함
			 * #######################################################
			 */
			
			// 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기 위한 userInfoService 호출하기
			res = userInfoService.getUserLoginCheck(pDTO);
			
			log.info("res : "+ res);
			/*
			 * 로그인을 성공했다면, 회원아이디 정보를 session에 저장함
			 * 
			 * 세션은 톰켓(was)의 메모리에 존재하며, 웹사이트에 접속한 사람(연결된 객체)마다 메모리에 값을 올린다.
			 * 			 * 
			 * 예) 톰켓에 100명의 사용자가 로그인했다면, 사용자 각각 회원아이디를 메모리에 저장하며.
			 *    메모리에 저장된 객체의 수는 100개이다.
			 *    따라서 과도한 세션은 톰켓의 메모리 부하를 발생시켜 서버가 다운되는 현상이 있을 수 있기때문에,
			 *    최소한으로 사용하는 것을 권장한다. 
			 *    
			 * 스프링에서 세션을 사용하기 위해서는 함수명의 파라미터에 HttpSession session 존재해야 한다.
			 * 세션은 톰켓의 메모리에 저장되기 때문에 url마다 전달하는게 필요하지 않고,
			 * 그냥 메모리에서 부르면 되기 때문에 jsp, controller에서 쉽게 불러서 쓸수 있다.
			 * */
			if (res==1) { //로그인 성공
				
				/*
				 * 세션에 회원아이디 저장하기, 추후 로그인여부를 체크하기 위해 세션에 값이 존재하는지 체크한다.
				 * 일반적으로 세션에 저장되는 키는 대문자로 입력하며, 앞에 SS를 붙인다.
				 * 
				 * Session 단어에서 SS를 가져온 것이다.
				 */
				session.setAttribute("SS_USER_ID", user_id);
				model.addAttribute("msg", user_id + "님 어서오세요!");
				model.addAttribute("url", "/index.do");
			}

		}catch(Exception e){
			//저장이 실패되면 사용자에게 보여줄 메시지			
			res = 2;
			
			model.addAttribute("msg", "로그인이 실패했습니다.");
			model.addAttribute("url", "/index.do");
			
			log.info(e.toString());
			e.printStackTrace();
			
		}finally{
			log.info(this.getClass().getName() + ".insertUserInfo end!");
			
			/* 로그인 처리 결과를 jsp에 전달하기 위해 변수 사용
			 * 숫자 유형의 데이터 타입은 값을 전달하고 받는데 불편함이  있어
			 * 문자 유형(String)으로 강제 형변환하여 jsp에 전달한다.
			 * */
			
			//변수 초기화(메모리 효율화 시키기 위해 사용함)
			pDTO = null;
			
		}
		if(res == 0) {
			model.addAttribute("msg", "비밀번호가 잘못되었습니다.");
			model.addAttribute("url", "/index.do");
		}
		return "/user/LoginResult";
	}	
			
	@RequestMapping(value="user/chgPW")
	public String chgPW(HttpServletRequest request, HttpServletResponse response 
			,HttpSession session, ModelMap model) throws Exception {
		
		log.info(this.getClass().getName() + ".chgPW start!");
		
		String user_id = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
		String password = CmmUtil.nvl(request.getParameter("password")); //비밀번호
				
		//웹(회원정보 입력화면)에서 받는 정보를 저장할 변수
		UserInfoDTO pDTO = new UserInfoDTO();;
		
		
			pDTO.setUser_id(user_id);
			pDTO.setPassword(EncryptUtil.encHashSHA256(password));
			
			userInfoService.chgPW(pDTO);
			
			model.addAttribute("msg", "수정되었습니다");
			model.addAttribute("url", "/index.do");
		
		return "/user/LoginResult2";
	}
	
	@RequestMapping(value="user/findId")
	public String findId(HttpServletRequest request, HttpServletResponse response 
			,ModelMap model) throws Exception{
		UserInfoDTO pDTO = null;
		String email = request.getParameter("email");
		
		pDTO = new UserInfoDTO();
		
		pDTO.setEmail(EncryptUtil.encAES128CBC(email));
		log.info(EncryptUtil.encAES128CBC(email));
		UserInfoDTO qDTO = userInfoService.findId(pDTO);
		
		if (qDTO == null) {
			model.addAttribute("msg", "해당 이메일로 가입된 아이디가 없습니다");
			model.addAttribute("url", "/index.do");
		} else {
		model.addAttribute("msg", "id는 : " + qDTO.getUser_id() + "입니다");
		model.addAttribute("url", "/index.do");
		}
		pDTO = null;
		
		
		return "/user/LoginResult2";
	}
	@RequestMapping(value="user/deleteId")
	public String deleteId(HttpServletRequest request, HttpServletResponse response 
			,HttpSession session, ModelMap model) throws Exception{
		
		UserInfoDTO pDTO = null;
		String user_id = (String) session.getAttribute("SS_USER_ID");
		
		pDTO = new UserInfoDTO();
		
		pDTO.setUser_id(user_id);
		log.info(user_id);
		userInfoService.deleteId(pDTO);
		
		pDTO = null;
		
		session.invalidate();
		
		model.addAttribute("msg", "처리되었습니다.");
		model.addAttribute("url", "/index.do");
		
		return "/user/LoginResult2";
	}
	@RequestMapping(value="/user/manage")
	public String manage(HttpServletRequest request, HttpServletResponse response 
			, ModelMap model) throws Exception{
		log.info(this.getClass().getName() + ".manage start!");
		List<UserInfoDTO> rList = userInfoService.manage();
		
		if (rList==null) {
			rList = new ArrayList<UserInfoDTO>();
		}
		
		log.info(rList.get(1).getUser_id());
		model.addAttribute("rList",rList);
		
		rList = null;
		
		log.info(this.getClass().getName() + ".manage end!");
		return "/user/manage";
	}
	@RequestMapping(value="user/deleteId2")
	public String deleteId2(HttpServletRequest request, HttpServletResponse response 
			,HttpSession session, ModelMap model) throws Exception{
		
		UserInfoDTO pDTO = null;
		String user_id = request.getParameter("user_id");
		
		pDTO = new UserInfoDTO();
		
		pDTO.setUser_id(user_id);
		log.info(user_id);
		userInfoService.deleteId(pDTO);
		
		pDTO = null;
		
		model.addAttribute("msg", "처리되었습니다.");
		model.addAttribute("url", "/user/manage.do");
		
		return "/user/LoginResult2";
	}
}
