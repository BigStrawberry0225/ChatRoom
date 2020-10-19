package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import server.ManageSocket;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.jdbc.conn.ConnectionClass;
import com.jdbc.conn.InfoOperate;

import tools.IOStream;
import tools.InfoStatus;
import tools.UserInfo;
/*
 *服务器连接客户端线程 
 * 服务器线程:一直读取消息
*/
public class ServerThread extends Thread{
	
	InfoOperate operate;//数据库管理类
	Socket socket;
	ServerFrame serverFrame;//服务器窗体,用于刷新
	static List<String> onlineUsers=new ArrayList<>();//在线用户名称
	static List<Socket> onlineSockets=new ArrayList<>();//在线客户端的socket
	
	public ServerThread(Socket socket,ServerFrame serverFrame) {
		this.socket=socket;//获得客户端返回的socket
		this.serverFrame=serverFrame;
	}
	@Override
	public void run() {
		//实例化一个管理类
		operate = new InfoOperate(new ConnectionClass());
		
		while(true) {
			//接受客户端信息消息
			Object obj = IOStream.readMessage(socket);
			//判断信息类型
			//if(obj instanceof UserInfo) {//如果收到的信息对象是信息UserInfo类的一个实例
				UserInfo user=(UserInfo)obj;//强转类型
				//画板消息类型
				if(user.getIsPaint()==1) {
					locateOne(user);
				}
				//文件
				else if(user.getIsFile()==1) {
					if(user.getAccepter().equals("all")) {
						fileALL(user);
					}
					else {
						fileOne(user);
					}
				}
				else if(user.getInfoStatus()==InfoStatus.LOGIN) {
					settextLog("服务器接收到登录信息");
					String userID = user.getUserID();
					String password = user.getPassword();
					String name=operate.getName(userID);
					user.setName(name);
					user.setUserID(userID);
					//如果账号不可用
					if(!justifyLogin(user)){
						//返回登录失败给客户端
						IOStream.sendMessage(socket, user);
						settextLog("登录失败");
					}
					//如果账号可用但是密码不正确
					else if(justifyLogin(user)&
							!operate.selectPassword(userID, password)){
						user.setInfoStatus(InfoStatus.LOGIN);
						user.setLoginSignal(false);
						user.setLoginStatus("用户名密码不正确");
						IOStream.sendMessage(socket, user);
						settextLog("登录失败");
					}
					//如果账号可用密码正确
					else if(operate.selectPassword(userID, password)&justifyLogin(user)) {
						//返回登录成功给客户端
						//得到是否为管理员
						String power = operate.selectPower(userID);
						user.setPower(power);
						user.setLoginSignal(true);
						user.setInfoStatus(InfoStatus.LOGIN);
						settextLog("输入用户名密码正确");
						//map中添加该通信socket
						System.out.println("-服务器:添加"+name+"的socket");
						ManageSocket.addSocket(name, socket);
						//给客户端发送成功消息
						IOStream.sendMessage(socket, user);
						//统计在线人数
						onlineUsers.add(name);
						onlineSockets.add(socket);
						//刷新用户列表
						flushUserList();
						//发上线系统消息给所有客户端
						user.setInfoStatus(InfoStatus.NOTICE);
						user.setNotice(name+"用户上线");
						System.out.println("-服务器:新用户上线");
						sendALL(user);
						settextLog(user.getNotice());
						//发在线用户列表数组给客户端
						user.setInfoStatus(InfoStatus.LIST);
						String[] users = onlineUsers.toArray(new String[onlineUsers.size()]);
						System.out.println("发送列表信息");
						user.setOnlineusers(users);
						sendALL(user);
					}
				}
				//系统消息类型
				else if(user.getInfoStatus()==InfoStatus.NOTICE) {
					
				}
				//公告消息
				else if(user.getInfoStatus()==InfoStatus.NOTICEPAD) {
					//转发给所有人	
					sendALL(user);
					settextLog(user.getOwner()+"发送公告:"+user.getNoticepad());
					System.out.println(user.getOwner()+"发送公告:"+user.getNoticepad());
					//把公告写到数据库里
					operate.insertNotice(user.getNoticepad(), user.getOwner(), user.getPadDate());
				}
				//聊天消息
				else if(user.getInfoStatus()==InfoStatus.CHAT) {
					//转发给所有人
					if(user.getAccepter().equals("all")) {
						sendALL(user);
						settextLog(user.getSender()+"对所有同学说:"+user.getChat());
					}
					//转发给特定人
					else {
						sendOne(user);
						settextLog(user.getSender()+"对"+user.getAccepter()+"说:"+user.getChat());
					}
				}
				//投票信息
				else if(user.getInfoStatus()==InfoStatus.VOTE) {
					//转发给所有人	
					sendALL(user);
					settextLog(user.getOwner()+"发布投票:"+user.getHeadline());
					System.out.println(user.getOwner()+"发布投票:"+user.getHeadline());
				}
			//}
		}
	}
	//写服务日志
	public void settextLog(String text) {
		JTextPane txtlog = serverFrame.serverUI.txtlog;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		String datestr = dateFormat.format(date);
		String oldText = txtlog.getText();
		txtlog.setText(oldText+datestr+" "+text+"\n");
	}
	//刷新在线用户列表
	public void flushUserList() {
		JList matelist = serverFrame.serverUI.matelist;
		String[] users = onlineUsers.toArray(new String[onlineUsers.size()]);
		for(int i=0;i<users.length;i++) {
			System.out.println("  "+users[i]);
		}
		
		matelist.setListData(users);
		//matelist.setListData(users);
		JTextField number = serverFrame.serverUI.number;
		number.setText(users.length+"人");
	}
	//发送坐标给特定人
	public void locateOne(UserInfo user) {
		Socket tempSocket = ManageSocket.getSocket(user.getAccepter());
		IOStream.sendMessage(tempSocket, user);
	}
	//发消息给特定人
	public void sendOne(UserInfo user) {
		//获取接受对象的socket
		Socket tempSocket = ManageSocket.getSocket(user.getAccepter());
		IOStream.sendMessage(tempSocket, user);
	}
	//发消息给所有人
	public void sendALL(UserInfo user) {
		//遍历
		for(int i=0;i<onlineUsers.size();i++) {
			Socket tempSocket = onlineSockets.get(i);
			IOStream.sendMessage(tempSocket, user);
		}
	}
	//发file给特定人
	public void fileOne(UserInfo user) {
		//获取接受对象的socket
		Socket tempSocket = ManageSocket.getSocket(user.getAccepter());
		IOStream.sendMessage(tempSocket, user);
	}
	//发file给所有人
	public void fileALL(UserInfo user) {
		//遍历
		for(int i=0;i<onlineUsers.size();i++) {
			Socket tempSocket = onlineSockets.get(i);
			IOStream.sendMessage(tempSocket, user);
		}
	}
	//判断登录账号是否可用
	public boolean justifyLogin(UserInfo user) {
		//判断账号是否不存在
		if(!operate.selectID(user.getUserID())) {
			settextLog(user.getName()+"用户不存在");
			user.setLoginSignal(false);
			user.setLoginStatus("用户不存在");
			return false;
		}
		//判断是账号否已登录
		for(int i=0;i<onlineUsers.size();i++) {
			String tempname=onlineUsers.get(i);
			if(user.getName().equals(tempname)) {
				user.setInfoStatus(InfoStatus.LOGIN);
				user.setLoginSignal(false);
				user.setLoginStatus("用户已登录");
				settextLog(user.getName()+"用户已登录");
				IOStream.sendMessage(socket, user);
				return false;
			}
		}
		return true;
	}
}
