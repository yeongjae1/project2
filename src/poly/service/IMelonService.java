package poly.service;

import java.util.List;

import poly.dto.MelonDTO;

public interface IMelonService {
	/**
	 * 멜론 Top100 순위 수집하기
	 */
	public int collectMelonRank() throws Exception;
	
	public List<MelonDTO> getRank() throws Exception;
	
}
