package com.tiance.speech;

import java.io.File;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

/**
 * 对音频进行重编码的工具类
 * 
 * @author jtian
 *
 */
public class MultimediaUtils {

	public static void main(String[] args) throws Exception {
		File source = new File("d:\\audio\\android.mp3");
		File target = new File("d:\\audio\\target.pcm");

		convertMp3ToPcm(source, target);
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
	public static void convertMp3ToPcm(File source, File target)
			throws IllegalArgumentException, InputFormatException, EncoderException {
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("s16le"); // 设置目标文件的封装格式,使用pcm的一种封装格式s16le

		// 设置目标语音的属性
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("pcm_s16le"); // 设置编码格式
		audio.setSamplingRate(16000); // 设置采样率
		audio.setChannels(1); // 设置声道数,即单声道
		attrs.setAudioAttributes(audio);

		Encoder encoder = new Encoder();
		encoder.encode(source, target, attrs); // 开始转换
		System.out.println("****mp3 转 pcm转换成功****");
	}
}
