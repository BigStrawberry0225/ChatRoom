package server;
import java.net.Socket;
import java.util.*;

import server.ServerThread;
public class ManageSocket {
	
	public static HashMap<String, Socket> hm=new HashMap<String,Socket>();
	//添加通讯socket
	public static void addSocket(String name,Socket st) {
		hm.put(name, st);
	}
	//返回线程
	public static Socket getSocket(String name) {
		return hm.get(name);
	}

}
