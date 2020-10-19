package client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.Socket;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyledDocument;

import client.ManageSocket;
import tools.IOStream;
import tools.InfoStatus;
import tools.UserInfo;

//聊天主界面加好友列表
public class MainFrame extends JFrame{
	
	//定义长和宽
	public static final int FRAME_WIDTH=750;
	public static final int FRAME_HEIGHT=618;
	
	//部分组件
	JTextPane accept;
	JTextPane send;
	JList matelist;
	FriendList friendlist;//好友列表
	NoticeList noticelist;//通知列表
	VoteList voteList;//投票列表
	Socket socket;
	String userName;
	String userID;
	String power;
	JLabel userSelf;//个人标签
	ClientThread ct;
	//四个背景图
	JLabel background;
	JLabel background2;
	JLabel background3;
	JLabel background4;
	//菜单
	JMenuBar bar;
	
	JButton uploadbutton;
	JButton downloadbutton;
	
	public MainFrame(Socket socket,String userName,String userID,String power) {
		
		Socket tempsocket = ManageSocket.getSocket(userName);
		System.out.println(ManageSocket.getSocket(userName));
		this.userID=userID;
		this.socket=socket;
		this.userName=userName;
		this.power=power;
		
		//设置一个右方面板
		JPanel jp2=new JPanel();
		jp2.setLayout(null);
		jp2.setBounds(190,0,550,600);
		this.setLayout(null);
		this.add(jp2);
		//设置一个左方面板
		JPanel jp1=new JPanel();
		jp1.setLayout(null);
		jp1.setBounds(0,0,200,600);
		this.setLayout(null);
		this.add(jp1);
		
		//设置选项卡三个面板
		JPanel chat=new JPanel();
		JPanel file=new JPanel();
		
		chat.setLayout(null);
		file.setLayout(null);
		//为窗体添加背景图
		ImageIcon imageicon=new ImageIcon("src/image/背景.jpg");
		//创建一个标签把图片添加进去
		background=new JLabel(imageicon);
		//设置位置和大小
		background.setBounds(0,0,FRAME_WIDTH,FRAME_HEIGHT);
		//设置布局为空布局
		background.setLayout(null);
		chat.add(background);
		//文件下载
		uploadbutton=new JButton("上传文件");
		uploadbutton.setForeground(Color.black);
		uploadbutton.setFont(new Font("黑体",0,12));
		uploadbutton.setBackground(Color.white);
		uploadbutton.setBounds(340,5,90,25);
		uploadbutton.setOpaque(false);
		uploadbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FileUploadClient(userName).init();
			}
		});
		jp2.add(uploadbutton);
		
		downloadbutton=new JButton("下载文件");
		downloadbutton.setForeground(Color.black);
		downloadbutton.setFont(new Font("黑体",0,12));
		downloadbutton.setBackground(Color.white);
		downloadbutton.setBounds(435,5,90,25);
		downloadbutton.setOpaque(false);
		downloadbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FileDownloadClient().init();
			}
		});
		jp2.add(downloadbutton);
		//个人标签
		if(power.equals("0")) {
			userSelf=new JLabel("欢迎"+userName);
		}
		if(power.equals("1")) {
			userSelf=new JLabel("欢迎"+userName+"(管理员) ");
		}
		userSelf.setBounds(10, 8, 140, 20);
		userSelf.setFont(new Font("黑体",Font.PLAIN,14));
		jp1.setBackground(Color.RED);
		jp1.add(userSelf);
		
		//选项卡
		JTabbedPane tab=new JTabbedPane();
		tab.setBorder(null);
		tab.setBounds(0, 10, 545, 550);
		tab.setFont(new Font("黑体",Font.PLAIN,13));
		tab.add("群聊",chat);//第一个选项卡
		
		//tab.add("公告",announce);
		noticelist = new NoticeList(userName,power);
		voteList = new VoteList(userName,power);
		tab.add("公告",noticelist);
		tab.add("投票",voteList);
		jp2.setBackground(Color.red);
		jp2.add(tab);
		
		//接受框
		accept=new JTextPane();
		accept.setOpaque(false);//设置透明
		accept.setFont(new Font("黑体",Font.PLAIN,15));
		accept.setEditable(false);
		
		//设置接受框滚动条
		JScrollPane acceptscro=new JScrollPane(accept);
		acceptscro.setBounds(0,0,540,340);
		acceptscro.setOpaque(false);
		acceptscro.getViewport().setOpaque(false);//功能是什么
		acceptscro.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		acceptscro.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//输入框
		send=new JTextPane();
		send.setOpaque(false);//设置透明
		send.setFont(new Font("黑体",Font.PLAIN,15));
		
		//设置输入框滚动条
		JScrollPane sendscro=new JScrollPane(send);
		sendscro.setBounds(0,370,535,110);
		sendscro.setOpaque(false);
		sendscro.getViewport().setOpaque(false);//功能是什么
		sendscro.setBorder(null);
		background.add(acceptscro);
		background.add(sendscro);
		
		//好友列表
		friendlist = new FriendList(userName);
		JScrollPane listscro=new JScrollPane(friendlist);
		listscro.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listscro.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		listscro.setOpaque(false);
		listscro.setBounds(0, 35, 190, 525);
		jp1.add(listscro);
		
		//添加图片选择
		JLabel photo=new JLabel(new ImageIcon("src/image/图片.jpg"));
		photo.setBounds(0,340,25,25);
		background.add(photo);
		photo.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("发送图片");
				JFileChooser chooser = new JFileChooser("D://");
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Allmost any Images", "jpg", "gif","BMP", "bmp", "jpg", "JPG","wbmp","jpeg","png","PNG","JPEG","WBMP");
				chooser.setFileFilter(filter);
				int value = chooser.showOpenDialog(MainFrame.this);
                if(value==JFileChooser.APPROVE_OPTION) {
                	File file=chooser.getSelectedFile();
                	System.out.println(file.getAbsolutePath());
                	String path = file.getAbsolutePath();
                	//发送图片地址
                	UserInfo user = new UserInfo();
                	user.setIsFile(1);
                	user.setFilePath(path);
                	user.setSender(userName);
                	user.setAccepter("all");
                	IOStream.sendMessage(socket, user);
                	
                }
			}
		});
		//发送按钮
		background.add(sendButton());
		
		//菜单
		bar = new JMenuBar();
		JMenu menu = new JMenu("[菜单]");
		menu.setFont(new Font("黑体",0,14));
		menu.setForeground(Color.WHITE);
		JMenuItem item = new JMenuItem("");
		item.setFont(new Font("黑体",0,14));
		item.setForeground(Color.WHITE);
		item.setBackground(new Color(150,190,200));
		menu.add(item);
		menu.setBounds(450, 0, 25, 20);
		bar.add(menu);
		bar.setBackground(new Color(150,190,200));
		this.setJMenuBar(bar);
		
		//设置窗体属性
		this.setResizable(false);//窗体不可扩大
		this.setTitle("学生管理系统");
		this.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		
		//获取屏幕长宽
		Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
		int width=screensize.width;
		int height=screensize.height;
		this.setLocation((width-FRAME_WIDTH)/2, (height-FRAME_HEIGHT)/2);//使程序显示在屏幕中间
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭时退出程序
	}
	/*
	 * 发送按钮
	 */
	public JButton sendButton() {
		JButton sendbutton=new JButton("发送");
		sendbutton.setForeground(Color.white);
		sendbutton.setFont(new Font("黑体",0,12));
		sendbutton.setBackground(Color.red);
		sendbutton.setBounds(400,485,125,25);
		sendbutton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String sendText = send.getText();
				UserInfo user=new UserInfo();
				user.setChat(sendText);
				user.setSender(userName);
				user.setAccepter("all");
				user.setInfoStatus(InfoStatus.CHAT);
				//给所有人发消息
				//先发给服务器
				IOStream.sendMessage(socket, user);
				send.setText("");
			}
		});
		return sendbutton;
	}
}