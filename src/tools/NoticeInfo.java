package tools;
/*
 * 公告信息类
 */
public class NoticeInfo {
	private String date;
	private String owner;
	private String notice;
	//构造信息
	public NoticeInfo(String notice,String owner,String date) {
		this.date=date;
		this.owner=owner;
		this.notice=notice;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
}
