package client;
/*
 * 管理用户聊天界面的类
 */
import java.util.*;
public class ManageChatFrame {

	private static HashMap<String, ChatFrame> hm=new HashMap<String,ChatFrame>();
	
	//加入
	public static void addChat(String userANDfriend,ChatFrame chatframe) {
		hm.put(userANDfriend, chatframe);
	}
	//得到
	public static ChatFrame getChat(String userName) {
		return hm.get(userName);
	}
}
