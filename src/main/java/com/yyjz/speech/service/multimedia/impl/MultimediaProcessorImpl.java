package com.yyjz.speech.service.multimedia.impl;

import java.io.InputStream;
import java.io.OutputStream;

import com.yyjz.speech.service.multimedia.MultimediaProcessService;

public class MultimediaProcessorImpl implements MultimediaProcessService {

	@Override
	public OutputStream convertMP3StreamToPCMStream(InputStream inputStream) {
		// TODO tiance
		return null;
	}

	/**
	 * 保存数据流到项目默认位置
	 * 
	 * @param inputStream
	 * @return
	 */
	private boolean storeStreamAsDefaultRule(InputStream inputStream) {

		return false;
	}

}
