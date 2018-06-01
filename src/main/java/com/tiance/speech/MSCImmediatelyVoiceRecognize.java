package com.tiance.speech;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUtility;

/**
 * 科大讯飞语音识别接口<br >
 * 即刻翻译
 * 
 * @author jtian
 *
 */
public class MSCImmediatelyVoiceRecognize {

	private static Map<String, String> mParamMap = new HashMap<String, String>();

	private static SpeechRecognizer mIat;

	private static File file = new File("d:/content.txt");

	private static void initParamMap() {
		mParamMap.put(SpeechConstant.ENGINE_TYPE, DefaultValue.ENG_TYPE);
		mParamMap.put(SpeechConstant.SAMPLE_RATE, DefaultValue.RATE);
		mParamMap.put(SpeechConstant.NET_TIMEOUT, DefaultValue.NET_TIMEOUT);
		mParamMap.put(SpeechConstant.KEY_SPEECH_TIMEOUT, DefaultValue.SPEECH_TIMEOUT);

		mParamMap.put(SpeechConstant.LANGUAGE, DefaultValue.LANGUAGE);
		mParamMap.put(SpeechConstant.ACCENT, DefaultValue.ACCENT);
		mParamMap.put(SpeechConstant.DOMAIN, DefaultValue.DOMAIN);
		mParamMap.put(SpeechConstant.VAD_BOS, DefaultValue.VAD_BOS);

		mParamMap.put(SpeechConstant.VAD_EOS, DefaultValue.VAD_EOS);
		mParamMap.put(SpeechConstant.ASR_NBEST, DefaultValue.NBEST);
		mParamMap.put(SpeechConstant.ASR_WBEST, DefaultValue.WBEST);
		mParamMap.put(SpeechConstant.ASR_PTT, DefaultValue.PTT);

		mParamMap.put(SpeechConstant.RESULT_TYPE, DefaultValue.RESULT_TYPE);
		mParamMap.put(SpeechConstant.ASR_AUDIO_PATH, null);
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
		RecognizerListener recognizerListener = new RecognizerListener() {

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

				// 如果要解析json结果，请考本项目示例的 com.iflytek.util.JsonParser类
//				String text = JsonParser.parseIatResult(results.getResultString());
				 String text = results.getResultString();
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

		Scanner scanner = new Scanner(System.in);
		int i = 0;
		while (i == 0) {
			System.out.println("输入r开始录制，输入q退出程序:");
			String command = scanner.next();

			if (quit(command)) {
				break;
			}

			boolean speak = false;
			if (command.equalsIgnoreCase("r")) {
				if (!mIat.isListening()) {
					mIat.startListening(recognizerListener);
					speak = true;
				} else {
					System.out.println("已经在录制了!");
				}
			}
			if (speak) {
				System.out.println("开始录制...输入s停止并识别:");
				command = scanner.next();
				if (quit(command)) {
					break;
				}

				if (command.equalsIgnoreCase("s")) {
					mIat.stopListening();
					writeContent("\n");
				}
			}
		}
		scanner.close();
	}

	private static boolean quit(String command) {
		return "q".equalsIgnoreCase(command);
	}

	private static void writeContent(String text) {
		try (FileWriter fw = new FileWriter(file, true);) {
			fw.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class DefaultValue {
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
}
