package com.yyjz.speech.aip;

import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;

/**
 * 语音识别工具测试类
 * 
 * @author jtian
 *
 */
public class AipVoiceRecognizer {
	// 从baidu云获取,设置APPID/AK/SK
	public static final String APP_ID = "11318880";
	public static final String API_KEY = "10FGV80epwTwo9EcPqIWRamc";
	public static final String SECRET_KEY = "iDnODuyhkp9weYtrGTIXwWlt5KCsPbWR1";

	public static void main(String[] args) {
		// 初始化一个AipSpeech
		AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);

		// 可选：设置log4j日志输出格式，若不设置，则使用默认配置
		// 也可以直接通过jvm启动参数设置此环境变量
		System.setProperty("aip.log4j.conf", "log4j.properties");

		String file = "d:\\audio/target.pcm";
		// 调用接口asr(文件, 格式, 采样率, null)
		JSONObject res = client.asr(file, "pcm", 16000, null);
		System.out.println(res.toString(2));

	}
}
