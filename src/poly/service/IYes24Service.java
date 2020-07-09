package poly.service;

import java.util.List;
import java.util.Map;

import poly.dto.Yes24DTO;

public interface IYes24Service {
	// 예스24 책 수집
	public int collectYes24book() throws Exception;

	public List<Yes24DTO> bookList() throws Exception;

	public List<Yes24DTO> tts() throws Exception;

	public List<Yes24DTO> search(Map<String, Object> qMap) throws Exception;
}
