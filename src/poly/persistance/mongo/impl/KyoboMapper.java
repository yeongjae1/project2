package poly.persistance.mongo.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import poly.dto.BookDTO;
import poly.dto.KyoboDTO;
import poly.persistance.mongo.IKyoboMapper;
import poly.util.CmmUtil;

@Component("KyoboMapper")
public class KyoboMapper implements IKyoboMapper {

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
	public int insertBookinfo(List<KyoboDTO> pList, String colNm) throws Exception {

		log.info(this.getClass().getName() + ".insertBookinfo Start!");

		int res = 0;

		if (pList == null) {
			pList = new ArrayList<KyoboDTO>();
		}
		Iterator<KyoboDTO> it = pList.iterator();

		while (it.hasNext()) {
			KyoboDTO pDTO = (KyoboDTO) it.next();

			if (pDTO == null) {
				pDTO = new KyoboDTO();
			}
			mongodb.insert(pDTO, colNm);
		}
		res = 1;

		log.info(this.getClass().getName() + ".insertBookinfo End!");

		return res;
	}

	@Override
	public List<BookDTO> getBookInfo(String colNm, String auth) throws Exception {

		log.info(this.getClass().getName() + ".getBookInfo Start!");

		// 데이터 가져올 컬렉션 선택
		DBCollection rCol = mongodb.getCollection(colNm);

		// 쿼리 만들기
		BasicDBObject query = new BasicDBObject();
		query.put("auth", auth);

		// 쿼리 실행하기
		Cursor cursor = rCol.find(query);

		// 컬렉션으로부터 전체 데이터 가져온 것을 List 형태로 저장하기 위한 변수 선언
		List<BookDTO> rList = new ArrayList<BookDTO>();

		// 가져온 데이터 저장하기
		BookDTO rDTO = null;

		while (cursor.hasNext()) {

			rDTO = new BookDTO();

			final DBObject current = cursor.next();

			String title = CmmUtil.nvl((String) current.get("title"));
			String pub = CmmUtil.nvl((String) current.get("pub"));
			String pubdt = CmmUtil.nvl((String) current.get("pubdt"));
			String genre = CmmUtil.nvl((String) current.get("genre"));
			String tts = CmmUtil.nvl((String) current.get("tts"));

			log.info("title : " + title);
			rDTO.setTitle(title);
			rDTO.setAuth(auth);
			rDTO.setGenre(genre);
			rDTO.setPub(pub);
			rDTO.setPubdt(pubdt);
			rDTO.setTts(tts);

			rList.add(rDTO);

			rDTO = null;
		}

		log.info(this.getClass().getName() + ".getBookInfo End!");

		return rList;
	}

	@Override
	public List<BookDTO> getAuthBook(String colNm) throws Exception {

		log.info(this.getClass().getName() + ".getAuthBook Start!");

		// 데이터 가져올 컬렉션 선택
		DBCollection rCol = mongodb.getCollection(colNm);

		// 쿼리 만들기
		List<DBObject> pipeline = Arrays.asList(
				// SQL의 Group by와 같은 역할을 수행하는 group 함수 호출
				new BasicDBObject().append("$group",
						new BasicDBObject().append("_id", new BasicDBObject().append("auth", "$auth") // 그룹으로 묶을 필드
						)
								// 그룹으로 묶인 함수를 통해 계산될 내용 (레코드수 세기)
								.append("COUNT(auth)", new BasicDBObject().append("$sum", 1))),
				// project는 결과 보여줄 내용, SQL의 select와 from 절 사이 내용으로 이해하자
				new BasicDBObject().append("$project",
						new BasicDBObject().append("auth", "$_id.auth").append("auth_cnt", "$COUNT(auth)").append("_id",
								0)),
				// SQL의 order by 와 같은 역할을 하는 sort 함수 호출
				// 1차 정렬은 카운트 큰 순서, 2차 정렬은 가나다
				new BasicDBObject().append("$sort", new BasicDBObject().append("book_cnt", -1).append("auth", 1)));
		AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();

		Cursor cursor = rCol.aggregate(pipeline, options);

		// 컬렉션으로부터 전체 데이터 가져온 것을 List 형태로 저장하기 위한 변수 선언
		List<BookDTO> rList = new ArrayList<BookDTO>();

		// 가져온 데이터 저장하기
		BookDTO rDTO = null;

		int rank = 1;

		while (cursor.hasNext()) {

			rDTO = new BookDTO();

			final DBObject current = cursor.next();

			String auth = CmmUtil.nvl((String) current.get("auth"));
			int auth_cnt = (int) current.get("auth_cnt");
			log.info("auth : " + auth);
			log.info("auth_cnt : " + auth_cnt);

			rDTO.setRank(rank);
			rDTO.setAuth(auth);
			rDTO.setBook_cnt(auth_cnt);

			rList.add(rDTO);

			rDTO = null;

			rank++;
		}

		log.info(this.getClass().getName() + ".getAuthBook End!");

		return rList;
	}

	@Override
	public List<KyoboDTO> bookList(String colNm) throws Exception {

		log.info(this.getClass().getName() + ".bookList start!");

		DBCollection rCol = mongodb.getCollection(colNm);

		Iterator<DBObject> cursor = rCol.find();

		List<KyoboDTO> rList = new ArrayList<KyoboDTO>();

		KyoboDTO rDTO = null;

		while (cursor.hasNext()) {

			rDTO = new KyoboDTO();

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
	public List<KyoboDTO> tts(String colNm, String tts) throws Exception {
		log.info(this.getClass().getName() + ".tts Start!");

		// 데이터 가져올 컬렉션 선택
		DBCollection rCol = mongodb.getCollection(colNm);

		// 쿼리 만들기
		BasicDBObject query = new BasicDBObject();
		query.put("tts", tts);

		// 쿼리 실행하기
		Cursor cursor = rCol.find(query);

		// 컬렉션으로부터 전체 데이터 가져온 것을 List 형태로 저장하기 위한 변수 선언
		List<KyoboDTO> rList = new ArrayList<KyoboDTO>();

		// 가져온 데이터 저장하기
		KyoboDTO rDTO = null;

		while (cursor.hasNext()) {

			rDTO = new KyoboDTO();

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
	public List<KyoboDTO> search(String colNm, Map<String, Object> pMap) throws Exception {
		log.info(this.getClass().getName() + ".search start!");
		// 데이터 가져올 컬렉션 선택
				DBCollection rCol = mongodb.getCollection(colNm);
				String category = (String) pMap.get("category");
				log.info(category);
				String search = (String) pMap.get("search");
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
				List<KyoboDTO> rList = new ArrayList<KyoboDTO>();

				// 가져온 데이터 저장하기
				KyoboDTO rDTO = null;

				while (cursor.hasNext()) {

					rDTO = new KyoboDTO();

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

					rList.add(rDTO);

					rDTO = null;
				}
				log.info(this.getClass().getName() + ".search end!");
		return rList;
	}

	@Override
	public List<KyoboDTO> random(String colNm) throws Exception {
		log.info(this.getClass().getName() + ".tts Start!");

		// 데이터 가져올 컬렉션 선택
		DBCollection rCol = mongodb.getCollection(colNm);

		// 쿼리 만들기
		BasicDBObject query = new BasicDBObject();
		query.put("pub", new BasicDBObject()
                .append("$not", new BasicDBObject()
                        .append("$in", Arrays.asList(
                                " \uAD6C\uD150\uBCA0\uB974\uD06C \uD504\uB85C\uC81D\uD2B8 "
                            )
                        )
                )
        );

		// 쿼리 실행하기
		Cursor cursor = rCol.find(query);

		// 컬렉션으로부터 전체 데이터 가져온 것을 List 형태로 저장하기 위한 변수 선언
		List<KyoboDTO> rList = new ArrayList<KyoboDTO>();

		// 가져온 데이터 저장하기
		KyoboDTO rDTO = null;

		while (cursor.hasNext()) {

			rDTO = new KyoboDTO();

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
	public List<KyoboDTO> title(String colNm) throws Exception {
		log.info(this.getClass().getName() + ".title start!");

		DBCollection rCol = mongodb.getCollection(colNm);

		Iterator<DBObject> cursor = rCol.find();

		List<KyoboDTO> rList = new ArrayList<KyoboDTO>();

		KyoboDTO rDTO = null;

		while (cursor.hasNext()) {

			rDTO = new KyoboDTO();

			final DBObject current = cursor.next();

			String title = CmmUtil.nvl((String) current.get("title"));
			
			rDTO.setTitle(title);
			
			rList.add(rDTO);

			rDTO = null;
		}

		log.info(this.getClass().getName() + ".title End!");

		return rList;
	}
}
