package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

import com.jdbc.conn.ConnectionClass;
import com.jdbc.conn.InfoOperate;

import tools.IOStream;
import tools.InfoStatus;
import tools.NoticeInfo;
import tools.UserInfo;
/*
 * 公告面板
 */
public class NoticeList extends JPanel{
	
	String power;
	InfoOperate infooperate;
	ArrayList<NoticeInfo> notices;
	JTextPane []textpane;
	String userName;
	JPanel noticepanel;//公告面板
	SendNotice sendnotice;
	
	public NoticeList(String userName,String power) {
		this.userName=userName;
		this.power=power;
		//实例化一个管理类
		infooperate=new InfoOperate(new ConnectionClass());
		//JLabel背景图
		ImageIcon imageicon=new ImageIcon("src/image/背景.jpg");
		JLabel background2=new JLabel(imageicon);
		background2.setBounds(0,0,750,600);
		background2.setLayout(null);
		this.add(background2);
		this.setOpaque(false);
		this.setLayout(null);
		//公告面板
		noticepanel = new JPanel();
		noticepanel.setLayout(new GridLayout(50,1,0,10));
		noticepanel.setBackground(Color.RED);
		//滚动栏
		JScrollPane listscro=new JScrollPane(noticepanel);
		listscro.setBounds(50,10,450,450);
		listscro.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listscro.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		listscro.setOpaque(false);
		//获得之前的公告
		addNotice();
		background2.add(listscro);
		//如果是管理员
		if(power.equals("1")) {
			background2.add(sendButton());
		}
	}
	/*
	 * 返回发送按钮
	 */
	public JButton sendButton() {
		//发布公告按钮
		JButton sendbutton=new JButton("发布公告");
		sendbutton.setForeground(Color.white);
		sendbutton.setFont(new Font("黑体",0,13));
		sendbutton.setBackground(Color.RED);
		sendbutton.setBounds(200,485,125,25);
		sendbutton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				sendnotice=new SendNotice(userName);	
			}
		});
		return sendbutton;
	}
	/*
	 * 返回textpane数组
	 */
	public void addNotice(){
		//连接数据库获得公告noticeinfo集合
		notices = infooperate.getNotices();
		//切分string的方法
		//String[] newNotice = cutNotice(array);
		textpane=new JTextPane[notices.size()];
		//根据公告集合创建一个textpane集合
		for(int i=0;i<notices.size();i++) {
			//裁剪公告消息
			NoticeInfo noticeInfo = notices.get(i);
			String notice = cutNotice(noticeInfo.getNotice());
			//添加发布人和发布时间
			notice+="\n发布人:"+noticeInfo.getOwner()+"\n发布时间:"+noticeInfo.getDate();
			JTextPane temp = new JTextPane();
			temp.setText(notice);
			temp.setEditable(false);
			temp.setFont(new Font("黑体",Font.PLAIN,12));
			textpane[i]=temp;
			noticepanel.add(textpane[i]);
		}
	}
	public String cutNotice(String notice) {
		//如果小于30
		if(notice.length()<=30) {
				return notice;
			}
		//如果超过30
		else{
			String str="";
			for(int j=0;j<=notice.length()/30;j++) {
				if(30*(j+1)<notice.length()) {
					String substring = notice.substring(30*j, 30*(j+1));
					//System.out.println("裁剪"+30*j+"到"+30*(j+1));
					str+=(substring+"\n");
				}
				else {
					String substring = notice.substring(30*j, notice.length());
					//System.out.println("裁剪"+30*j+"到"+notice.length());
					str+=(substring+"\n");
				}
			}
			return str;
		}
	}
}
/*
 * 发布公告窗体
 */
class SendNotice extends JFrame{
	
	JTextPane jtp;
	JScrollPane jsp;
	JButton sendbutton2;
	String userName;
	ReturnFrame returnFrame;
	public static final int FRAME_WIDTH=475;
	public static final int FRAME_HEIGHT=370;
	
	public SendNotice(String userName) {
		this.userName=userName;
		//界面
		Container contentPane = this.getContentPane();
		contentPane.setLayout(null);
		jtp=new JTextPane();
		jtp.setFont(new Font("黑体",0,12));
		jtp.setBackground(Color.WHITE);
		jtp.setText("(请限制字数在200内)");
		jsp=new JScrollPane(jtp);
		jsp.setBounds(5,5,450,280);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//注册监听
		jtp.addMouseListener(new MouseAdapter() {
			int clickNum=0;
			public void mouseClicked(MouseEvent e) {
				if(clickNum==0) {
					jtp.setText("");
					clickNum++;
				}
			}
		});
		//发送按钮
		sendbutton2=new JButton("发布");
		sendbutton2.setForeground(Color.BLACK);
		sendbutton2.setFont(new Font("黑体",0,13));
		sendbutton2.setBackground(Color.white);
		sendbutton2.setBounds(170,300,125,25);
		//添加组件
		contentPane.add(jsp);
		contentPane.add(sendbutton2);
		contentPane.setBackground(Color.RED);
		//发送按钮添加监听
		sendbutton2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JLabel jl = new JLabel("确定发送吗?");
				returnFrame = new ReturnFrame(jl);
				returnFrame.add(confirmSend());
			}
		});
		//设置窗体属性
		this.setTitle("发布公告");
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); 
		//获取屏幕长宽
		Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
		int width=screensize.width;
		int height=screensize.height;
		//使程序显示在屏幕中间
		this.setLocation((width-475)/2, (height-370)/2);
		this.setVisible(true);
		this.setResizable(false);//窗体不可扩大
	}
	//确认发送按钮
	public JButton confirmSend() {
		JButton jb = new JButton("确定");
		jb.setFont(new Font("黑体",0,12));
		jb.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				returnFrame.dispose();
				String text = jtp.getText();
				//如果公告为空
				if(text.equals("")) {
					new ReturnFrame(new JLabel("公告不能为空"));
				}
				//如果公告不为空
				UserInfo user = new UserInfo();
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
				String datestr = dateFormat.format(date);
				//设置公告信息
				user.setInfoStatus(InfoStatus.NOTICEPAD);
				user.setPadDate(datestr);
				user.setNoticepad(text);
				user.setOwner(userName);
				//给服务器发送公告信息
				Socket socket = ManageSocket.getSocket(userName);
				IOStream.sendMessage(socket, user);
			}
		});
		return jb;
	}
}

