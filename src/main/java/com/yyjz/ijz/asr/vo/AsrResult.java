package com.yyjz.ijz.asr.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 识别结果
 * 
 * @author jtian
 *
 */
public class AsrResult {

	// sentence number 第几句
	private int sn;

	// last sentence boolean 是否最后一句
	private boolean ls;

	// begin number 开始
	private int bg;

	// end number 结束
	private int ed;

	// words array 词
	private Words[] ws;

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public boolean isLs() {
		return ls;
	}

	public void setLs(boolean ls) {
		this.ls = ls;
	}

	public int getBg() {
		return bg;
	}

	public void setBg(int bg) {
		this.bg = bg;
	}

	public int getEd() {
		return ed;
	}

	public void setEd(int ed) {
		this.ed = ed;
	}

	public Words[] getWs() {
		return ws;
	}

	public void setWs(Words[] ws) {
		this.ws = ws;
	}

	@Override
	public String toString() {
		if (ws == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Words words : ws) {
			String str = words.getCw()[0].getW();
			sb.append(str);
		}

		return sb.toString();
	}

}
