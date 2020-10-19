package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 * ��ȡ������Ϣ�ķ���
 */
public class FileStream {
	//��ȡ������Ϣ����
	String []info;
	public void storeInfo(UserInfo user) {
		File f=new File("f:\\Message\\"+user.getSender()+"to"+user.getAccepter());
		//���жϴ治����,�������׸���
		//�ֽ������
		FileOutputStream fos=null;
		try {
			fos=new FileOutputStream(f);
			String s=user.getSender()+":\n"+user.getChat()+"\n";
			//д��ȥ
			fos.write(s.getBytes());
			s.getBytes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//��ȡ������Ϣ�ķ���
	public void getInfo(UserInfo user) {
			File f=new File("f:\\Message\\"+user.getSender()+"to"+user.getAccepter());
			FileInputStream fis=null;
			//Fileû�ж�д����,������Ҫʹ��InputStream��
			try {
				fis=new FileInputStream(f);
				//����byte������,���Ƕ����ڴ���
				//����һ���ֽ�����,�൱�ڻ���һ���Ķ���ʾ�����ٶ�
				byte []bytes=new byte[1024];
				int n=0;//�õ�ʵ�ʶ�ȡ�����ֽ���
				//ѭ����ȡ
				while((n=fis.read(bytes))!=-1) {
					//�Ӹ���������ȡ��� b.length���ֽڵ����ݵ�һ���ֽ�����
					//���ֽ�׫��String
					String s=new String(bytes,0,n);//��0��n
					System.out.println(s);
				}
			} catch (Exception e) {//ȫ������
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
}
