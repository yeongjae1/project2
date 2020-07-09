package poly.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import poly.dto.BookDTO;
import poly.dto.KyoboDTO;
import poly.persistance.mongo.IKyoboMapper;
import poly.service.IKyoboService;
import poly.util.DateUtil;

@Service("KyoboService")
public class KyoboService implements IKyoboService {

	@Resource(name = "KyoboMapper")
	private IKyoboMapper kyoboMapper;

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public int collectKyobobook() throws Exception {
		log.info(this.getClass().getName() + ".collectKyobobook Start!");

		int res = 0;

		List<KyoboDTO> pList = new ArrayList<KyoboDTO>();

		for (int i = 1; i < 586; i++) {
			String url = "http://digital.kyobobook.co.kr/digital/publicview/publicViewFree.ink?tabType=EBOOK&tabSrnb=12&currPage="
					+ i;
			System.out.println(url);
			// JSOUP 라이브러리를 통해 사이트 접속되면, 그 사이트 전체 HTML소스 저장할 변수
			Document doc1 = null;
			doc1 = Jsoup.connect(url).get();

			Elements element1 = doc1.select("div.thumbnail_list").eq(1);

			Iterator<Element> bookList = element1.select("ul li.list_item").iterator();

			// 순위는 1위부터 50위까지 반복문
			while (bookList.hasNext()) {
				
				Element bookInfo = bookList.next();
				String a = bookInfo.select("div.pic_area > a > span").text();
				if (a.equals("무료 19세 이상 성인용"))
					continue;

				// 크롤링을 통해 데이터 저장하기
				// 제목
				String title = bookInfo.select("div.cont_area > div.title").text();
				log.info(title);
				// 이미지링크
				String img = bookInfo.select("div.pic_area > a > img").attr("src");
				log.info(img);
				// 그냥링크
				String info = bookInfo.select("div.pic_area > a").attr("href");
				log.info(info);
				if (title.equals(" ") || img.equals(" ") || info.equals(" "))
					continue;

				Document doc2 = null;
				doc2 = Jsoup.connect("https://digital.kyobobook.co.kr" + info).get();

				Elements element2 = doc2.select("div.bookDetail_wrap");

				String book1 = element2.select("div.bookContent_wrap div.bookContent div.article ul li a").eq(0).text();
				log.info(book1);
				String book2 = element2.select("div.bookContent_wrap div.bookContent div.article ul li a").eq(1).text();
				log.info(book2);
				String book3 = element2.select("div.bookDetail div.bookDetail_inner p.txt_info").eq(0).text();
				log.info(book3);
				String ttsok = element2.select("div.bookDetail > div.bookInfo_wrap > ul > li:nth-child(1) > p.group")
						.text();
				log.info(ttsok);
				if (book1.equals(" ") || book2.equals(" ") || book3.equals(" ") || ttsok.equals(" "))
					continue;

				String[] apd = book3.split("\\|");
				if (apd.length == 2) {
					String genre = book1 + " " + book2;
					String auth = apd[0];
					String pub = apd[0];
					String pubdt = apd[1];
					String tts = ttsok.replace("소득공제", "");
					String link = "https://digital.kyobobook.co.kr" + info;

					bookInfo = null;

					KyoboDTO pDTO = new KyoboDTO();
					pDTO.setTitle(title); // 제목
					pDTO.setAuth(auth); // 저자
					pDTO.setPub(pub);
					pDTO.setPubdt(pubdt);
					pDTO.setGenre(genre); // 장르
					pDTO.setImg(img);
					pDTO.setLink(link);
					pDTO.setTts(tts);

					pList.add(pDTO);		
				} else if(apd.length == 4){
					String genre = book1 + " " + book2;
					String auth = apd[0];
					String pub = apd[1];
					String pubdt = apd[3];
					String tts = ttsok.replace("소득공제", "");
					String link = "https://digital.kyobobook.co.kr" + info;
				
					bookInfo = null;
					
					KyoboDTO pDTO = new KyoboDTO();
					pDTO.setTitle(title); // 제목
					pDTO.setAuth(auth); // 저자
					pDTO.setPub(pub);
					pDTO.setPubdt(pubdt);
					pDTO.setGenre(genre); // 장르
					pDTO.setImg(img);
					pDTO.setLink(link);
					pDTO.setTts(tts);

					pList.add(pDTO);					
					
				}else if(apd.length == 5){
					String genre = book1 + " " + book2;
					String auth = apd[0];
					String pub = apd[1];
					String pubdt = apd[4];
					String tts = ttsok.replace("소득공제", "");
					String link = "https://digital.kyobobook.co.kr" + info;
				
					bookInfo = null;
					
					KyoboDTO pDTO = new KyoboDTO();
					pDTO.setTitle(title); // 제목
					pDTO.setAuth(auth); // 저자
					pDTO.setPub(pub);
					pDTO.setPubdt(pubdt);
					pDTO.setGenre(genre); // 장르
					pDTO.setImg(img);
					pDTO.setLink(link);
					pDTO.setTts(tts);

					pList.add(pDTO);					
					
				}
				else {
					String genre = book1 + " " + book2;
					String auth = apd[0];
					String pub = apd[1];
					String pubdt = apd[2];
					String tts = ttsok.replace("소득공제", "");
					String link = "https://digital.kyobobook.co.kr" + info;

					bookInfo = null;

					KyoboDTO pDTO = new KyoboDTO();
					pDTO.setTitle(title); // 제목
					pDTO.setAuth(auth); // 저자
					pDTO.setPub(pub);
					pDTO.setPubdt(pubdt);
					pDTO.setGenre(genre); // 장르
					pDTO.setImg(img);
					pDTO.setLink(link);
					pDTO.setTts(tts);

					pList.add(pDTO);					
					
				}
			}
		}
		String colNm = "KyoboBookInfo_" + DateUtil.getDateTime("yyyyMMdd");

		kyoboMapper.createCollection(colNm);

		kyoboMapper.insertBookinfo(pList, colNm);

		log.info(this.getClass().getName() + ".collectKyobobook End!");

		return res;
	}

	@Override
	public List<BookDTO> getBookInfo() throws Exception {
		
		log.info(this.getClass().getName() + ".collectKyobobook Start!");
		
		String colNm = "KyoboBookInfo_20200618";
		String auth = "유시민 지음";
		
		List<BookDTO> rList = kyoboMapper.getBookInfo(colNm, auth);
		
		if(rList == null) {
			rList = new ArrayList<BookDTO>();
		}
				
		
		log.info(this.getClass().getName() + ".collectKyobobook Start!");
		
		return rList;
	}

	@Override
	public List<BookDTO> getAuthBook() throws Exception {
				
		String colNm = "KyoboBookInfo_20200618";
		
		List<BookDTO> rList = kyoboMapper.getAuthBook(colNm);
		
		if (rList == null) {
			rList = new ArrayList<BookDTO>();
		}
		return rList;
	}

	@Override
	public List<BookDTO> search() throws Exception {
		
		String colNm = "KyoboBookInfo_20200618";
		
		String colNm1 = "Yes24BookInfo_20200616";
		
	//	List<KyoboDTO> rList = kyoboMapper.getBookInfo(colNm, auth);
		
		return null;
	}
	
	@Override
	public List<KyoboDTO> bookList() throws Exception {

		log.info(this.getClass().getName() + ".bookList Start!");

		// 조회할 컬렉션 이름
		String colNm = "KyoboBookInfo_20200626";

		List<KyoboDTO> rList = kyoboMapper.bookList(colNm);

		if (rList == null) {
			rList = new ArrayList<KyoboDTO>();
		}

		log.info(this.getClass().getName() + ".bookList End!");

		return rList;	
	}

	@Override
	public List<KyoboDTO> tts() throws Exception {
		log.info(this.getClass().getName() + ".tts Start!");

		// 조회할 컬렉션 이름
		String colNm = "KyoboBookInfo_20200626";
		String tts = "듣기 가능";

		List<KyoboDTO> rList = kyoboMapper.tts(colNm, tts);

		if (rList == null) {
			rList = new ArrayList<KyoboDTO>();
		}

		log.info(this.getClass().getName() + ".tts End!");

		return rList;
	}

	public List<KyoboDTO> search(Map<String, Object> pMap) throws Exception {
		log.info(this.getClass().getName() + ".search start!");
		String colNm = "KyoboBookInfo_20200626";
		
		List<KyoboDTO> rList = kyoboMapper.search(colNm, pMap);
		if (rList == null) {
			rList = new ArrayList<KyoboDTO>();
		}

		log.info(this.getClass().getName() + ".search End!");
		
		return rList;
		
	}

	@Override
	public List<KyoboDTO> random() throws Exception {
		log.info(this.getClass().getName() + ".random Start!");

		// 조회할 컬렉션 이름
		String colNm = "KyoboBookInfo_20200626";

		List<KyoboDTO> rList = kyoboMapper.random(colNm);

		if (rList == null) {
			rList = new ArrayList<KyoboDTO>();
		}

		log.info(this.getClass().getName() + ".random End!");

		return rList;	
	}

	@Override
	public List<KyoboDTO> title() throws Exception {
		// 조회할 컬렉션 이름
				String colNm = "KyoboBookInfo_20200626";

				List<KyoboDTO> rList = kyoboMapper.title(colNm);

				if (rList == null) {
					rList = new ArrayList<KyoboDTO>();
				}

				log.info(this.getClass().getName() + ".random End!");

				return rList;	

	}
	
}
