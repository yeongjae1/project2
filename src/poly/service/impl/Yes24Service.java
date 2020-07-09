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

import poly.dto.KyoboDTO;
import poly.dto.Yes24DTO;
import poly.persistance.mongo.IYes24Mapper;
import poly.service.IYes24Service;
import poly.util.DateUtil;

@Service("Yes24Service")
public class Yes24Service implements IYes24Service {

	@Resource(name = "Yes24Mapper")
	private IYes24Mapper yes24Mapper;

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public int collectYes24book() throws Exception {

		log.info(this.getClass().getName() + ".collectYes24book Start!");

		int res = 0;

		List<Yes24DTO> pList = new ArrayList<Yes24DTO>();

		for (int i = 1; i < 10; i++) {
			for (int j = 1; j < 6; j++) {
				String url = "http://www.yes24.com/24/Category/More/017001016?ElemNo=89&ElemSeq=" + i + "&PageNumber="
						+ j;

				// JSOUP 라이브러리를 통해 사이트 접속되면, 그 사이트 전체 HTML소스 저장할 변수
				Document doc = null;
				doc = Jsoup.connect(url).get();

				Elements element = doc.select("div#category_layout ul");

				Iterator<Element> bookList = element.select("li").iterator();

				// 순위는 1위부터 50위까지 반복문
				while (bookList.hasNext()) {

					Element bookInfo = bookList.next();
					// 이미지 url
					String image = bookInfo.select("div.cCont_goodsSet p.goods_img span.goods_imgSet span.imgBdr a img")
							.attr("src");
					log.info(image);
					if (image.equals("http://image.yes24.com/sysimage/pd_19_L.gif")) {
						continue;
					}
					if (image.equals("")) 
						continue;
					
					// 제목
					String title = bookInfo.select("div.cCont_goodsSet div.goods_info div.goods_name a").text();
					
					// 링크 url
					String product = bookInfo.select("div.cCont_goodsSet p.goods_img span.goods_imgSet span.imgBdr a")
							.attr("href");
					// 새로 만들어주자
					String link = "http://www.yes24.com" + product;

					Document doc1 = null;
					doc1 = Jsoup.connect(link).get();

					Elements element1 = doc1.select("div#yesWrap");
					// 작가, 출판사, 출간일
					String publish = element1
							.select("div#yDetailTopWrap.tp_book div.topColRgt div.gd_infoTop span.gd_pubArea").text();
					// TTS 가능 여부
					String ttsok = element1.select(
							"#infoset_specific > div.infoSetCont_wrap > div > table > tbody > tr:nth-child(2) > td > ul > li:nth-child(3)")
							.text();
					// 장르
					String genre = element1.select("#infoset_goodsCate > div.infoSetCont_wrap > dl > dd > ul").text();

					String[] apd = new String[3];

					apd = publish.split("\\|");
					
					String auth = apd[0];
					String pub = apd[1].trim();
					String pubdt = apd[2];
			
					String tts = ttsok.replace("TTS 안내", " ");

					bookInfo = null;

					Yes24DTO pDTO = new Yes24DTO();
					pDTO.setTitle(title); // 제목
					pDTO.setAuth(auth); // 저자
					pDTO.setPub(pub);
					pDTO.setPubdt(pubdt);
					pDTO.setGenre(genre); // 장르
					pDTO.setImg(image);
					pDTO.setLink(link);
					pDTO.setTts(tts);

					pList.add(pDTO);

				}
			}
		}
		String colNm = "Yes24BookInfo_" + DateUtil.getDateTime("yyyyMMdd");

		yes24Mapper.createCollection(colNm);

		yes24Mapper.insertBookinfo(pList, colNm);

		log.info(this.getClass().getName() + ".collectYes24book End!");

		return res;
	}

	@Override
	public List<Yes24DTO> bookList() throws Exception {
		log.info(this.getClass().getName() + ".bookList Start!");

		// 조회할 컬렉션 이름
		String colNm = "Yes24BookInfo_20200616";

		List<Yes24DTO> rList = yes24Mapper.bookList(colNm);

		if (rList == null) {
			rList = new ArrayList<Yes24DTO>();
		}

		log.info(this.getClass().getName() + ".bookList End!");
		
		return rList;
	}

	@Override
	public List<Yes24DTO> tts() throws Exception {
		log.info(this.getClass().getName() + ".tts Start!");

		// 조회할 컬렉션 이름
		String colNm = "Yes24BookInfo_20200616";
		String tts = "듣기 가능";

		List<Yes24DTO> rList = yes24Mapper.tts(colNm, tts);

		if (rList == null) {
			rList = new ArrayList<Yes24DTO>();
		}

		log.info(this.getClass().getName() + ".tts End!");

		return rList;
	}

	@Override
	public List<Yes24DTO> search(Map<String, Object> qMap) throws Exception {
		log.info(this.getClass().getName() + ".search start!");
		String colNm = "Yes24BookInfo_20200616";
		
		List<Yes24DTO> rList = yes24Mapper.search(colNm, qMap);
		if (rList == null) {
			rList = new ArrayList<Yes24DTO>();
		}

		log.info(this.getClass().getName() + ".search End!");
		
		return rList;
	}

}
