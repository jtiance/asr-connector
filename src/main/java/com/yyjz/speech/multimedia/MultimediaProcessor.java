package com.yyjz.speech.multimedia;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 多媒体处理器, 负责对媒体文件进行操作
 * 
 * @author jtian
 *
 */
public interface MultimediaProcessor {

	/**
	 * 将mp3格式的数据流转换成pcm格式的数据流
	 * 
	 * @param inputStream
	 * @return
	 */
	OutputStream convertMP3StreamToPCMStream(InputStream inputStream);
}
