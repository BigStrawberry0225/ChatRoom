package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import client.ManageSocket;
import javax.swing.*;
import com.jdbc.conn.ConnectionClass;
import com.jdbc.conn.InfoOperate;

import tools.IOStream;
import tools.InfoStatus;
import tools.UserInfo;
import tools.VoteInfo;

/*
 * 公告面板
 */

public class VoteList extends JPanel{
	
	String power;
	InfoOperate infooperate;
	JLabel []jls;//投票label栏
	JButton []jbs;//查看详情按钮数组,用于获得下标
	String userName;
	JPanel votepanel;//公告面板
	SendVote sendvote;//发布窗体
	GetVote getvote;
	
	public VoteList(String userName,String power) {
		this.userName=userName;
		this.power=power;
		//实例化一个管理类
		infooperate=new InfoOperate(new ConnectionClass());
		//JLabel背景图
		ImageIcon imageicon=new ImageIcon("src/image/背景.jpg");
		
		JLabel background3=new JLabel(imageicon);
		background3.setBounds(0,0,750,600);
		background3.setLayout(null);
		this.add(background3);
		this.setOpaque(false);
		this.setLayout(null);
		//公告面板存放标题标签
		votepanel = new JPanel();
		votepanel.setLayout(new GridLayout(50,1,0,10));
		votepanel.setBackground(Color.RED);
		//滚动栏
		JScrollPane listscro=new JScrollPane(votepanel);
		listscro.setBounds(50,10,450,450);
		listscro.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listscro.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		background3.add(listscro);
		listscro.setOpaque(false);
		//添加刷新按钮
//		JButton jb = new JButton("刷新");
//		jb.setBounds(500,100,100,20);
//		background3.add(jb);
//		jb.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				getHeadline();
//			}
//		});
		//获取标题标签
		getHeadline();
		//如果是管理员
		if(power.equals("1")) {
			background3.add(sendButton());
		}
		
	}
	
	/*
	 * 获取标题标签的方法
	 */
	public void getHeadline() {
		ArrayList<String> headline = infooperate.getHeadline();
		//根据标题创建一系列JLabel
		jls=new JLabel[headline.size()];
		for(int i=0;i<headline.size();i++) {
		jls[i]=new VoteLabel(headline.get(i),userName);
		votepanel.add(jls[i]);
		}
	}
	/*
	 * 发布投票按钮
	 */
	public JButton sendButton() {
		//发布公告按钮
		JButton sendbutton=new JButton("发布投票");
		sendbutton.setForeground(Color.white);
		sendbutton.setFont(new Font("黑体",0,13));
		sendbutton.setBackground(Color.RED);
		sendbutton.setBounds(200,485,125,25);
		sendbutton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				sendvote=new SendVote(userName);	
			}
		});
		return sendbutton;
	}
}


/*
 * 投票标签栏Label类
 */
class VoteLabel extends JLabel{
	String headline;
	ImageIcon imageicon2=new ImageIcon("src/image/投票框.jpg");
	public VoteLabel(String headline,String userName) {//传入标题
		//属性设置
		this.headline=headline;
		this.setIcon(imageicon2);
		JButton selectButton = selectButton("查看详情");
		selectButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//打开详情面板
				new GetVote(headline,userName);
			}
		});
		this.add(selectButton);
		//创建标题名
		JLabel jl=new JLabel(headline);
		jl.setFont(new Font("黑体",0,14));
		jl.setBounds(10,13,350,20);
		this.add(jl);
	
	}
	/*
	 *获取详情按钮 
	 */
	public JButton selectButton(String str) {
		//发布公告按钮
		JButton selectbutton=new JButton(str);
		selectbutton.setForeground(Color.black);
		selectbutton.setFont(new Font("黑体",0,12));
		selectbutton.setBackground(Color.white);
		selectbutton.setBounds(330,14,90,25);
		
		return selectbutton;
	}
}
/*
 * 获取详情窗体
 */
class GetVote extends JFrame{
	
	Boolean a=false;
	InfoOperate infoOperate;
	JLabel head;//标题
	JTextPane content;//详情内容
	ButtonGroup bg;//选项组
	String userName;
	JPanel down;//下方布局
	JTextPane jtp;//已有意见栏
	JTextPane myjtp=null;//我的意见栏
	JButton OK;//提交按钮
	JRadioButton[] jbs;//单选框数组
	
	JScrollPane jsp;
	JButton sendbutton;
	String number;//投票编号
	public static final int FRAME_WIDTH=350;
	public static final int FRAME_HEIGHT=400;
	
	public GetVote(String headline,String userName) {
		this.userName=userName;
		//连接数据库获得投票信息
		infoOperate=new InfoOperate(new ConnectionClass());
		VoteInfo voteInfo = infoOperate.getVote(headline);
		//界面
		Container contentPane = this.getContentPane();
		contentPane.setLayout(null);
		//获取投票标题
		head=new JLabel("HeadLine:"+headline);
		head.setBounds(5,5,300,20);
		head.setFont(new Font("黑体",Font.BOLD,15));
		contentPane.add(head);
		//获取投票内容
		content=new JTextPane();
		content.setBounds(10,30,180,120);
		content.setText("[Details]\n"+voteInfo.getcontent()+"\n发布者:"+voteInfo.getOwner()+"\n发布时间:"+voteInfo.getDate());
		content.setFont(new Font("黑体",0,13));
		content.setEditable(false);
		contentPane.add(content);
	
		//获取投票选项名和新建down
		down=new JPanel(new GridLayout(5,2,0,5));
		down.setBounds(10,160,180,200);
		down.setOpaque(false);
		contentPane.add(down);
		String[] options = infoOperate.getOptionHead(headline);
		bg=new ButtonGroup();
		jbs=new JRadioButton[4];
		//获取到当前票数
		HashMap<String, Integer> optionSum = voteInfo.getOptionSum();
		for(int i=0;i<4;i++) {
			//如果不为空
			if(options[i]!=null) {
				JRadioButton jrb = new JRadioButton(options[i]);
				jrb.setFont(new Font("黑体",0,13));
				jrb.setBackground(Color.white);
				bg.add(jrb);
				int y = jrb.getY();
				JLabel jl=new JLabel(" 当前票数为"+optionSum.get(options[i]).toString());
				jl.setFont(new Font("黑体",0,13));
				jl.setBounds(10, y, 100, 20);
				jbs[i]=jrb;
				down.add(jrb);
				down.add(jl);
			}
		}
		
		//是否有意见栏
		if(voteInfo.getIsAdvice()==0) {
			JLabel jl = new JLabel("[暂无意见栏]");
			jl.setBounds(210,200,100,20);
			jl.setFont(new Font("黑体",Font.BOLD,15));
			contentPane.add(jl);
		}
		//如果有意见栏
		else {
			jtp=new JTextPane();
			//获取之前的意见
			String advice = infoOperate.getAdvice(headline);
			jtp.setText("   [当前意见箱]\n"+advice);
			jtp.setFont(new Font("黑体",0,12));
			jtp.setBackground(Color.WHITE);
			jtp.setEditable(false);
			jsp=new JScrollPane(jtp);
			jsp.setBounds(200,30,130,150);
			jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			contentPane.add(jsp);

			myjtp=new JTextPane();
			myjtp.setFont(new Font("黑体",0,12));
			myjtp.setText("(输入你的意见)");
			myjtp.setBounds(200,190,130,100);
			myjtp.setBackground(Color.WHITE);
			myjtp.addMouseListener(new MouseAdapter() {
				int clickNum=0;
				public void mouseClicked(MouseEvent e) {
					if(clickNum==0) {
						myjtp.setText("");
						clickNum++;
					}
				}
			});
			contentPane.add(myjtp);
		}
		
		//判断投票是否结束
		ArrayList<String> userlist = infoOperate.getUserlist();
		//如果结束
		if(voteInfo.getSUM()>=userlist.size()) {
			JButton jb = new JButton("投票已结束");
			jb.setEnabled(false);
			jb.setFont(new Font("黑体",0,13));
			jb.setBounds(120,330,100,25);
			contentPane.add(jb);
		}
		else {
			//如果参与投票
			if(infoOperate.isPartIn(headline, userName)) {
				JButton jb = new JButton("已参与投票");
				jb.setEnabled(false);
				jb.setFont(new Font("黑体",0,13));
				jb.setBounds(120,330,100,25);
				contentPane.add(jb);
			}
			//如果没参与
			else {
				//发送按钮
				sendbutton=new JButton("提交");
				sendbutton.setForeground(Color.BLACK);
				sendbutton.setFont(new Font("黑体",0,13));
				sendbutton.setBackground(Color.white);
				sendbutton.setBounds(120,330,100,25);
				sendbutton.addActionListener(new ActionListener() {
					int index=0;
					String advice=null;
					String option=null;
					
					public void actionPerformed(ActionEvent e) {
						//获取所选内容下标
						try{
							for(int i=0;i<jbs.length;i++) {
								JRadioButton temp = jbs[i];
								if(temp.isSelected()) {
									option=jbs[i].getText();
									index=i;
								}
							}
						}
						catch (Exception e1) {
							System.out.println("something wrong");
						}
						if(option==null){
							JLabel jl = new JLabel("请选择投票信息");
							new ReturnFrame(jl);
						}
						else {
							//获取所输入的意见
							if(myjtp==null){
								System.out.println("我没意见");
							}
							else if(myjtp.getText().equals("(输入你的意见)")){
								System.out.println("我没意见");
							}
							else {
								advice = myjtp.getText();
							}
							JLabel jl = new JLabel("提交成功");
							new ReturnFrame(jl);
							sendbutton.setText("已参与投票");
							sendbutton.setEnabled(false);
							//把意见和选项写进数据库
							infoOperate.WriteVote(userName, headline, index, advice);
						}
					}
				});
				contentPane.add(sendbutton);
			}
		}
		//添加组件
		contentPane.setBackground(new Color(170,210,210));
		//设置窗体属性
		this.setTitle("投票详情");
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); 
		this.setResizable(false);//窗体不可扩大
		//获取屏幕长宽
		Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
		int width=screensize.width;
		int height=screensize.height;
		//使程序显示在屏幕中间
		this.setLocation((width-350)/2, (height-400)/2);
		this.setVisible(true);
	}
}
/*
发布公告窗体
*/
class SendVote extends JFrame implements ActionListener,MouseListener{
	 
	String userName;

	public static final int FRAME_WIDTH=250;
	public static final int FRAME_HEIGHT=400;
	
	JList jl;
	DefaultListModel dlm;// Jlist里面的内容的对象
	JButton jb1;// 删除按钮
	JButton jb2;// 添加按钮
	JPanel jp;// 放2个按钮的panel
	JPanel jp2;// 放文本框的panel
	JTextField headline;// 文本框，用于输入要插入的文字
	JTextPane content;
	JTextField option;
	JRadioButton jrb;
	JButton send;
	
	InfoOperate infooperate;
	int isAdvice;//是否添加意见标志
	 public SendVote(String userName) {
		 
		infooperate=new InfoOperate(new ConnectionClass());
		this.userName=userName;
		Font f = new Font("黑体",0,13);
		Container contentPane = this.getContentPane();
		contentPane.setLayout(null);
		contentPane.setBackground(Color.RED);
		//按钮
		jb1 = new JButton("删除");
		jb1.setBounds(50,300,70,20);
		jb1.setFont(f);
		jb1.setBackground(Color.white);
		jb2 = new JButton("添加");
		jb2.setBounds(130,300,70,20);
		jb2.setFont(f);
		jb2.setBackground(Color.white);
		send = new JButton("发布");
		send.setBounds(70,330,100,20);
		send.setFont(f);
		send.setBackground(Color.white);
		jp = new JPanel();
		//内容
		headline = new JTextField("请输入投票标题");
		headline.setBounds(20, 10, 200, 20);
		content=new JTextPane();
		content.setText("请输入内容");
		content.setBounds(20, 40, 200, 100);
		option = new JTextField("请输入选项(最多4个)");
		option.setBounds(20, 240, 200, 20);
		dlm = new DefaultListModel();
		jl = new JList(dlm);// 创建一个包含DefaultListModel的Jlist
		jl.setBounds(20,150,200,80);
		jrb=new JRadioButton("是否添加意见箱");
		jrb.setFont(f);
		jrb.setBounds(60,270,150,20);
		jrb.setOpaque(false);
		contentPane.add(jb1);
		contentPane.add(jb2);
		contentPane.add(headline);
		contentPane.add(jl);
		contentPane.add(content);
		contentPane.add(option);
		contentPane.add(jrb);
		contentPane.add(send);
		//注册监听
		jb1.addActionListener(this);// 把监听注册个按钮
		jb2.addActionListener(this);
		headline.addMouseListener(this);
		option.addMouseListener(this);
		content.addMouseListener(this);
		
		//发送按钮也注册监听
		send.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				//获取信息
				//判断是否添加意见箱
				if(jrb.isSelected()) {
					isAdvice=1;
				}
				else {
					isAdvice=0;
				}
				//标题
				String head1 = headline.getText();
				if(head1.equals("")) {
					JLabel j = new JLabel("请输入标题");
					new ReturnFrame(j);
				}
				else {
					//选项和内容
					String []options=new String[4];
					String content1 = content.getText();
					int size = jl.getModel().getSize();
					if(size==0||size>4) {
						JLabel j = new JLabel("选项错误");
						new ReturnFrame(j);
					}
					else {
						for(int i=0;i<size;i++) {
							 String str = jl.getModel().getElementAt(i).toString();
							 options[i]=str;
						}
						//发布投票
						infooperate.insertVote(head1, content1, userName, isAdvice, options);
						//发送给服务器
						UserInfo user = new UserInfo();
						user.setInfoStatus(InfoStatus.VOTE);
						user.setHeadline(head1);
						user.setOwner(userName);
						//给服务器发送信息
						Socket socket = ManageSocket.getSocket(userName);
						IOStream.sendMessage(socket, user);
					}
				}
			}
		});
		//设置窗体属性
		this.setTitle("发布投票");
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); 
		//,this.setResizable(false);//窗体不可扩大
		//获取屏幕长宽
		Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
		int width=screensize.width;
		int height=screensize.height;
		//使程序显示在屏幕中间
		this.setLocation((width-250)/2, (height-400)/2);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 如果按的是删除按钮
		if (e.getSource() == jb1) {
		// 移除当前选择的内容
			dlm.remove(jl.getSelectedIndex());
			jl.remove(jl.getSelectedIndex());
		}
		// 如果按的是田间按钮
		if (e.getSource() == jb2) {
		// 吧文本框中的内容添加到列表
			dlm.addElement(option.getText());
		}
	}

	public void mouseClicked(MouseEvent e) {
		int clickNum=0;
		if(e.getSource()==headline) {
			if(clickNum==0) {
				headline.setText("");
				clickNum++;	
			}
		}
		if(e.getSource()==content) {
			if(clickNum==0) {
				content.setText("");
				clickNum++;	
			}
		}
		if(e.getSource()==option) {
			if(clickNum==0) {
				option.setText("");
				clickNum++;	
			}
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
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
