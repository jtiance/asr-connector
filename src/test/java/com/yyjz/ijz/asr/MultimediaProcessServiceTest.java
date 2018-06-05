package com.yyjz.ijz.asr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.yyjz.ijz.asr.service.MultimediaProcessService;
import com.yyjz.ijz.asr.service.impl.MultimediaProcessServiceImpl;

public class MultimediaProcessServiceTest {

	private MultimediaProcessService multimediaProcessService;

	@Test
	public void testRecognizeMP3Stream() {
		System.out.println("start to test testRecognizeMP3Stream");

		multimediaProcessService = new MultimediaProcessServiceImpl();

		File file = new File("d:/audio/new2.mp3");

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);

			multimediaProcessService.recognizeMP3Stream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
