package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.sql.*;

public class FileDownloadClient {
    private JFrame fdc;
    private JList<String> fl;
    private DefaultListModel<String> filelist;
    private Socket socket;
    private Socket socket1;
    private DataOutputStream dos;
    private String path = null;
    private FileOutputStream fos;
    private String filePath = null;
    private String[] name;
    private boolean t1 = false;

    
    public void init(){
        fdc = new JFrame();
        fdc.setBounds(300,200,450,350);
        fdc.setResizable(false);
        fdc.setLayout(null);
        fdc.setTitle("下载文件");
        fdc.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); 
        JLabel label = new JLabel("请选择要下载的文件");
        label.setBounds(130,0,150,40);
        fdc.add(label);

        filelist = new DefaultListModel<String>();
        String FileName;
        String username;
        String time;
        String url = "jdbc:mysql://localhost:3306/lab";
        String user = "root";
        String pass = "root";
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "select * from file";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                FileName = rs.getString("FileName");
                username = rs.getString("username");
                time = rs.getString("time");
                String message = FileName + "----------" + "上传者:" + username + "----------" + time;
                filelist.addElement(message);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JButton button = new JButton("下载");
        button.setBounds(360,5,70,27);
        fdc.add(button);

        fl = new JList<String>(filelist);
        fl.setModel(filelist);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0,40,435,270);
        fl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(fl);
        fdc.add(scrollPane);
        fdc.setVisible(true);

        Connection finalCon = con;
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = fl.getSelectedValue();
                name = text.split("----------");
                t1 = true;
                String sql_2 = "select * from file where FileName=?";
                try {
                    PreparedStatement ps_2 = finalCon.prepareStatement(sql_2);
                    ps_2.setString(1,name[0]);
                    ResultSet rs_2 = ps_2.executeQuery();
                    if (rs_2.next()){
                        path = rs_2.getString("FileName");
                    }
                    System.out.println(path);
                    rs_2.close();
                    ps_2.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                try {
                    finalCon.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    filePath = chooser.getSelectedFile().getAbsolutePath();
                    Thread thread = new Thread(new FileDownloadHandler());
                    thread.start();
                }

            }
        });

    }

    private class FileDownloadHandler implements Runnable {
        DataInputStream dis;

        @Override
        public void run() {
            try {
                socket = new Socket("127.0.0.1",9999);
                System.out.println("hh");
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("DOWNLOAD#" + path);
                try {
                    socket1 = new Socket("127.0.0.1",9998);
                    System.out.println("connect11");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                File f = new File(filePath + "//" + name[0]);
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
                dis = new DataInputStream(socket1.getInputStream());
                while((length = dis.read(bytes,0,bytes.length)) != -1){
                    fos.write(bytes,0,length);
                    fos.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
                dis.close();
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


