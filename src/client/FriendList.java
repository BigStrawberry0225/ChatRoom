package client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;

import com.jdbc.conn.ConnectionClass;
import com.jdbc.conn.InfoOperate;

import tools.UserInfo;

public class FriendList extends JPanel implements MouseListener{
	
	InfoOperate infooperate;
	String userName;
	private String[] friendsname;//名字数组
	private JLabel[] friendslist;//label数组
	ArrayList<String> userlist;
	
	public FriendList(String userName) {
		this.userName=userName;
		this.setBackground(Color.white);
		//实例化一个管理类获取所有用户的集合
		infooperate=new InfoOperate(new ConnectionClass());
		userlist = infooperate.getUserlist();
		//集合中去除自己的名字
		userlist.remove(userName);
		getLabellist();
		setLayout(new GridLayout(100, 1,0,5));
	}
	//创建列表
	public void getLabellist() {
		//将集合转变为数组,并根据数组更新jlabel集合
		friendsname= userlist.toArray(new String[userlist.size()]);
		friendslist=new JLabel[userlist.size()];
		for(int i=0;i<userlist.size();i++) {
			JLabel templabel = friendLabel(friendsname[i]);
			templabel.setEnabled(false);
			templabel.addMouseListener(this);
			templabel.setBackground(Color.WHITE);
			friendslist[i]=templabel;
			add(friendslist[i]);
		}
	}
	//更新在线好友情况
	public void updateList(UserInfo user) {
		//获取到在线的好友
		String[] onlineusers = user.getOnlineusers();
		for(int i=0;i<onlineusers.length;i++) {
			String temp = onlineusers[i];
			//遍历集合
			for(int j=0;j<userlist.size();j++) {
				//获取到在线时
				if(userlist.get(j).equals(temp)) {
					//记录下标
					int index=j;
					//改变该用户为彩色
					friendslist[index].setEnabled(true);
				}
			}
		}
	}
	//收到消息时修改好友列表
	public void noticeList(String sender) {
		//removeAll();
		//updateUI();
		//遍历集合,将sender修改为(有新消息)
		for(int i=0;i<userlist.size();i++) {
			String temp = userlist.get(i);
			if(temp.equals(sender)) {
				userlist.set(i, temp+"~(有新消息)");
				friendslist[i].setText(temp+"~(有新消息)");
				//friendslist[i].setEnabled(true);
			}
		}
		//getLabellist();
	}
	//收到消息后修改好友列表
	public void unnoticeList(String sender) {
		//遍历集合,将sender修改为(有新消息)
		for(int i=0;i<userlist.size();i++) {
			String temp = userlist.get(i);
			if(temp.equals(sender+"~(有新消息)")) {
				userlist.set(i, sender);
				friendslist[i].setText(sender);
				//friendslist[i].setEnabled(true);
			}
		}
	}
	public JLabel friendLabel(String name) {
		ImageIcon head=new ImageIcon("src/image/在线.jpg");
		JLabel jl=new JLabel(name);
		jl.setFont(new Font("黑体",0,14));
		return jl;
	}
	//双击事件
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount()==2) {
			String str=((JLabel)e.getSource()).getText();
			//获取~前的用户名
			String[] split = str.split("~");
			String friendname = split[0];
			System.out.println(friendname);
			//打开私聊界面
			ChatFrame chatFrame = new ChatFrame(userName,friendname);
			//把聊天界面加入到管理类
			ManageChatFrame.addChat(userName+""+friendname, chatFrame);
			//更新label
			unnoticeList(friendname);
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel jl=(JLabel)e.getSource();
		jl.setForeground(new Color(150,190,200));//蓝色
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel jl=(JLabel)e.getSource();
		jl.setForeground(Color.black);
	}
	public String[] getFriendsname() {
		return friendsname;
	}
	public void setFriendsname(String[] friendsname) {
		this.friendsname = friendsname;
	}
	public JLabel[] getFriendslist() {
		return friendslist;
	}
	public void setFriendslist(JLabel[] friendslist) {
		this.friendslist = friendslist;
	}
}