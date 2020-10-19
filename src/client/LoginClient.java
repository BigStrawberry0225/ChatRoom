package client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.*;

import server.ChatServer1;
import client.ManageSocket;
import server.ServerInfo;
import tools.IOStream;
import tools.InfoStatus;
import tools.UserInfo;

import com.jdbc.conn.ConnectionClass;
import com.jdbc.conn.InfoOperate;
import com.mysql.fabric.xmlrpc.Client;
public class LoginClient extends JFrame {
	JTextField usernumberText;
	JPasswordField passwordText;
	JButton loginButton;
	JButton cancelButton;
	JButton registerButton;
	JLabel usernameLabel;
	JLabel passwordLabel;
	public static void main(String []args) {
		LoginClient loginclient=new LoginClient();
	}
	
	public LoginClient() {
		
		usernameLabel = new JLabel("User Number");
		usernameLabel.setBounds(130, 100, 120, 35);  //设置位置大小
		usernameLabel.setForeground(Color.WHITE);
		usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		this.add(usernameLabel);
		
		usernumberText = new JTextField();
		usernumberText.setBounds(250, 100, 150, 35);
		usernumberText.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		usernumberText.setOpaque(false);
		usernumberText.setForeground(Color.white);
//		usernameText.setBorder(null);
		this.add(usernumberText);
		
		passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(150, 150, 150, 35);
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		this.add(passwordLabel);
		
		passwordText = new JPasswordField();
		passwordText.setBounds(250, 150, 150, 35);
		passwordText.setOpaque(false);
		passwordText.setForeground(Color.WHITE);
//		passwordText.setBorder(null);
		this.add(passwordText);
		
		loginButton = new JButton("Enter");
		loginButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		loginButton.setBounds(170, 280, 100, 35);
		loginButton.setForeground(Color.WHITE);
		loginButton.setOpaque(false);
		loginButton.setContentAreaFilled(false);
		
		this.add(loginButton);
		registerButton = new JButton("Register");
		registerButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		registerButton.setBounds(430, 100, 110, 35);
		registerButton.setForeground(Color.WHITE);
		registerButton.setOpaque(false);
		registerButton.setContentAreaFilled(false);
		this.add(registerButton);
		

		cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("微软雅黑",Font.PLAIN,18));
        cancelButton.setBounds(300, 280, 100, 35);
        cancelButton.setOpaque(false);
        cancelButton.setContentAreaFilled(false);
        cancelButton.setForeground(Color.WHITE);
//        cancelButton.setBorderPainted(false);
        this.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
    		
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "welcome again");
				System.exit(0);
			}
		});
        this.add(new LoginPanel());

        //注册监听
      //监听
       loginButton.addActionListener(new ActionListener() {
    	   public void actionPerformed(ActionEvent e) {
    		   //新建一个用户信息类
    		   UserInfo user = new UserInfo();
    		   user.setUserID(usernumberText.getText());
    		   user.setPassword(new String(passwordText.getPassword()));
    		   user.setInfoStatus(InfoStatus.LOGIN);
    		   if(user.getUserID().equals("")|user.getPassword().equals("")) {
    			   JLabel jl=new JLabel("信息不能为空");
    			   jl.setForeground(Color.red);
    			   new ReturnFrame(jl);
    		   }
			else{//信息不为空,连接服务器发送信息
			System.out.println("ID:"+user.getUserID());
				connServer(user);
				}
			}
		});
        registerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				new RegistFrame();
			}
		});
        //窗体属性
		Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(600,400);
		this.setLocationRelativeTo(null);
		this.setResizable(false);//窗体不可扩大
		this.setUndecorated(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭时退出程序
	}
	public class LoginPanel extends JPanel {
		private Image image;
		public LoginPanel() {
			try {
				image = ImageIO.read(new File("src/image/a2.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void paint (Graphics g) {
			super.paint(g);
			g.drawImage(image, 0, 0, 600, 400, null);
			
		}
	}
/*
 * 登录连接服务器
 */
	public void connServer(UserInfo user) {
		try {
			//连接
			Socket socket=new Socket("127.0.0.1",ServerInfo.port);//服务器端给的socket
			System.out.println("已连接");
			//发送用户信息
			IOStream.sendMessage(socket, user);
			//开辟客户端线程
			ClientThread clientThread = new ClientThread(socket,this);
			clientThread.start();
			System.out.println("客户端线程启动之后");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}