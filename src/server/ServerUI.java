package server;
import java.awt.Color;
/*
 * 功能:server内部的结构
 */
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class ServerUI {
	
	JTextField number;//当前在线人数框
	JList matelist;//在线用户框
	JTextField ip;
	JTextField name;
	JButton closeBtn;//关闭按钮
	JTextPane txtlog;//日记区域
	
public JLabel getServerPane1() {
	ImageIcon image =new ImageIcon("src/image/背景.jpg");
	JLabel tabback=new JLabel(image);//选项卡背景
	tabback.setBounds(0,0,ServerFrame.FRAME_WIDTH,ServerFrame.FRAME_HEIGHT);
	
	JPanel pane=new JPanel();//整个面板
	
	pane.setOpaque(false);
	pane.setLayout(null);
	pane.setBounds(0,0,ServerFrame.FRAME_WIDTH,ServerFrame.FRAME_HEIGHT);
	
	JPanel server1=new JPanel();//参数信息面板
	server1.setOpaque(false);
	server1.setBounds(5,5,150,410);
	server1.setFont(new Font("黑体",0,16));
	server1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder(""),
			BorderFactory.createEmptyBorder(1,1,1,1)
			));
	//左边区域
	JLabel jl1=new JLabel("当前在线人数:");
	jl1.setForeground(Color.white);
	jl1.setFont(new Font("黑体",0,16));
	jl1.setBounds(10,0,100,100);
	server1.add(jl1);
	
	number=new JTextField("0人",12);
	number.setFont(new Font("黑体",Font.BOLD,16));
	number.setForeground(Color.white);
	number.setOpaque(false);
	number.setEditable(false);
	server1.add(number);
	
	JLabel jl2=new JLabel("服务器名称:");
	jl2.setForeground(Color.white);
	jl2.setFont(new Font("黑体",0,16));
	jl2.setBounds(10,30,200,200);
	server1.add(jl2);
	
	name=new JTextField(12);
	name.setFont(new Font("黑体",Font.BOLD,16));
	name.setForeground(Color.white);
	name.setOpaque(false);
	name.setEditable(false);
	server1.add(name);
	
	JLabel jl3=new JLabel("服务器IP:");
	jl3.setFont(new Font("黑体",0,16));
	jl3.setBounds(10,50,200,200);
	server1.add(jl3);
	jl3.setForeground(Color.white);
	

	ip=new JTextField(12);
	ip.setFont(new Font("黑体",Font.BOLD,16));
	ip.setBackground(Color.white);
	ip.setForeground(Color.white);
	ip.setOpaque(false);
	ip.setEditable(false);
	server1.add(ip);
	
	JLabel jl4=new JLabel("服务器端口:");
	jl4.setFont(new Font("黑体",0,16));
	jl4.setBounds(10,70,200,200);
	server1.add(jl4);
	jl4.setForeground(Color.white);
	

	JTextField port=new JTextField(ServerInfo.port+"",12);
	port.setFont(new Font("黑体",Font.BOLD,16));
	port.setBackground(Color.white);
	port.setEditable(false);
	port.setForeground(Color.white);
	port.setOpaque(false);
	server1.add(port);
	
	
	//日志区域
	JLabel diarylog=new JLabel("[服务器日志]");
	diarylog.setForeground(Color.white);
	diarylog.setFont(new Font("黑体",0,16));
	diarylog.setBounds(200,5,100,30);
	pane.add(diarylog);
	
	JLabel onlinelog=new JLabel("[当前在线用户]");
	onlinelog.setForeground(Color.black);
	onlinelog.setFont(new Font("黑体",0,16));
	onlinelog.setBounds(550,5,200,30);
	onlinelog.setForeground(Color.white);
	pane.add(onlinelog);
	
	txtlog=new JTextPane();
	txtlog.setFont(new Font("黑体",0,15));
	txtlog.setOpaque(false);
	txtlog.setEditable(false);
	//txtlog.setOpaque(false);
	
	JScrollPane jscrollpane=new JScrollPane(txtlog);
	jscrollpane.setBounds(200, 35, 340, 360);
	jscrollpane.setOpaque(false);
	jscrollpane.getViewport().setOpaque(false);
	pane.add(jscrollpane);

	closeBtn = closeBtn();
	pane.add(closeBtn);
	//pane.add(saveBtn());
	pane.add(onlineList());
	
	pane.add(server1);
	tabback.add(pane);
	return tabback;
}
public JButton closeBtn() {//关闭按钮
		JButton closeBtn=new JButton("关闭服务器");
		closeBtn.setFont(new Font("黑体",0,16));
		closeBtn.setBounds(300, 400, 150, 30);
		closeBtn.setOpaque(false);
		closeBtn.setBackground(Color.WHITE);
		closeBtn.setForeground(Color.WHITE);
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		return closeBtn;
}
//public JButton saveBtn() {//保存按钮
//	//封装的话便于之后对其使用
//	JButton saveBtn=new JButton("保存日志");
//	saveBtn.setFont(new Font("黑体",0,14));
//	saveBtn.setBackground(new Color(150,190,200));
//	saveBtn.setForeground(Color.WHITE);
//	saveBtn.setBounds(380, 400, 120, 25);
//	return saveBtn;
//}
public JScrollPane onlineList() {//当前在线用户数
	matelist=new JList();
	matelist.setBackground(Color.WHITE);
	matelist.setFont(new Font("黑体",Font.PLAIN,12));
	matelist.setVisibleRowCount(15);//显示的个数
	matelist.setFixedCellWidth(100);//单元格的宽度CELL
	matelist.setFixedCellHeight(20);//单元格的高度
	//matelist.setOpaque(false);
	
	JScrollPane listscro=new JScrollPane(matelist);
	listscro.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	listscro.setBounds(550, 35, 170, 360);
	listscro.setBackground(Color.WHITE);
	return listscro;
}
}