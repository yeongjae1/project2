package poly.persistance.mongo.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import poly.dto.KyoboDTO;
import poly.dto.Yes24DTO;
import poly.persistance.mongo.IYes24Mapper;
import poly.util.CmmUtil;

@Component("Yes24Mapper")
public class Yes24Mapper implements IYes24Mapper {
	
	@Autowired
	private MongoTemplate mongodb;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public boolean createCollection(String colNm) throws Exception {
		log.info(this.getClass().getName() + ".createCollction Start!");
		
		boolean res = false;
		// 기존에 등록된 컬렉션 이름이 존재하는지 체크하고, 존재하면 기존 컬렉션 삭제함
		if (mongodb.collectionExists(colNm)) {
			mongodb.dropCollection(colNm);// 기존 컬렉션 삭제
		}
		// 컬렉션 생성 및 인덱스 생성, MongoDB에서 데이터 가져오는 방식에 맞게 인덱스는 ㅏㅂㄴ드시 생성하자!
		// 데이터 양이 많지 않으면 문제되지 않으나, 최소 10만건 이상 데이터 저장시 속도가 약 10배이상 발생함
		mongodb.createCollection(colNm).createIndex(new BasicDBObject("collect_time", 1).append("rank", 1), "rankIdx");
		
		res = true;
		
		log.info(this.getClass().getName() + ".createCollction End!");
		
		return res;
	
	}

	@Override
	public int insertBookinfo(List<Yes24DTO> pList, String colNm) throws Exception {
		
		log.info(this.getClass().getName() + ".insertBookinfo Start!");
		
		int res = 0;
		
		if (pList == null) {
			pList = new ArrayList<Yes24DTO>();
		}
		Iterator<Yes24DTO> it = pList.iterator();
		
		while (it.hasNext()) {
			Yes24DTO pDTO = (Yes24DTO) it.next();
			
			if (pDTO == null) {
				pDTO = new Yes24DTO();
			}
			mongodb.insert(pDTO, colNm);
		}
		res = 1;
		
		log.info(this.getClass().getName() + ".insertBookinfo End!");
		
		return res;
	}

	@Override
	public List<Yes24DTO> bookList(String colNm) throws Exception {
		
		log.info(this.getClass().getName() + ".bookList start!");
		
		DBCollection rCol = mongodb.getCollection(colNm);
		
		Iterator<DBObject> cursor = rCol.find();
		
		List<Yes24DTO> rList = new ArrayList<Yes24DTO>();
		
		Yes24DTO rDTO = null;
		
		while (cursor.hasNext()) {
			
			rDTO = new Yes24DTO();
			
			final DBObject current = cursor.next();
			
			String title = CmmUtil.nvl((String) current.get("title"));
			String auth = CmmUtil.nvl((String) current.get("auth"));
			String pub = CmmUtil.nvl((String) current.get("pub"));
			String pubdt = CmmUtil.nvl((String) current.get("pubdt"));
			String genre = CmmUtil.nvl((String) current.get("genre"));
			String tts = CmmUtil.nvl((String) current.get("tts"));
			String img = CmmUtil.nvl((String) current.get("img"));
			String link = CmmUtil.nvl((String) current.get("link"));
			
			rDTO.setTitle(title);
			rDTO.setAuth(auth);
			rDTO.setGenre(genre);
			rDTO.setPub(pub);
			rDTO.setPubdt(pubdt);
			rDTO.setTts(tts);
			rDTO.setImg(img);
			rDTO.setLink(link);
			
			rList.add(rDTO);

			rDTO = null;
		}
		
		log.info(this.getClass().getName() + ".bookList End!");
		
		return rList;
	}

	@Override
	public List<Yes24DTO> tts(String colNm, String tts) throws Exception {
		log.info(this.getClass().getName() + ".tts Start!");

		// 데이터 가져올 컬렉션 선택
		DBCollection rCol = mongodb.getCollection(colNm);

		// 쿼리 만들기
		BasicDBObject query = new BasicDBObject();
		query.put("tts", tts);

		// 쿼리 실행하기
		Cursor cursor = rCol.find(query);

		// 컬렉션으로부터 전체 데이터 가져온 것을 List 형태로 저장하기 위한 변수 선언
		List<Yes24DTO> rList = new ArrayList<Yes24DTO>();

		// 가져온 데이터 저장하기
		Yes24DTO rDTO = null;

		while (cursor.hasNext()) {

			rDTO = new Yes24DTO();

			final DBObject current = cursor.next();

			String title = CmmUtil.nvl((String) current.get("title"));
			String auth = CmmUtil.nvl((String) current.get("auth"));
			String pub = CmmUtil.nvl((String) current.get("pub"));
			String pubdt = CmmUtil.nvl((String) current.get("pubdt"));
			String genre = CmmUtil.nvl((String) current.get("genre"));			
			String link = CmmUtil.nvl((String) current.get("link"));
			String img = CmmUtil.nvl((String) current.get("img"));

			log.info("title : " + title);
			rDTO.setTitle(title);
			rDTO.setAuth(auth);
			rDTO.setGenre(genre);
			rDTO.setPub(pub);
			rDTO.setPubdt(pubdt);
			rDTO.setLink(link);
			rDTO.setImg(img);

			rList.add(rDTO);

			rDTO = null;
		}

		log.info(this.getClass().getName() + ".tts End!");

		return rList;
	}

	@Override
	public List<Yes24DTO> search(String colNm, Map<String, Object> qMap) throws Exception {
		log.info(this.getClass().getName() + ".search start!");
		// 데이터 가져올 컬렉션 선택
				DBCollection rCol = mongodb.getCollection(colNm);
				String category = (String) qMap.get("category");
				log.info(category);
				String search = (String) qMap.get("search");
				BasicDBObject query = new BasicDBObject();
				if(category.equals("title")) {					
					query.put("title", Pattern.compile(search));
				} else if (category.equals("auth")) {
					query.put("auth", Pattern.compile(search));
				} else if (category.equals("pub")) {
					query.put("pub", Pattern.compile(search));
				} else {
					query.put("genre", Pattern.compile(search));
				}
				Cursor cursor = rCol.find(query);
				
				// 컬렉션으로부터 전체 데이터 가져온 것을 List 형태로 저장하기 위한 변수 선언
				List<Yes24DTO> qList = new ArrayList<Yes24DTO>();

				// 가져온 데이터 저장하기
				Yes24DTO rDTO = null;

				while (cursor.hasNext()) {

					rDTO = new Yes24DTO();

					final DBObject current = cursor.next();

					String title = CmmUtil.nvl((String) current.get("title"));
					String auth = CmmUtil.nvl((String) current.get("auth"));
					String pub = CmmUtil.nvl((String) current.get("pub"));
					String pubdt = CmmUtil.nvl((String) current.get("pubdt"));
					String genre = CmmUtil.nvl((String) current.get("genre"));			
					String link = CmmUtil.nvl((String) current.get("link"));
					String img = CmmUtil.nvl((String) current.get("img"));

					log.info(title);
					rDTO.setTitle(title);
					rDTO.setAuth(auth);
					rDTO.setGenre(genre);
					rDTO.setPub(pub);
					rDTO.setPubdt(pubdt);
					rDTO.setLink(link);
					rDTO.setImg(img);

					qList.add(rDTO);

					rDTO = null;
				}
				log.info(this.getClass().getName() + ".search end!");
		return qList;
	}
}
