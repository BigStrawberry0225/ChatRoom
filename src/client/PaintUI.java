package client;
/*
 * 客户端?
 */
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import client.ManageSocket; 
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import tools.IOStream;
import tools.InfoStatus;
import tools.UserInfo;
 
public class PaintUI extends JFrame {
 
	public JPanel drawPanel;
	public JPanel colorPanel;
	public JPanel waitPanel;
	public JPanel drawLeftPanel;
	public JPanel centerPanel;
	public JTextField jtf;
	public JTextArea jta;
	public Graphics2D g;
	public Color color;
	//public ClientCtroller control;
	public Socket socket;
	public int x1, y1;
	public BasicStroke strock;
    public JComboBox<Integer> box;
    String sender;
    String accepter;
    UserInfo user;
	//初始化界面的时候开始创建客户端对象
//	public ClientUI() {
////		try {
////			socket = new Socket("localhost", 9090);
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//	}
////	
//	public static void main(String[] args) {
//		new PaintUI("桃子");
//	}
	public PaintUI(String sender,String accepter){
		socket = ManageSocket.getSocket(sender);
		user = new UserInfo();
		user.setIsPaint(1);
		this.sender=sender;
		this.accepter=accepter;
		this.setTitle(sender+"的画板");
		this.setSize(500, 500);
		this.setDefaultCloseOperation(3);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		addDrawPanel();
	}
 
	//添加画面板的函数
	public void addDrawPanel() {
		drawPanel = new JPanel();
		drawPanel.setLayout(new BorderLayout());
		// 画面板的左右子面板
		drawLeftPanel = new JPanel();
		drawLeftPanel.setLayout(new BorderLayout());
		
		//左边板的中间面板
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		//左面板下的颜色面板
		colorPanel = new JPanel();
		
		//给颜色面板设置空布局
		colorPanel.setLayout(null);
		colorPanel.setBackground(Color.gray);
		colorPanel.setPreferredSize(new Dimension(0,60));
		
		//颜色面板的颜色按钮
		Color [] colors={Color.red,Color.black,Color.orange,Color.green,
				Color.pink,Color.blue,Color.cyan,Color.magenta,Color.YELLOW};
		
		//颜色按钮添加
		ActionListener btnlistener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				JButton bt =(JButton)e.getSource();
				color =bt.getBackground();
			}
		};
		for (int i = 0; i < colors.length; i++) {
			JButton btn = new JButton();
			btn.setBackground(colors[i]);
			btn.addActionListener(btnlistener);
			btn.setBounds(40+i*30, 15, 30, 30);
			colorPanel.add(btn);
		}
		//添加画笔粗细
		box =new JComboBox<Integer>();
		box.setBounds(380, 15, 80, 30);
		for (int i = 0; i < 10; i++) {
			Integer intdata = new Integer(i+1);
			box.addItem(intdata);
		}
		colorPanel.add(box);
		
		jta = new JTextArea();
		jta.setLineWrap(true);
		JScrollPane jsp = new JScrollPane(jta);
		jtf = new JTextField(11);
		drawLeftPanel.add(centerPanel, BorderLayout.CENTER);
		drawLeftPanel.add(colorPanel, BorderLayout.SOUTH);
		drawPanel.add(drawLeftPanel);
		this.add(drawPanel);
		centerPanel.addMouseListener(ma);
		centerPanel.addMouseMotionListener(ma);
		this.setVisible(true);
		g = (Graphics2D)centerPanel.getGraphics();
	}
	//鼠标监听器
	MouseAdapter ma = new MouseAdapter() {
		
		public void mousePressed(MouseEvent e) {
			x1 = e.getX();
			y1 = e.getY();
			System.out.println("坐标1"+x1+","+y1);
		};
		
		public void mouseEntered(MouseEvent e) {
			if(color==null){
				color=Color.black;
			}
			g.setColor(color);
		};
		
		public void mouseDragged(MouseEvent e) {
			int width=(int)box.getSelectedItem();
			strock = new BasicStroke(width);
			g.setStroke(strock);
			int x2 = e.getX();
			int y2 = e.getY();
			//把坐标发给服务器服务器再发给那个客户端
			//g.fillOval(x2, y2, width, width);
			g.drawLine(x1, y1, x2, y2);
			user.setX1(x1);
			user.setY1(y1);
			user.setX2(x2);
			user.setY2(y2);
			try {
				//control.sendMsg1(socket.getOutputStream(), x1, y1, x2, y2,g.getColor().getRGB(),width);
				x1 = x2;
				y1 = y2;
			} catch (Exception e1) {
			}
			user.setColor(color);
			user.setWidth(width);
			user.setSender(sender);
			user.setAccepter(accepter);
			IOStream.sendMessage(socket, user);
			
		};
	};
}