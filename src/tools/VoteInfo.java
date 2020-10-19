package tools;

import java.util.ArrayList;
import java.util.HashMap;

import client.ChatFrame;

/*
 * 投票信息类
 */
public class VoteInfo {
	private String date;
	private String owner;
	private String content;
	private String number;
	private String headline;
	private int SUM;//当前票数
	private int isAdvice;//是否有建议栏
	private String partIn;//参与投票的人员
	private String[] options;//所有选项的数组
	private HashMap<String,Integer> optionSum=new HashMap<String,Integer>();
	private String advice;
	//构造信息
	public VoteInfo(String headline) {
		this.headline=headline;
	}
	public int getIsAdvice() {
		return isAdvice;
	}

	public void setIsAdvice(int isAdvice) {
		this.isAdvice = isAdvice;
	}

	public String getPartIn() {
		return partIn;
	}

	public void setPartIn(String partIn) {
		this.partIn = partIn;
	}

	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public HashMap<String, Integer> getOptionSum() {
		return optionSum;
	}
	public void setOptionSum(HashMap<String, Integer> optionSum) {
		this.optionSum = optionSum;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getcontent() {
		return content;
	}
	public void setcontent(String content) {
		this.content = content;
	}

	public int getSUM() {
		return SUM;
	}

	public void setSUM(int sUM) {
		SUM = sUM;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	public String[] getOptions() {
		return options;
	}
	public void setOptions(String[] options) {
		this.options = new String[options.length];
		for(int i=0;i<options.length;i++) {
			this.options[i]=options[i];
		}
	}
}
