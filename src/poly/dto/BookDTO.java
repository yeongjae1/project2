package poly.dto;

public class BookDTO {
	private String title;
	private String auth;
	private String pub;
	private String pubdt;
	private String genre;
	private String tts;
	private int rank;
	private int auth_cnt;
	public int getBook_cnt() {
		return auth_cnt;
	}
	public void setBook_cnt(int auth_cnt) {
		this.auth_cnt = auth_cnt;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getPub() {
		return pub;
	}
	public void setPub(String pub) {
		this.pub = pub;
	}
	public String getPubdt() {
		return pubdt;
	}
	public void setPubdt(String pubdt) {
		this.pubdt = pubdt;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getTts() {
		return tts;
	}
	public void setTts(String tts) {
		this.tts = tts;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
		
	}
}
