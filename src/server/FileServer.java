package server;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer{
    ServerSocket serverSocket;
    String[] parts;
    
    public void init() {
        Thread fileserverThread = new Thread(new fileServerThread());
        fileserverThread.start();
    }
    private class fileServerThread implements Runnable {
        private void startFileServer(){

        }
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(9999);
                while (true) {
                    Socket socket = serverSocket.accept();
                    Thread thread = new Thread(new FileClientHandler(socket));
                    thread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class FileClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream dis;
        private DataOutputStream dos;
        String clientInputStr;
        FileOutputStream fos;
        FileInputStream fis;

        public FileClientHandler(Socket socket) {
            this.socket = socket;
            try {
                this.dis = new DataInputStream(socket.getInputStream());
                this.dos = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            
                try {
                    clientInputStr = dis.readUTF();
                    System.out.println(clientInputStr);
                    parts = clientInputStr.split("#");
                    switch (parts[0]){
                        case "UPLOAD":
                            File f = new File("D:\\1223\\" + parts[1]);
                            if(!f.getParentFile().exists()){
                                f.getParentFile().mkdir();
                            }
                            if (!f.exists()){
                                f.createNewFile();
                            }
                            //接受文件
                            fos = new FileOutputStream(f);
                            byte[] bytes = new byte[1024];
                            int length = 0;
                            while((length = dis.read(bytes,0,bytes.length)) != -1){
                                fos.write(bytes,0,length);
                                fos.flush();
                          
                            }
                            if(dis != null){
                                dis.close();
                            }
                            fos.close();
                            break;
                        case "DOWNLOAD":
                            ServerSocket serverSocket1 = new ServerSocket(9998);
                            while (true) {
                                Socket socket1 = serverSocket1.accept();
                                Thread thread1 = new Thread(new download(socket1));
                                thread1.start();
                            }
                        default:
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {

                    try {
                        if(fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                
            }

        }
    }

    private class download implements Runnable {
        private Socket socket1;
        private DataInputStream dis1;
        private DataOutputStream dos1;
        private FileInputStream fis1;

        public download(Socket socket1) {
            this.socket1 = socket1;
            try {
                this.dis1 = new DataInputStream(socket1.getInputStream());
                this.dos1 = new DataOutputStream(socket1.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            try {
                File f_2 = new File("D:\\1223\\" + parts[1]);
                fis1 = new FileInputStream(f_2);
                byte[] bytes_2 = new byte[1024];
                int length_2 = 0;
                while ((length_2 = fis1.read(bytes_2,0,bytes_2.length)) != -1){
                    dos1.write(bytes_2,0,length_2);
                    dos1.flush();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}