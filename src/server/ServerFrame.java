package server;
import java.awt.*;
import javax.swing.*;

//import com.sun.security.ntlm.Server;

public class ServerFrame extends JFrame{//服务器端界面
	
	//定义长和宽
	public static final int FRAME_WIDTH=750;
	public static final int FRAME_HEIGHT=480;
	//组件
	ServerUI serverUI;
	
	public ServerFrame() {
		//选项卡
		//JTabbedPane tpServer=new JTabbedPane();
		//tpServer.setFont(new Font("黑体",0,13));
		//tpServer.setBounds(0,5,750,500);
		//获取服务器UI
		serverUI=new ServerUI();
		//新建管理员界面
		//JPanel manager=new JPanel();
		//tpServer.add("服务器信息",serverUI.getServerPane1());
		//tpServer.add("管理员日志",manager);
		Container contentPane = this.getContentPane();
		contentPane.setLayout(null);
		contentPane.add(serverUI.getServerPane1());
		contentPane.setBackground(Color.PINK);
		//设置窗体属性
		this.setResizable(false);//窗体不可扩大
		this.setTitle("服务器");
		this.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		//this.setUndecorated(true);
		//获取屏幕长宽
		Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
		int width=screensize.width;
		int height=screensize.height;
		this.setLocation((width-FRAME_WIDTH)/2, (height-FRAME_HEIGHT)/2);//使程序显示在屏幕中间
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭时退出程序
	
	}

}
