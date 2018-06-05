package com.yyjz.speech.msc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechRecognizer;

public class MSCVoiceRecognizer {
	protected static Map<String, String> mParamMap = new HashMap<String, String>();

	protected static SpeechRecognizer mIat;

	protected static File file = new File("d:/content.txt");

	/**
	 * 听写监听器
	 */
	protected static class MyRecognizerListener implements RecognizerListener {

		@Override
		public void onBeginOfSpeech() {
			// System.out.println("听写中...");
		}

		@Override
		public void onEndOfSpeech() {
			// 监听
		}

		/**
		 * 获取听写结果. 获取RecognizerResult类型的识别结果，并对结果进行累加，显示到Area里
		 */
		@Override
		public void onResult(RecognizerResult results, boolean islast) {
			if (results.getResultString() == null || results.getResultString().equals("")) {
				return;
			}
			// 如果要解析json结果，请考本项目示例的 com.iflytek.util.JsonParser类
			String text = JsonParser.parseIatResult(results.getResultString());
			// String text = results.getResultString();
			writeContent(text);
		}

		@Override
		public void onVolumeChanged(int volume) {

		}

		@Override
		public void onError(SpeechError error) {
			System.out.println("onError enter");

		}

		@Override
		public void onEvent(int eventType, int arg1, int agr2, String msg) {
			System.out.println("onEvent enter");
		}
	};

	protected static void writeContent(String text) {
		try (FileWriter fw = new FileWriter(file, true);) {
			fw.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static boolean quit(String command) {
		return "q".equalsIgnoreCase(command);
	}

	protected static class SpeakDefaultValue {
		public static final String ENG_TYPE = SpeechConstant.TYPE_CLOUD;
		public static final String SPEECH_TIMEOUT = "60000";
		public static final String NET_TIMEOUT = "20000";
		public static final String LANGUAGE = "zh_cn";

		public static final String ACCENT = "mandarin";
		public static final String DOMAIN = "iat";
		public static final String VAD_BOS = "5000";
		public static final String VAD_EOS = "1800";

		public static final String RATE = "16000";
		public static final String NBEST = "1";
		public static final String WBEST = "1";
		public static final String PTT = "1";

		public static final String RESULT_TYPE = "json";
		public static final String SAVE = "0";
	}

	protected static class FileSourceDefaultValue {
		// public static final String ENG_TYPE = SpeechConstant.TYPE_CLOUD;
		// public static final String SPEECH_TIMEOUT = "60000";
		// public static final String NET_TIMEOUT = "20000";
		public static final String LANGUAGE = "zh_cn";

		public static final String ACCENT = "mandarin";
		public static final String DOMAIN = "iat";
		public static final String AUDIO_SOURCE = "-1";
		// public static final String VAD_BOS = "5000";
		// public static final String VAD_EOS = "1800";
		//
		// public static final String RATE = "16000";
		// public static final String NBEST = "1";
		// public static final String WBEST = "1";
		// public static final String PTT = "1";
		//
		// public static final String RESULT_TYPE = "json";
		// public static final String SAVE = "0";
	}
}
