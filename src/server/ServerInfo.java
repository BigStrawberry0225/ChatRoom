package server;
/*
 * 功能:服务器信息类
 */
public class ServerInfo {
	public static final Integer port=8988;//服务器端口
	private String ip; 
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String name;
}
