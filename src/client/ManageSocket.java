package client;
import java.net.Socket;
import java.util.*;

import server.ServerThread;
public class ManageSocket {
	
	public static HashMap<String, Socket> hm=new HashMap<String,Socket>();
	public static int i=5;
	//添加通讯socket
	public static void addSocket(String name,Socket st) {
		hm.put(name, st);
	}
	//返回线程
	public static Socket getSocket(String name) {
		return hm.get(name);
	}
	public static void changeI(int m) {
		i=m;
	}
	public static int getI() {
		return i;
	}
}
