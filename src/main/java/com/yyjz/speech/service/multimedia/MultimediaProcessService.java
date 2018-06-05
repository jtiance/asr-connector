package com.yyjz.speech.service.multimedia;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 多媒体处理器, 负责对媒体文件进行操作
 * 
 * @author jtian
 *
 */
public interface MultimediaProcessService {

	OutputStream convertMP3StreamToPCMStream(InputStream inputStream);
}
