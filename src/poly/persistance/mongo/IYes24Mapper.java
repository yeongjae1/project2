package poly.persistance.mongo;

import java.util.List;
import java.util.Map;

import poly.dto.Yes24DTO;

public interface IYes24Mapper {
	// 컬렉션 생성
	public boolean createCollection(String colNm) throws Exception;
	// 데이터 저장
	public int insertBookinfo(List<Yes24DTO> pList, String colNm) throws Exception;
	
	public List<Yes24DTO> bookList(String colNm) throws Exception;
	public List<Yes24DTO> tts(String colNm, String tts)throws Exception;
	public List<Yes24DTO> search(String colNm, Map<String, Object> qMap) throws Exception;
}
