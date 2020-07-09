package poly.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import poly.dto.BookDTO;
import poly.dto.KyoboDTO;
import poly.service.IKyoboService;

@Controller
public class KyoboContoller {
	private Logger log = Logger.getLogger(this.getClass());
	/*
	 * 비즈니스 로직(중요 로직을 수행하기 위해 사용되는 서비스를 메모리에 적재(싱글톤패턴 적용됨) *
	 */
	@Resource(name = "KyoboService")
	private IKyoboService kyoboService;

	@RequestMapping(value = "book/komoran_test")
	
	public String komoran_test(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		log.info(this.getClass().getName() + ".komoran_test Start!");
		String str = "";
		List<KyoboDTO> aList = kyoboService.title();
		 
		List<String> sList = new ArrayList<String>();
		for (int r = 25; r < aList.size();) {
			for (int i = r - 25; i < r; i++) {

				str += aList.get(i).getTitle() + "/";
			}
			Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
			KomoranResult komoranResult = komoran.analyze(str);
			sList = komoranResult.getNouns();
			komoranResult = null;
			komoran = null;
			str = "";
			r += 25;
			log.info(r-24 + "번째!");
		}
		List<String> pList = new ArrayList<String>(); // 단어
		List<Integer> iList = new ArrayList<Integer>(); // 카운트

		for (int i = 0; i < sList.size(); i++) {
			if (sList.get(i).length() > 1) {
				if (!pList.contains(sList.get(i))) {
					pList.add(sList.get(i));
					int tmp = 0;
					for (int j = 0; j < sList.size(); j++) {
						if (sList.get(i).equals(sList.get(j)))
							tmp++;
					}
					iList.add(tmp);
				}
			}
		}
		model.addAttribute("pList", pList);
		model.addAttribute("iList", iList);

		/**
		 * count 1. 길이가 1인 글자가 아닌것중에서 2. wordList에다가 있는지 없는지 확인해서 저장 3. 있는경우 아무것도 안하면되
		 * 4. 없는경우 wordList에다가 넣고 그 글자가 몇번 나왔는지 카운팅 4. 카운팅 한 후에 countList에다가 넣음
		 */

		log.info(this.getClass().getName() + ".komoran_test End!");

		return "/book/komoran_test";
	}

	@RequestMapping(value = "kyobo/collectKyobo")
	@ResponseBody
	public String collectKyobo(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info(this.getClass().getName() + ".collectKyobo Start!");

		kyoboService.collectKyobobook();
		log.info(this.getClass().getName() + ".collectKyobo End!");

		return "success";
	}

	@RequestMapping(value = "kyobo/selectKyobo")
	@ResponseBody
	public List<BookDTO> getBookInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info(this.getClass().getName() + ".selectKyobo Start!");

		List<BookDTO> rList = kyoboService.getBookInfo();

		if (rList == null) {
			rList = new ArrayList<BookDTO>();
		}

		log.info(this.getClass().getName() + ".selectKyobo End!");

		return rList;
	}

	@RequestMapping(value = "kyobo/getAuthBook")
	@ResponseBody
	public List<BookDTO> getAuthBook(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info(this.getClass().getName() + ".getAuthBook Start!");

		List<BookDTO> rList = kyoboService.getAuthBook();

		if (rList == null) {
			rList = new ArrayList<BookDTO>();
		}

		log.info(this.getClass().getName() + ".getAuthBook End!");

		return rList;
	}

	@RequestMapping(value = "book/bookList")
	@ResponseBody
	public List<KyoboDTO> bookList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info(this.getClass());

		List<KyoboDTO> rList = kyoboService.bookList();

		if (rList == null) {
			rList = new ArrayList<KyoboDTO>();
		}

		return rList;
	}
}
