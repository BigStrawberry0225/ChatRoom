package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;

import client.ManageSocket;
import tools.IOStream;
import tools.InfoStatus;
import tools.UserInfo;

/*
 * 客户端连接服务器线程
 * 客户端开辟的读取消息的线程
 */

public class ClientThread extends Thread{
	
	Socket socket;
	
	LoginClient loginClient;//登录窗体,用于关闭
	MainFrame mainFrame;//聊天窗体
	
	String username;
	StyledDocument doc;
	Document docs;
	public ClientThread(Socket socket, LoginClient loginClient) {
		this.socket=socket;
		this.loginClient=loginClient;
		// TODO Auto-generated constructor stub
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			//接受消息
			Object obj = IOStream.readMessage(socket);
			//if(obj instanceof UserInfo) {
				UserInfo user=(UserInfo)obj;//强转类型
				//如果接收到登录消息
				if(user.getIsPaint()==1) {
					locateResult(user);
				}
				else if(user.getIsFile()==1) {
					try {
						fileResult(user);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(user.getInfoStatus()==InfoStatus.LOGIN) {
					loginResult(user);
				} 
				//如果接收到列表信息
				else if(user.getInfoStatus()==InfoStatus.LIST) {						
					listResult(user);
				}
				//如果接收到系统通知
				else if(user.getInfoStatus()==InfoStatus.NOTICE) {
					noticeResult(user);
				}
				//如果接受到聊天消息 
				else if(user.getInfoStatus()==InfoStatus.CHAT) {
					chatResult(user);
				}
				//如果接受到公告信息
				else if(user.getInfoStatus()==InfoStatus.NOTICEPAD) {
					noticepadResult(user);
				}
				else if(user.getInfoStatus()==InfoStatus.VOTE) {
					voteResult(user);
				}
			//}
		}
	}
	//处理登录结果方法
	public void loginResult(UserInfo user) {
		System.out.println("-客户端:判断登录结果");
		if(user.getLoginSignal()) {//如果登录成功
			username=user.getName();
			String userID = user.getUserID();
			String power=user.getPower();
			//客户端map添加socket
			ManageSocket.addSocket(username, socket);
			mainFrame = new MainFrame(socket,username,userID,power);
			//传入clientthread
			//关闭登录窗体
			loginClient.dispose();
			//通知别人自己上线
		}
		else {
			System.out.println("-客户端:登录失败");
			JLabel jl=new JLabel(user.getLoginStatus());
			jl.setForeground(Color.red);
			new ReturnFrame(jl);
		}
	}
	//刷新用户列表
	public void listResult(UserInfo user) {
		//获取friendlist
		String[] onlineusers = user.getOnlineusers();
		for(int i=0;i<onlineusers.length;i++) {
			String temp = onlineusers[i];
			System.out.println("-"+temp);
		}
		FriendList friendlist = mainFrame.friendlist;
		//调用更新方法
		friendlist.updateList(user);
	}	
	//系统消息处理
	public void noticeResult(UserInfo user) {
		docs=mainFrame.accept.getDocument();
		try {
			docs.insertString(docs.getLength(),"\n客户端(系统消息):"+user.getNotice(), null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc =mainFrame.accept.getStyledDocument();
	}
	//聊天消息处理
	public void chatResult(UserInfo user) {
		//如果是群聊
		if(user.getAccepter().equals("all")) {
			JTextPane accept = mainFrame.accept;
			docs = accept.getDocument();
			String text = user.getChat();
			 try {
		            docs.insertString(docs.getLength(),"\n"+user.getSender()+":\n"+text, null);//对文本进行追加
		        } catch (BadLocationException e) {
		            e.printStackTrace();
		        }
			//mainFrame.accept.setText(pastText+user.getSender()+":\n"+text+"\n");
			//mainFrame.accept.replaceSelection(pastText+user.getSender()+":\n"+text+"\n");
		}
		//如果是私聊
		else {
			mainFrame.friendlist.noticeList(user.getSender());
			ChatFrame chatFrame = ManageChatFrame.getChat(user.getAccepter()+user.getSender());
			JTextPane accept = chatFrame.accept;
			docs = accept.getDocument();
			String text = user.getChat();
			 try {
		            docs.insertString(docs.getLength(),"\n"+user.getSender()+":\n"+text, null);//对文本进行追加
		        } catch (BadLocationException e) {
		            e.printStackTrace();
		        }
			
		}
	}
	//公告消息处理
	public void noticepadResult(UserInfo user) {
		//获取到noticelist
		JPanel noticepanel = mainFrame.noticelist.noticepanel;
		//新建一个Jtextpane
		JTextPane jtp = new JTextPane();
		jtp.setFont(new Font("黑体",0,12));
		jtp.setText(user.getNoticepad()+"\n发布人:"+user.getOwner()+"\n发布时间:"+user.getPadDate());
		noticepanel.add(jtp);
		noticepanel.updateUI();
	}
	//投票消息处理
	public void voteResult(UserInfo user) {
		//获取到votelist
		JPanel votepanel = mainFrame.voteList.votepanel;
		//新建一个JLabel
		JLabel vl=new VoteLabel(user.getHeadline(),username);
		votepanel.add(vl);
		votepanel.updateUI();
	}
	public void locateResult(UserInfo user) {
		//获取画板
		ChatFrame chatFrame = ManageChatFrame.getChat(user.getAccepter()+user.getSender());
		PaintUI paintUI = chatFrame.paintUI;
		BasicStroke strock = new BasicStroke(user.getWidth());
		paintUI.g.setStroke(strock);
		paintUI.g.setColor(user.getColor());
		paintUI.g.drawLine(user.getX1(), user.getY1(), user.getX2(), user.getY2());
	}
	//文件
	public void fileResult(UserInfo user) throws BadLocationException {
		String path = user.getFilePath();
		ImageIcon image = new ImageIcon(path);
		image.setImage(image.getImage().getScaledInstance(image.getIconWidth()/2, image.getIconHeight()/2,Image.SCALE_DEFAULT ));
		//如果是群聊
		if(user.getAccepter().equals("all")) {
			JTextPane accept = mainFrame.accept;
			//mainFrame.accept.setText(pastText+user.getSender()+":\n");	
			docs=accept.getDocument();
			docs.insertString(docs.getLength(),"\n"+user.getSender()+":\n", null);
			doc = accept.getStyledDocument();
			accept.setCaretPosition(doc.getLength()); // 设置插入位置
			accept.insertIcon(image); // 插入图片
			docs.insertString(docs.getLength(), "\n", null);
		}
		//如果是私聊
		else {
			ChatFrame chatFrame = ManageChatFrame.getChat(user.getAccepter()+user.getSender());
			JTextPane accept = chatFrame.accept;
			docs=accept.getDocument();
			docs.insertString(docs.getLength(), "\n"+user.getSender()+":\n\n", null);
			//String oldText = accept.getText();
			//String info=oldText+user.getSender()+":\n";
			//accept.setText(info);
			doc = accept.getStyledDocument();
			accept.setCaretPosition(doc.getLength()); // 设置插入位置
			accept.insertIcon(image);
			docs.insertString(docs.getLength(), "\n", null);
		}
	}
}