package com.yyjz.ijz.asr.msc;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUtility;

public class MSCStreamVoiceRecognizer extends MSCVoiceRecognizer {
	protected static void initParamMap() {
		mParamMap.put(SpeechConstant.LANGUAGE, FileSourceDefaultValue.LANGUAGE);
		mParamMap.put(SpeechConstant.ACCENT, FileSourceDefaultValue.ACCENT);
		mParamMap.put(SpeechConstant.DOMAIN, FileSourceDefaultValue.DOMAIN);
		mParamMap.put(SpeechConstant.AUDIO_SOURCE, FileSourceDefaultValue.AUDIO_SOURCE);
	}

	public static void main(String[] args) {
		initParamMap();

		SpeechUtility.createUtility(SpeechConstant.APPID + "=5b0fac06");
		mIat = SpeechRecognizer.createRecognizer();

		for (Entry<String, String> entry : mParamMap.entrySet()) {
			mIat.setParameter(entry.getKey(), entry.getValue());
		}

		/**
		 * 听写监听器
		 */
		MyRecognizerListener listener = new MyRecognizerListener();

		File file = new File("d:/audio/new2.pcm");
		byte[] _bytes = null;
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream(((int) file.length()));
			BufferedInputStream in = null;
			try {
				in = new BufferedInputStream(new FileInputStream(file));
				int bufSize = 1024;
				byte[] buffer = new byte[bufSize];
				int len = 0;
				while (-1 != (len = in.read(buffer, 0, bufSize))) {
					bos.write(buffer, 0, len);
				}
				_bytes = bos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		List<byte[]> buffers = byteSplit(_bytes, 4800);

		mIat.startListening(listener);

		for (int i = 0; i < buffers.size(); i++) {
			mIat.writeAudio(buffers.get(i), 0, buffers.get(i).length);
		}
		mIat.stopListening();
	}

	/**
	 * 切割byte流以指定长度
	 * 
	 * @param buffer
	 * @param length
	 * @return
	 */
	public static List<byte[]> byteSplit(byte[] buffer, int length) {
		if (buffer == null) {
			return null;
		}

		List<byte[]> byteList = new ArrayList<>();

		// 获得可以争端切割的部分数量
		int segment = buffer.length / length;

		// 切割完整的段
		for (int i = 0; i < segment; i++) {
			byte[] _byte = new byte[length];
			System.arraycopy(buffer, i * length, _byte, 0, length);
			byteList.add(_byte);
		}

		// 切割整段后剩下的部分
		int rest_length = buffer.length % length;
		byte[] rest_byte = new byte[rest_length];
		System.arraycopy(buffer, segment * length, rest_byte, 0, rest_length);
		byteList.add(rest_byte);

		return byteList;
	}
}
