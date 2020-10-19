package client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

class ReturnFrame extends JFrame{
	JLabel jl;
	public ReturnFrame(JLabel jl) {
		this.jl=jl;
		jl.setFont(new Font("黑体",0,12));
		Container jp=this.getContentPane();
		jp.setBackground(Color.white);
		jp.setLayout(new FlowLayout(FlowLayout.CENTER));//为了让提示显示正中间
		jp.add(jl);
		
		this.setBackground(Color.WHITE);
		this.setSize(200, 100);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); 
		//获取屏幕长宽
		Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
		int width=screensize.width;
		int height=screensize.height;
		this.setLocation((width-200)/2, (height-100)/2);//使程序显示在屏幕中间
		this.setResizable(false);//窗体不可扩大
		this.setVisible(true);
	}
}