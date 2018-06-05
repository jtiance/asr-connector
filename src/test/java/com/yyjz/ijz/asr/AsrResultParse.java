package com.yyjz.ijz.asr;

import com.alibaba.fastjson.JSON;
import com.yyjz.ijz.asr.vo.AsrResult;

public class AsrResultParse {
	public static void main(String[] args) {
		String s = "{\"bg\":0,\"sn\":1,\"ed\":0}";

		AsrResult result = (AsrResult) JSON.parseObject(s, AsrResult.class);
		System.out.println();
	}
}
