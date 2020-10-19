package tools;
/*
 * 功能:信息状态枚举类
 */
public enum InfoStatus {
	
	LOGIN(1,"登录消息"),NOTICE(2,"系统消息"),CHAT(3,"聊天消息"),LIST(4,"列表信息"),NOTICEPAD(5,"公告")
	,VOTE(6,"投票");
	
	private int status;
	private String desc;
	
	private InfoStatus(int status,String desc) {
		this.status=status;
		this.desc=desc;
	}
}
