package com.jdbc.conn;
import java.sql.ResultSet;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import tools.NoticeInfo;
import tools.VoteInfo;

import java.sql.PreparedStatement;
/*
 * 功能:用户信息类和操作信息类
 */
public class InfoOperate {
	
	private ConnectionClass connectionclass=null;
	private Connection conn=null;
	private Statement stmt=null;
	public VoteInfo voteinfo;
	public static  ArrayList<String> owner;//公告发布人集合
	public static  ArrayList<String> date;//时间集合
	public static  ArrayList<VoteInfo> votes;//投票集合
	public InfoOperate(ConnectionClass connclass) {
			conn=connclass.getConnection();//取得连接类中的statement对象
			stmt=connclass.getStatement();//取得连接类中的sql语句
			//这样,当实例化一个连接类 其中的connection已连接 statement已连接,即可使用
		}
	
	//获取所有用户名字的list
	public ArrayList<String> getUserlist() {
		ArrayList<String> allUsers=new ArrayList<>();
		try {
			String sql =("SELECT name FROM student");
			ResultSet rs=stmt.executeQuery(sql);
				for(int i=0;rs.next();i++) {
					allUsers.add(rs.getString("name"));
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return allUsers;
	}
	//获取用户名字
	public String getName(String ID) {
		String name=null;
		try {
			String sql =("SELECT name FROM student WHERE userID='"+ID+"'");
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()){
				name=rs.getString("name");
			}
			return name;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return name;
	}
	//查询用户是否为管理员
	public String selectPower(String ID) {
		try {
			String sql="SELECT power FROM student where userID="+ID;
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()){
				String str=rs.getString("power");
				return str;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		//默认不是
		return "0";
	}
	//查询用户ID是否存在
	public boolean selectID(String ID) {
		try {
			String sql="SELECT userID FROM student";
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()){
				String str=rs.getString("userID");
				if(ID.equals(str)) {
					return true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	//查询确认密码是否正确方法
	public boolean confirmPassword(String password1,String password2) {
			if(password1.equals(password2)) {
				return true;//返回真
			}
			else
				return false;
	}

	//查询密码是否正确
	public boolean selectPassword(String userID,String password) {
		try {
			String sql="SELECT userID,password,password FROM student";
				ResultSet rs=stmt.executeQuery(sql);//返回结果集
				while(rs.next()){
					String pswd=rs.getString("password");
					String id=rs.getString("userID");
					if(userID.equals(id)&password.equals(pswd)) {
						return true;
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;	
	}
	//添加用户方法
	public void insertUser(String userID,String password,String name,String sex,int power) {
		try {
			String sql = "insert into student values ("+userID+","+password+",'"+name+"','"+sex+"',"+power+")";
			System.out.println(sql);
			stmt.executeUpdate(sql);
            System.out.println("注册成功");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//更新数据语句
	}
	//添加公告方法
	public void insertNotice(String content,String owner,String date) {
		try {
			String sql = "insert into notice values ('"+content+"','"+owner+"','"+date+"')";
			System.out.println(sql);
			stmt.executeUpdate(sql);
            System.out.println("加公告成功");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//获取所有公告的方法
	public ArrayList<NoticeInfo> getNotices() {
		ArrayList<NoticeInfo> allNotices=new ArrayList<>();
		try {
			String sql =("SELECT content,date,owner FROM notice");
			ResultSet rs=stmt.executeQuery(sql);
				while(rs.next())  {
					NoticeInfo noticeInfo = new NoticeInfo(rs.getString("content"),rs.getString("owner"),rs.getString("date"));
					allNotices.add(noticeInfo);
					}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return allNotices;
	}
	//获取所有投票标题的方法
	public ArrayList<String> getHeadline() {
		ArrayList<String> allHeadline=new ArrayList<>();
		try {
			String sql =("SELECT headline FROM vote");
			ResultSet rs=stmt.executeQuery(sql);
				for(int i=0;rs.next();i++) {
					String str = rs.getString("headline");
					allHeadline.add(str);
					}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return allHeadline;
	}
	//获取投票类的方法
	public VoteInfo getVote(String headline) {
		voteinfo=new VoteInfo(headline);
		try {
			//先从vote中获取到投票内容
			String sql1 =("SELECT content,date,owner,SUM,isAdvice,partIn FROM vote where headline='"+headline+"'");
			ResultSet rs1=stmt.executeQuery(sql1);
				while(rs1.next()) {
					//设置消息属性
					voteinfo.setcontent(rs1.getString("content"));
					voteinfo.setDate(rs1.getString("date"));
					voteinfo.setOwner(rs1.getString("owner"));
					voteinfo.setSUM(rs1.getInt("SUM"));
					voteinfo.setPartIn(rs1.getString("partIn"));
					voteinfo.setIsAdvice(rs1.getInt("isAdvice"));
					voteinfo.setOptionSum(getOptionSum(headline));
				}
				if(voteinfo.getIsAdvice()==1) {
					String sql2 =("SELECT advice FROM options where headline='"+headline+"'");
					ResultSet rs2=stmt.executeQuery(sql2);
					while(rs2.next())  {
							voteinfo.setAdvice(rs2.getString("advice"));
						}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return voteinfo;
	}
			//获取选项标题
			public String[] getOptionHead(String headline) {
				String[] options=new String[4];
				try {
					String sql =("SELECT option1,option2,option3,option4 FROM options where headline='"+headline+"'");
					ResultSet rs=stmt.executeQuery(sql);
						for(int i=0;rs.next();i++) {
							options[0]=rs.getString("option1");
							options[1]=rs.getString("option2");
							options[2]=rs.getString("option3");
							options[3]=rs.getString("option4");
							}
				} catch (Exception e) {
					// TODO: handle exception
				}
				voteinfo.setOptions(options);
				//如果有意见箱
				return options;
			}
	//获取投票包含票数的方法
	public HashMap<String, Integer> getOptionSum(String headline) {
		HashMap<String,Integer> options=new HashMap<String,Integer>();
		//先从数据库中获取到投票内容
		try{
			String sql =("SELECT option1,option2,option3,option4,sum1,sum2,sum3,sum4 FROM options where headline='"+headline+"'");
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next())  {
				System.out.println("aaa"+rs.getInt("sum1"));
				options.put(rs.getString("option1"), rs.getInt("sum1"));
				options.put(rs.getString("option2"), rs.getInt("sum2"));
				options.put(rs.getString("option3"), rs.getInt("sum3"));
				options.put(rs.getString("option4"), rs.getInt("sum4"));
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return options;
	}
	//判断是否参与投票
	public boolean isPartIn(String headline,String userName) {
		try{
			String sql =("SELECT partIn FROM vote where headline='"+headline+"'");
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next())  {
				String str = rs.getString("partIn");
				if(str.equals("")) {
					return false;
				}
				else {
					String[] partIn = str.split("~");
					for(int i=0;i<partIn.length;i++) {
						if(userName.equals(partIn[i])) {
							return true;
						}
					}
					return false;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	//获取意见箱
	public String getAdvice(String headline) {
		String str =null;
		try{
			String sql =("SELECT advice FROM options where headline='"+headline+"'");
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next())  {
				str = rs.getString("advice");
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return str;
	}
	//填写选中
	public void WriteVote(String userName,String headline,int index,String advice) {
		// update 表名 set 字段名=新的value where 条件;
		//先获取之前的sum,++
		int sum=0;
		int optionSum=0;
		String partIn=null;
		String pastadvice=null;
		int i=++index;
		try{
			String sql1 =("SELECT SUM,partIn FROM vote where headline='"+headline+"'");
			ResultSet rs=stmt.executeQuery(sql1);
			while(rs.next())  {
				sum = rs.getInt("SUM");
				sum++;
				partIn=rs.getString("partIn");
				partIn+=(userName+"~");
			}
			//更新sum和参与者
			String sql2 = "update vote set SUM="+sum+",partIn='"+partIn+"' where headline='" + headline + "'";
			stmt.executeUpdate(sql2);
			//更新意见和选项数字
			String sql3 =("SELECT sum"+i+",advice FROM options where headline='"+headline+"'");
			ResultSet rs3=stmt.executeQuery(sql3);
			while(rs3.next())  {
				optionSum = rs3.getInt("sum"+i);
				optionSum++;
				pastadvice=rs3.getString("advice");
				if(advice!=null) {
					pastadvice+=("\n"+advice);
				}
			}
			String sql4 = "update options set sum"+i+"='"+optionSum+"',advice='"+pastadvice+"'where headline='" + headline + "'";
			stmt.executeUpdate(sql4);
		} catch (SQLException e) {
		        e.printStackTrace();
		}
	}
	//发布投票功能
	public void insertVote(String headline,String content,String owner,int isAdvice,String[] options) {
		try {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
			String datestr = dateFormat.format(date);
			String sql2=null;
			String sql1 = "insert into vote values ('"+headline+"','"+content+"','"+datestr+"','"+owner+"',"+isAdvice+",0,'~')";
			System.out.println(sql1);
			stmt.executeUpdate(sql1);
            
            if(options[1]==null) {
            	sql2 = "insert into options values ('"+headline+"','"+options[0]+"','','','',0,0,0,0,'')";
            }
            else if(options[2]==null){
            	sql2 = "insert into options values ('"+headline+"','"+options[0]+"','"+options[1]+"','','',0,0,0,0,'')";
            }
            else if(options[3]==null){
            	sql2 = "insert into options values ('"+headline+"','"+options[0]+"','"+options[1]+"','"+options[2]+"','',0,0,0,0,'')";
            }
            else{
            	sql2 = "insert into options values ('"+headline+"','"+options[0]+"','"+options[1]+"','"+options[2]+"','"+options[3]+"',0,0,0,0,'')";
            }
            System.out.println(sql2);
			stmt.executeUpdate(sql2);
            
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

