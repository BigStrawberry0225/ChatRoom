package client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.jdbc.conn.ConnectionClass;
import com.jdbc.conn.InfoOperate;

/*
 * 注册界面框架
 */
public class RegistFrame extends JFrame{
//	JPanel jp1;
//	JLabel jl1;
//	JLabel jl2;//学号
//	JLabel jl4;//name
//	JLabel jl5;//性别
//	JComboBox jcb;//男女框
//	JLabel jl3;//密码
//	JLabel jl6;//确认密码
//	JTextField number;
//	JTextField namein;
//	JPasswordField password1;
//	JPasswordField password2;
//	
//	JButton jb;//注册按钮
//	
	
	JTextField usernumberText;
	JTextField passwordText;
	JTextField confirmText;
	JTextField usernameText;
	JTextField genderText;
	
	JButton registerButton;
	JButton cancelButton;
	
	JLabel usernumberLabel;
	JLabel passwordLabel;
	JLabel confirmLabel;
	JLabel usernameLabel;
	JLabel genderLabel;
	
	String userID;
	String password;
	String confirmpassword;
	String sex;
	String name;
	int power=0;
	public static void main(String[] args) {
		new RegistFrame();
	}
	public RegistFrame() {
		
		usernumberLabel = new JLabel("User Number");
		usernumberLabel.setBounds(20, 50, 120, 35); 
		usernumberLabel.setForeground(Color.WHITE);
		usernumberLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		this.add(usernumberLabel);
		
		usernumberText = new JTextField();
		usernumberText.setBounds(140, 50, 100, 35);
		usernumberText.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		usernumberText.setOpaque(false);
		usernumberText.setForeground(Color.WHITE);
//		usernumberText.setBorder(null);
		this.add(usernumberText);
		
		passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(30, 100, 150, 35);
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		this.add(passwordLabel);
		
		passwordText = new JTextField();
		passwordText.setBounds(140, 100, 100, 35);
		passwordText.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		passwordText.setOpaque(false);
		passwordText.setForeground(Color.white);
//		passwordText.setBorder(null);
		this.add(passwordText);
		
		confirmLabel = new JLabel("Confirm");
		confirmLabel.setBounds(30, 150, 150, 35);
		confirmLabel.setForeground(Color.WHITE);
		confirmLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		this.add(confirmLabel);
		confirmText = new JTextField();
		confirmText.setBounds(140, 150, 100, 35);
		confirmText.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		confirmText.setOpaque(false);
		confirmText.setForeground(Color.WHITE);
//		confirmText.setBorder(null);
		this.add(confirmText);
		
		
		usernameLabel = new JLabel("Your Name");
		usernameLabel.setBounds(30, 200, 120, 35);
		usernameLabel.setForeground(Color.WHITE);
		usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		this.add(usernameLabel);
		
		usernameText = new JTextField();
		usernameText.setBounds(140, 200, 100, 35);
		usernameText.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		usernameText.setOpaque(false);
		usernameText.setForeground(Color.WHITE);
//		usernameText.setBorder(null);
		this.add(usernameText);
		
		genderLabel = new JLabel("Your Gender");
		genderLabel.setBounds(30, 250, 120, 35);
		genderLabel.setForeground(Color.WHITE);
		genderLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		this.add(genderLabel);
		
		genderText = new JTextField();
		genderText.setBounds(140, 250, 100, 35);
		genderText.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		genderText.setOpaque(false);
		genderText.setForeground(Color.white);
//		genderText.setBorder(null);
		this.add(genderText);

		registerButton = new JButton("Finish");
		registerButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		registerButton.setBounds(35, 380, 100, 35);
		registerButton.setForeground(Color.WHITE);
		registerButton.setOpaque(false);
		registerButton.setContentAreaFilled(false);
		this.add(registerButton);
		
		cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("微软雅黑",Font.PLAIN,18));
        cancelButton.setBounds(165, 380, 100, 35);
        cancelButton.setOpaque(false);
        cancelButton.setContentAreaFilled(false);
        cancelButton.setForeground(Color.WHITE);
        this.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				
			}
		});
        
        //注册监听
        registerButton.addActionListener(new ActionListener() {//为注册按钮注册监听
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//获取填写的信息
				userID=usernumberText.getText();
				password=passwordText.getText();
				confirmpassword=confirmText.getText();
				sex=genderText.getText();
				name=usernameText.getText();
				
				if(userID.equals("")|password.equals("")|sex.equals("")|confirmpassword.equals("")) {//如果任何一项为空
					JLabel jl=new JLabel("不能为空");
					jl.setForeground(Color.red);
					new ReturnFrame(jl);
				}
				else {
					//实例化一个管理类
					InfoOperate userInfo = new InfoOperate(new ConnectionClass());
					if(!userInfo.selectID(userID)) {//用户名不重复
						if(userInfo.confirmPassword(password, confirmpassword)) {//如果两次密码为真
							userInfo.insertUser(userID, password,name,sex,power);//添加新用户
							JLabel jl = new JLabel("注册成功!");
							jl.setForeground(Color.red);
							new ReturnFrame(jl);
							}
						else {//如果两次密码不一致
							JLabel jl = new JLabel("确认密码不正确");
							jl.setForeground(Color.red);
							new ReturnFrame(jl);
							}
						}
					else {//用户名重复
						JLabel jl = new JLabel("该用户已存在,不可以注册");
						jl.setForeground(Color.red);
						new ReturnFrame(jl);
					}
				}
			}
		});
//		JPanel jp1=new JPanel();//注册面板
//		jp1.setLayout(null);
//		jp1.setBounds(20,10,250,340);
//		jp1.setFont(new Font("黑体",0,16));
//		jp1.setBorder(BorderFactory.createCompoundBorder(
//				BorderFactory.createTitledBorder(""),
//				BorderFactory.createEmptyBorder(1,1,1,1)
//				));
//		jp1.setBackground(Color.WHITE);
//		
//		//欢迎
//		jl1=new JLabel("欢迎注册新用户");
//		jl1.setFont(new Font("黑体",0,20));
//		jl1.setBounds(30,20,200,50);
//		jp1.add(jl1);
//		//标题标签
//		Font font=new Font("黑体",0,14);
//		jl2=new JLabel("学号");
//		jl2.setFont(font);
//		jl2.setBounds(30, 100, 50, 20);
//		jp1.add(jl2);
//		number=new JTextField(10);
//		number.setBounds(80, 100, 130, 20);
//		jp1.add(number);
//		//姓名
//		jl4=new JLabel("姓名");
//		jl4.setFont(font);
//		jl4.setBounds(30, 140, 100, 20);
//		jp1.add(jl4);
//		namein=new JTextField(10);
//		namein.setBounds(80, 140, 60, 20);
//		jp1.add(namein);
//		//性别
//		jl5=new JLabel("性别");
//		jl5.setFont(font);
//		jl5.setBounds(150, 140, 50, 20);
//		jp1.add(jl5);
//		//性别框
//		String[] sex= {"男","女"};
//		jcb=new JComboBox(sex);
//		jcb.setBounds(180,140,40,20);
//		jcb.setFont(new Font("黑体",0,12));
//		jcb.setBackground(Color.white);
//		jp1.add(jcb);
//		//密码
//		jl3=new JLabel("密码");
//		jl3.setFont(font);
//		jl3.setBounds(30, 180, 50, 20);
//		jp1.add(jl3);
//		password1=new JPasswordField(10);
//		password1.setBounds(80, 180, 130, 20);
//		jp1.add(password1);
//		//确认密码
//		jl6=new JLabel("确认密码");
//		jl6.setFont(font);
//		jl6.setBounds(15, 220, 100, 20);
//		jp1.add(jl6);
//		password2=new JPasswordField(10);
//		password2.setBounds(80, 220, 130, 20);
//		jp1.add(password2);
//		
//		jb=registBtn();//注册按钮模块
//		this.add(jb);
//		this.setLayout(null);
//		this.add(jp1);
		//创建窗口
		this.setTitle("注册");
		this.setSize(300, 500);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); 
		//获取屏幕长宽
		Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
		int width=screensize.width;
		int height=screensize.height;
		this.setLocation((width-300)/2, (height-400)/2);//使程序显示在屏幕中间
		this.add(new RegisterPanel());
		this.setUndecorated(true);
		this.setVisible(true);
		//关闭时退出程序
	}
	public class RegisterPanel extends JPanel {
		private Image image;
		public RegisterPanel() {
			try {
				image = ImageIO.read(new File("src/image/r.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void paint (Graphics g) {
			super.paint(g);
			g.drawImage(image, 0, 0, 300, 500, null);
			
		}
	}
	//注册按钮
//	public JButton registBtn() {	
//		JButton registBtn=new JButton("完成注册");
//		registBtn.setFont(new Font("黑体",0,14));
//		registBtn.setBounds(85, 280, 120, 25);
//		registBtn.setBackground(new Color(150,190,220));
//		registBtn.setForeground(Color.WHITE);
//		registBtn.addActionListener(new ActionListener() {//为注册按钮注册监听
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				
//				//获取填写的信息
//				userID=number.getText();
//				password=new String(password1.getPassword());
//				confirmpassword=new String(password2.getPassword());
//				sex=(String)jcb.getSelectedItem();
//				name=namein.getText();
//				
//				if(userID.equals("")|password.equals("")|sex.equals("")|confirmpassword.equals("")) {//如果任何一项为空
//					JLabel jl=new JLabel("不能为空");
//					jl.setForeground(Color.red);
//					new ReturnFrame(jl);
//				}
//				else {
//					//实例化一个管理类
//					InfoOperate userInfo = new InfoOperate(new ConnectionClass());
//					if(!userInfo.selectID(userID)) {//用户名不重复
//						if(userInfo.confirmPassword(password, confirmpassword)) {//如果两次密码为真
//							userInfo.insertUser(userID, password,name,sex,power);//添加新用户
//							JLabel jl = new JLabel("注册成功!");
//							jl.setForeground(Color.red);
//							new ReturnFrame(jl);
//							}
//						else {//如果两次密码不一致
//							JLabel jl = new JLabel("确认密码不正确");
//							jl.setForeground(Color.red);
//							new ReturnFrame(jl);
//							}
//						}
//					else {//用户名重复
//						JLabel jl = new JLabel("该用户已存在,不可以注册");
//						jl.setForeground(Color.red);
//						new ReturnFrame(jl);
//					}
//				}
//			}
//		});
//		return registBtn;
//	}
}
