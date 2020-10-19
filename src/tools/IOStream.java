package tools;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
/*
 * 功能:io流工具类
 */
import java.net.Socket;

public class IOStream {
	public static Object readMessage(Socket socket) {//读取对象
		Object obj=null;
		try {
			InputStream is = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			obj = ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	public static void sendMessage(Socket socket,Object message) {//写对象
		try {
			OutputStream os = socket.getOutputStream();	
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
