package poly.service;

import java.util.List;
import java.util.Map;

import poly.dto.BookDTO;
import poly.dto.KyoboDTO;

public interface IKyoboService {
	public int collectKyobobook() throws Exception;
	
	//	  책 정보 가져오기
	public List<BookDTO> getBookInfo() throws Exception;
	
	public List<BookDTO> getAuthBook() throws Exception;

	public List<BookDTO> search() throws Exception;
	
	public List<KyoboDTO> bookList() throws Exception;

	public List<KyoboDTO> tts() throws Exception;

	public List<KyoboDTO> search(Map<String, Object> pMap)throws Exception;

	public List<KyoboDTO> random() throws Exception;

	public List<KyoboDTO> title()throws Exception;
}

