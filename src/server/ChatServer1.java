package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextPane;

/*
 * 服务器启动入口
 */
public class ChatServer1 {
	
	ServerFrame serverFrame;
	
	Socket socket=null;
	ServerSocket sso=null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatServer1();
		
	}
	public ChatServer1() {
		new FileServer().init();
		try {
			//建立服务器的Socket监听
			sso=new ServerSocket(ServerInfo.port);
			
			serverFrame = new ServerFrame();
			serverFrame.serverUI.txtlog.setText(">>>服务器已启动\n");
			serverFrame.serverUI.closeBtn.addActionListener(new ActionListener() {	
				public void actionPerformed(ActionEvent e) {
					serverFrame.dispose();
				}
			});
			//实例化一个ServerInfo类
			ServerInfo serverInfo = getServerInfo();
			loadServerInfo(serverInfo);
			
			//解决多客户端使用
			while(true) {
				//等待连接,阻塞实现
				socket=sso.accept();
				System.out.println("服务器接收到客户端连接"+socket);
				//建立该socket的线程
				ServerThread serverThread = new ServerThread(socket,serverFrame);
				System.out.println("服务端获取消息线程启动");
				serverThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				//sso.close();
				//socket.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	 * 功能:填写服务器IP等信息
	 */
	public void loadServerInfo(ServerInfo serverInfo) {
		serverFrame.serverUI.name.setText(serverInfo.getName());
		serverFrame.serverUI.ip.setText(serverInfo.getIp());
	}
	/*
	 * 功能:获取服务器IP等信息
	 */
	public ServerInfo getServerInfo() {
		ServerInfo si=null;
		try {
			InetAddress address = InetAddress.getLocalHost();
			byte[] ipAddress = address.getAddress();
			si=new ServerInfo();
			si.setIp(address.getHostAddress());
			si.setName(address.getHostName());
			System.out.println("服务器的IP:"+(ipAddress[0]&0xff)+"."+(ipAddress[1]&0xff)+"."
								+(ipAddress[2]&0xff)+"."+(ipAddress[3]&0xff));
		} catch (Exception e) {
			System.out.println("###CAN'T GET SERVER IP"+e);
		}
		return si;
	}
}
