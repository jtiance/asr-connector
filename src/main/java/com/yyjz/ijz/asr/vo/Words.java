package com.yyjz.ijz.asr.vo;

public class Words {
	// begin number 开始
	private int bg;

	// chinese word array 中文分词
	private ChineseWord[] cw;

	public ChineseWord[] getCw() {
		return cw;
	}

	public void setCw(ChineseWord[] cw) {
		this.cw = cw;
	}

	public int getBg() {
		return bg;
	}

	public void setBg(int bg) {
		this.bg = bg;
	}

}
