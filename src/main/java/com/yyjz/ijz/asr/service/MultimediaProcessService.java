package com.yyjz.ijz.asr.service;

import java.io.InputStream;

/**
 * 多媒体处理器, 负责对媒体文件进行操作
 * 
 * @author jtian
 *
 */
public interface MultimediaProcessService {

	/**
	 * 识别mp3的数据流
	 * 
	 * @param inputStream
	 *            mp3格式的数据流
	 * @return 被识别后的字符串
	 */
	String recognizeMP3Stream(InputStream inputStream);
}
