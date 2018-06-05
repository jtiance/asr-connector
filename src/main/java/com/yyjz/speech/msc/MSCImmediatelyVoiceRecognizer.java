package com.yyjz.speech.msc;

import java.util.Map.Entry;
import java.util.Scanner;

import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUtility;

/**
 * 科大讯飞语音识别接口<br >
 * 即刻翻译
 * 
 * @author jtian
 *
 */
public class MSCImmediatelyVoiceRecognizer extends MSCVoiceRecognizer {

	protected static void initParamMap() {
		mParamMap.put(SpeechConstant.ENGINE_TYPE, SpeakDefaultValue.ENG_TYPE);
		mParamMap.put(SpeechConstant.SAMPLE_RATE, SpeakDefaultValue.RATE);
		mParamMap.put(SpeechConstant.NET_TIMEOUT, SpeakDefaultValue.NET_TIMEOUT);
		mParamMap.put(SpeechConstant.KEY_SPEECH_TIMEOUT, SpeakDefaultValue.SPEECH_TIMEOUT);

		mParamMap.put(SpeechConstant.LANGUAGE, SpeakDefaultValue.LANGUAGE);
		mParamMap.put(SpeechConstant.ACCENT, SpeakDefaultValue.ACCENT);
		mParamMap.put(SpeechConstant.DOMAIN, SpeakDefaultValue.DOMAIN);
		mParamMap.put(SpeechConstant.VAD_BOS, SpeakDefaultValue.VAD_BOS);

		mParamMap.put(SpeechConstant.VAD_EOS, SpeakDefaultValue.VAD_EOS);
		mParamMap.put(SpeechConstant.ASR_NBEST, SpeakDefaultValue.NBEST);
		mParamMap.put(SpeechConstant.ASR_WBEST, SpeakDefaultValue.WBEST);
		mParamMap.put(SpeechConstant.ASR_PTT, SpeakDefaultValue.PTT);

		mParamMap.put(SpeechConstant.RESULT_TYPE, SpeakDefaultValue.RESULT_TYPE);
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
		MyRecognizerListener listener = new MyRecognizerListener();

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
					mIat.startListening(listener);
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

}
