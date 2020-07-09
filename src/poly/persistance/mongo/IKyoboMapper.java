package poly.persistance.mongo;

import java.util.List;
import java.util.Map;

import poly.dto.BookDTO;
import poly.dto.KyoboDTO;

public interface IKyoboMapper {
	// 컬렉션 생성
		public boolean createCollection(String colNm) throws Exception;
		// 데이터 저장
		public int insertBookinfo(List<KyoboDTO> pList, String colNm) throws Exception;
		/**
		 * MongoDB 책 데이터 가져오기
		 * @param colNm 가져올 컬렉션 이름
		 * @param auth 저자 이름
		 */
		public List<BookDTO> getBookInfo(String colNm, String auth) throws Exception;
		/**
		 * 작가 별 많이 등록된 순서댈도 가져오기
		 */
		public List<BookDTO> getAuthBook(String colNm) throws Exception;
		
		public List<KyoboDTO> bookList(String colNm) throws Exception;
		
		public List<KyoboDTO> tts(String colNm, String tts) throws Exception;
		
		public List<KyoboDTO> search(String colNm, Map<String, Object> pMap) throws Exception;
		
		public List<KyoboDTO> random(String colNm)throws Exception;
		
		public List<KyoboDTO> title(String colNm)throws Exception;
}
