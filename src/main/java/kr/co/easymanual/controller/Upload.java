package kr.co.easymanual.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import kr.co.easymanual.service.FileUploadManager;
import kr.co.easymanual.utils.Return;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class Upload {
	private static final Logger logger = LoggerFactory.getLogger(Upload.class);

	@Autowired
	private FileUploadManager fileUploadManager;

	@RequestMapping(value = {"/upload.do"}, method = {RequestMethod.GET})
	public String uploadGet() {
		return "upload";
	}

	// view에서 multiple 옵션에 의해 2개 이상의 파일을 업로드해도, 이 컨트롤러는 파일 하나마다 매번 호출된다.
	@RequestMapping(value = "/uploadByAjax.do", method = {RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> uploadByAjaxPost(MultipartHttpServletRequest multipartHttpServletRequest) throws IOException {
		List<MultipartFile> multiPartFiles = multipartHttpServletRequest.getFiles("uploadFile");

		try {
			this.fileUploadManager.insertAndindexing(multiPartFiles);
		} catch (IOException e) {
			logger.error("", e);
			throw new IOException();
		}

		return Return.success();
	}
}