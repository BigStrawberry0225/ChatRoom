package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import client.PaintUI;

/*
 * 功能:用户信息类
 */
public class UserInfo implements Serializable{//实现虚拟化接口,通过io流写实体类时

	private static final long serialVersionUID = -3597400529081552805L;
	private InfoStatus infostatus;//表名消息类型的枚举
	private String userID;
	private String password;
	private String name;
	private String[] onlineusers;//在线列表数组
	private Boolean loginSignal=false;//登录标志
	private String loginStatus;//登录状态
	private String notice;//系统消息
	private String chat;//聊天信息
	private String sender;//接受者
	private String power="0";//管理员权限
	private String noticepad;//公告
	private String padDate;//发公告的日期
	private String owner;//发公告的人
	private String headline;//投票标题
	private int isPaint=0;
	private int isFile=0;
	private String filePath;
	public int getIsPaint() {
		return isPaint;
	}
	public void setIsPaint(int isPaint) {
		this.isPaint = isPaint;
	}
	private int x2;
	private int y2;
	private int x1;
	private int y1;
	private int width;
	private Color color;
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public int getX2() {
		return x2;
	}
	public void setX2(int x2) {
		this.x2 = x2;
	}
	public int getY2() {
		return y2;
	}
	public void setY2(int y2) {
		this.y2 = y2;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	//getset一堆方法
	public String getPadDate() {
		return padDate;
	}
	public void setPadDate(String padDate) {
		this.padDate = padDate;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getNoticepad() {
		return noticepad;
	}
	public void setNoticepad(String noticepad) {
		this.noticepad = noticepad;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getAccepter() {
		return accepter;
	}
	public void setAccepter(String accepter) {
		this.accepter = accepter;
	}
	private String accepter;
	
	public String getChat() {
		return chat;
	}
	public void setChat(String chat) {
		this.chat = chat;
	}
	public String[] getOnlineusers() {
		return onlineusers;
	}
	public void setOnlineusers(String[] onlineusers) {
		this.onlineusers = new String[onlineusers.length];
		for(int i=0;i<onlineusers.length;i++) {
			this.onlineusers[i]=onlineusers[i];
		}
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public InfoStatus getInfoStatus() {
		return infostatus;
	}

	public void setInfoStatus(InfoStatus infostatus) {
		this.infostatus = infostatus;
	}
	public Boolean getLoginSignal() {
		return loginSignal;
	}
	public void setLoginSignal(boolean signal) {
		loginSignal = signal;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}
	//public PaintUI getPaintUI() {
	//	return paintUI;
	//}
	//public void setPaintUI(PaintUI paintUI) {
	//	this.paintUI = paintUI;
	//}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getX1() {
		return x1;
	}
	public void setX1(int x1) {
		this.x1 = x1;
	}
	public int getY1() {
		return y1;
	}
	public void setY1(int y1) {
		this.y1 = y1;
	}
	public int getIsFile() {
		return isFile;
	}
	public void setIsFile(int isFile) {
		this.isFile = isFile;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}