package client;


import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUploadClient {

    private String fileName;
    private String filePath;
    private JFileChooser chooser;
    private DataOutputStream dos;
    private FileInputStream fis;
    private Socket socket;
    private String userName;
    

    public FileUploadClient(String username) {
    	this.userName = username;
    }
 
    public void init() {
        Thread thread = new Thread(new FileClientServer());
        thread.start();

    }

    private class FileClientServer implements Runnable {

    	
        public void run() {
            try {
                socket = new Socket("127.0.0.1",9999);
                dos = new DataOutputStream(socket.getOutputStream());
                chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    fileName = chooser.getSelectedFile().getName();
                    filePath = chooser.getSelectedFile().getPath();
                    File f = new File(filePath);
                    dos.writeUTF("UPLOAD#" + fileName);
                    dos.flush();
                    fis = new FileInputStream(f);
                    byte[] bytes = new byte[1024];
                    int length = 0;
                    while ((length = fis.read(bytes,0,bytes.length)) != -1){
                        dos.write(bytes,0,length);
                        dos.flush();
                    }
                    updateDatabase(fileName,filePath,userName);
                    JOptionPane.showMessageDialog(null,"上传完成");
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }finally {
                    try {
                        if(fis != null) {
                            fis.close();
                        }
                        if(dos != null) {
                            dos.close();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

            }

        }

    }

        private void updateDatabase(String fn,String fp,String un) {

            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab", "root", "root");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String sql = "insert into file(FileName,FilePath,username,time) values(?,?,?,?)";
            try {
                PreparedStatement ptmt = con.prepareStatement(sql);
                ptmt.setString(1,fn);
                ptmt.setString(2,fp);
                ptmt.setString(3,un);
                Date date = new Date();
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		String dateStr = sdf.format(date);
                ptmt.setString(4,dateStr);
                ptmt.execute();
                ptmt.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    

}
