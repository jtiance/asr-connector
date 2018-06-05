package com.yyjz.ijz.asr.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUtility;
import com.yyjz.ijz.asr.service.MultimediaProcessService;
import com.yyjz.ijz.asr.vo.AsrResult;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

@Service(value = "multimediaProcessService")
public class MultimediaProcessServiceImpl implements MultimediaProcessService {

	private static Logger logger = LoggerFactory.getLogger(MultimediaProcessServiceImpl.class);

	// 保存的mp3等多媒体文件的路径前缀
	private static final String FILE_LOCATION_PREFIX = "../multimedia";

	private static SimpleDateFormat folderFormat = new SimpleDateFormat("yyyyMMddHH");

	private static Encoder encoder = new Encoder(); // 多媒体转码类

	@Override
	public String recognizeMP3Stream(InputStream inputStream) {
		String filePath = null, pcmFilePath = null, result = null;
		try {
			// 保存原始录音文件
			filePath = storeStreamAsDefaultRule(inputStream);

			// 转码为可识别的pcm文件
			pcmFilePath = convertMp3ToPcm(filePath);

			// 语音识别
			result = recognizePCM(pcmFilePath);
		} finally {
			removeFiles(filePath);
			removeFiles(pcmFilePath);
		}
		return result;
	}

	private void removeFiles(String filePath) {
		if (filePath == null) {
			return;
		}
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	private static String recognizePCM(String pcmFilePath) {
		// XXX tiance:加载appid,目前appid为我个人申请的, 后续需要改为公司的
		String app_id = "5b0fac06";
		SpeechUtility.createUtility(SpeechConstant.APPID + "=" + app_id);
		SpeechRecognizer mIat = SpeechRecognizer.createRecognizer();
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");
		mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");

		File file = new File(pcmFilePath);
		byte[] bytes = null;
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
			bytes = readBytesFromStream(in);
			if (bytes == null) {
				return "";
			}
		} catch (IOException e) {
			logger.error("file not found:" + pcmFilePath, e);
		}

		// 切割byte流为语音识别可识别的4800长度
		List<byte[]> buffers = byteSplit(bytes, 4800);

		final LinkedBlockingQueue<AsrResult> queue = new LinkedBlockingQueue<>();

		/**
		 * 听写监听器
		 */
		RecognizerListener listener = new RecognizerListener() {
			public void onBeginOfSpeech() {
				// 听写中...
			}

			public void onEndOfSpeech() {
				// 监听
			}

			/**
			 * 获取听写结果. 获取RecognizerResult类型的识别结果，并对结果进行累加，显示到Area里
			 */
			public void onResult(RecognizerResult results, boolean islast) {

				AsrResult asrResult = JSON.parseObject(results.getResultString(), AsrResult.class);
				queue.offer(asrResult);
			}

			public void onVolumeChanged(int volume) {

			}

			public void onError(SpeechError error) {
				logger.error("监听语音识别数据中发生错误:", error.getCause());
			}

			public void onEvent(int eventType, int arg1, int agr2, String msg) {
				// event
			}

		};

		// 开始监听数据
		mIat.startListening(listener);
		for (int i = 0; i < buffers.size(); i++) {
			mIat.writeAudio(buffers.get(i), 0, buffers.get(i).length);
		}
		mIat.stopListening();
		AsrResult asrResult = null;
		StringBuilder sb = new StringBuilder();
		try {
			while ((asrResult = queue.poll(5, TimeUnit.SECONDS)) != null) {
				sb.append(asrResult.toString());
				// 等待结果收集完成
				if (asrResult.isLs()) {
					break;
				}
			}
		} catch (InterruptedException e) {
			logger.error("error while waiting for AsrResult,e");
		}

		return sb.toString();

	}

	/**
	 * 切割byte流以指定长度
	 * 
	 * @param buffer
	 * @param length
	 * @return
	 */
	private static List<byte[]> byteSplit(byte[] buffer, int length) {
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

	/**
	 * 
	 * @param source
	 *            源文件
	 * @param target
	 *            转换后文件
	 * @throws IllegalArgumentException
	 * @throws InputFormatException
	 * @throws EncoderException
	 */
	private static String convertMp3ToPcm(String filePath) {
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("s16le"); // 设置目标文件的封装格式,使用pcm的一种封装格式s16le

		// 设置目标语音的属性
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("pcm_s16le"); // 设置编码格式
		audio.setSamplingRate(16000); // 设置采样率
		audio.setChannels(1); // 设置声道数,即单声道
		attrs.setAudioAttributes(audio);
		// XXX TIANCE: 转码后的文件语音时常被压缩，这里还需要深入研究一下.

		String pcmFilePath = filePath.replace(".mp3", ".pcm");

		try {
			encoder.encode(new File(filePath), new File(pcmFilePath), attrs);
			logger.debug("multimedia encoded successfully!");
			return pcmFilePath;
		} catch (IllegalArgumentException | EncoderException e) {
			logger.error("error while encoding multimedia, file is:" + filePath, e);
			return null;
		}
	}

	/**
	 * 保存数据流到项目默认目录位置
	 * 
	 * @param inputStream
	 *            将要保存的数据流
	 * @return 保存路径
	 */
	private static String storeStreamAsDefaultRule(InputStream inputStream) {
		return storeStreamAsDefaultRule(inputStream, ".mp3");// 默认mp3格式
	}

	/**
	 * 保存数据流到项目默认目录位置
	 * 
	 * @param inputStream
	 *            将要保存的数据流
	 * @param suffix
	 *            数据流应该被保存成的文件后缀
	 * @return 保存路径
	 */
	private static String storeStreamAsDefaultRule(InputStream inputStream, String suffix) {

		FileOutputStream fos = null;
		try {
			byte[] content = readBytesFromStream(inputStream);
			logger.debug("reads bytes:" + content.length);

			// 创建将要保存的文件信息
			String targetFolderPath = FILE_LOCATION_PREFIX + File.separator + folderFormat.format(new Date());
			File targetFolder = new File(targetFolderPath);
			if (!targetFolder.exists()) {
				targetFolder.mkdirs();
			}

			if (!suffix.startsWith(".")) {
				suffix = "." + suffix;
			}
			String fileName = String.valueOf(content.hashCode()) + suffix;

			String filePath = targetFolderPath + File.separator + fileName;
			logger.debug("multimedia file path is:" + filePath);

			fos = new FileOutputStream(filePath);
			fos.write(content);
			logger.info("file successfully saved into file system.");

			return filePath;
		} catch (IOException e) {
			logger.error("error while store multimedia in file system.", e);
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				logger.error("error while closing FileOutputStream", e);
			}
		}
		return null;

	}

	private static byte[] readBytesFromStream(InputStream is) {
		byte[] _bytes = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(is.available());
			logger.debug("available bytes:" + is.available());
			try {
				int bufSize = 1024;
				byte[] buffer = new byte[bufSize];
				int len = 0;
				while (-1 != (len = is.read(buffer, 0, bufSize))) {
					bos.write(buffer, 0, len);
				}
				_bytes = bos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			logger.error("error reading stream.", e);
		}
		return _bytes;
	}

}
