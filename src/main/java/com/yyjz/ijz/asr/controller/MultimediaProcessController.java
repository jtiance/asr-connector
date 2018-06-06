package com.yyjz.ijz.asr.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yyjz.ijz.asr.service.MultimediaProcessService;
import com.yyjz.ijz.asr.vo.RecognizeResult;

/**
 * 多媒体处理Controller
 * 
 * @author jtian
 *
 */
@Controller
@RequestMapping(value = "multimedia")
public class MultimediaProcessController {

	private static Logger logger = LoggerFactory.getLogger(MultimediaProcessController.class);

	@Autowired
	private MultimediaProcessService multimediaProcessService;

	/**
	 * 识别mp3流为文字
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "recognize/mp3", method = RequestMethod.POST)
	@ResponseBody
	public RecognizeResult recognizeMP3(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		RecognizeResult result = new RecognizeResult();

		try {
			String pathToSave = request.getServletContext().getRealPath("");
			String content = multimediaProcessService.recognizeMP3Stream(file.getInputStream(), pathToSave);
			result.setContent(content);
		} catch (Exception e) {
			logger.error("error while recognize mp3 stream", e);
			result.setSuccess(false);
			result.setErrorMsg(e.getMessage());
		}
		return result;

	}
}
