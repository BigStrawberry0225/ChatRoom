package client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;

import client.ManageSocket;
import tools.IOStream;
import tools.InfoStatus;
import tools.UserInfo;
//私聊界面
public class ChatFrame extends JFrame{
	
	//定义长和宽
	public static final int FRAME_WIDTH=552;
	public static final int FRAME_HEIGHT=575;
	
	//部分组件
	JTextPane accept;
	JTextPane send;
	String userName;//用户
	String accepter;
	JTabbedPane tab;//选项卡
	
	
	Socket socket;
	//最新消息
	UserInfo user;
	JLabel emoji;//画板图标
	JLabel photo;
	PaintUI paintUI;
	private Document docs;
	private StyledDocument doc;
	
	public ChatFrame(String userName,String accepter) {
		System.out.println("chatframe");
		this.userName=userName;
		this.accepter=accepter;
		Container contentPane = this.getContentPane();
		contentPane.setLayout(null);
		contentPane.setBackground(Color.WHITE);
		ImageIcon imageicon=new ImageIcon("src/image/背景.jpg");
		//创建一个标签把图片添加进去
		JLabel background=new JLabel(imageicon);
		background.setBounds(0,25,ChatFrame.FRAME_WIDTH,ChatFrame.FRAME_HEIGHT);
		//设置布局为空布局
		background.setLayout(null);
		contentPane.add(background);
		//头像+名字
		JLabel head=new JLabel(new ImageIcon("src/image/在线.jpg"));
		head.setBounds(0, 0, 25, 25);
		contentPane.add(head);
		JLabel accepterr_label=new JLabel(accepter);
		accepterr_label.setFont(new Font("黑体",0,14));
		accepterr_label.setBounds(30, 0, 100, 25);
		contentPane.add(accepterr_label);
		//接受框
		accept=new JTextPane();
		accept.setEditable(false);
		accept.setOpaque(false);//设置透明
		accept.setFont(new Font("黑体",Font.PLAIN,15));
		accept.setSize(100, 200);
		//设置接受框滚动条
		JScrollPane acceptscro=new JScrollPane(accept);
		acceptscro.setBounds(2,0,535,340);
		acceptscro.setOpaque(false);
		acceptscro.getViewport().setOpaque(false);
		//输入框
		send=new JTextPane();
		send.setOpaque(false);//设置透明
		send.setFont(new Font("黑体",Font.PLAIN,15));
		send.setSize(100, 200);
		//设置输入框滚动条
		JScrollPane sendscro=new JScrollPane(send);
		sendscro.setBounds(0,370,530,110);
		sendscro.setOpaque(false);
		sendscro.getViewport().setOpaque(false);
		sendscro.setBorder(null);
		background.add(acceptscro);
		background.add(sendscro);
		//添加图片选择
		photo=new JLabel(new ImageIcon("src/image/图片.jpg"));
		photo.setBounds(0,340,25,25);
		background.add(photo);
		emoji=new JLabel(new ImageIcon("src/image/画板.jpg"));
		emoji.setBounds(30,340,25,25);
		background.add(emoji);
		//发送按钮
		background.add(sendButton());
		
		//为画板注册监听
		emoji.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				UserInfo user = new UserInfo();
				//user.setInfoStatus(InfoStatus.PAINT);
				user.setSender(userName);
				user.setAccepter(accepter);
				socket = ManageSocket.getSocket(userName);
				//IOStream.sendMessage(socket, user);
				paintUI = new PaintUI(userName,accepter);
			}
		});
		//为发送图片注册监听
		photo.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("发送图片");
				JFileChooser chooser = new JFileChooser("D://");
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Allmost any Images", "jpg", "gif","BMP", "bmp", "jpg", "JPG","wbmp","jpeg","png","PNG","JPEG","WBMP");
				chooser.setFileFilter(filter);
				int value = chooser.showOpenDialog(ChatFrame.this);
                if(value==JFileChooser.APPROVE_OPTION) {
                	File file=chooser.getSelectedFile();
                	System.out.println(file.getAbsolutePath());
                	String path = file.getAbsolutePath();
                	//发送图片地址
                	UserInfo user = new UserInfo();
                	user.setIsFile(1);
                	user.setFilePath(path);
                	user.setSender(userName);
                	user.setAccepter(accepter);
                	socket = ManageSocket.getSocket(userName);
                	IOStream.sendMessage(socket, user);
                	docs=accept.getDocument();
        			try {
						docs.insertString(docs.getLength(),"\n"+user.getSender()+":\n", null);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        			doc = accept.getStyledDocument();
                	doc = accept.getStyledDocument();
                	ImageIcon image = new ImageIcon(path);
                	image.setImage(image.getImage().getScaledInstance(image.getIconWidth()/2, image.getIconHeight()/2,Image.SCALE_DEFAULT ));
                	accept.setCaretPosition(doc.getLength()); // 设置插入位置
                	accept.insertIcon(image); // 插入图片
                	try {
						docs.insertString(docs.getLength(), "\n", null);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
			}
		});
		//设置窗体属性
		this.setResizable(false);//窗体不可扩大
		this.setTitle(userName+"的聊天框");
		this.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		//获取屏幕长宽
		Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
		int width=screensize.width;
		int height=screensize.height;
		this.setLocation((width-FRAME_WIDTH)/2, (height-FRAME_HEIGHT)/2);//使程序显示在屏幕中间
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); //关闭时隐藏程序
	}
	public JButton sendButton() {
		JButton sendbutton=new JButton("发送");
		sendbutton.setForeground(Color.white);
		sendbutton.setFont(new Font("黑体",0,12));
		sendbutton.setBackground(new Color(150,190,200));
		sendbutton.setBounds(400,485,125,25);
		sendbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//获取输入框信息
				String sendText = send.getText();
				//设置最新消息属性
				user=new UserInfo();
				user.setSender(userName);
				user.setAccepter(accepter);
				user.setInfoStatus(InfoStatus.CHAT);
				user.setChat(sendText);
					//显示消息
				docs = accept.getDocument();
				try {
					docs.insertString(docs.getLength(),"\n"+user.getSender()+":\n"+sendText, null);//对文本进行追加
				} catch (BadLocationException e1) {
				    e1.printStackTrace();
				}
				send.setText("");
				//通过发送者socket给服务端发消息
				socket = ManageSocket.getSocket(userName);
				System.out.println(ManageSocket.getSocket(userName));
				IOStream.sendMessage(socket, user);
			}
		});
		return sendbutton;
	}
	public void openPaint() {
		paintUI=new PaintUI(accepter,userName);
	}
}