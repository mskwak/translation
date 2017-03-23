package kr.co.easymanual.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.easymanual.exception.FileDownloadException;
import kr.co.easymanual.service.FileDeletionService;
import kr.co.easymanual.service.FileDownloadService;
import kr.co.easymanual.service.ListAttachmentsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class Download {
	private static final Logger logger = LoggerFactory.getLogger(Download.class);

	@Autowired
	private ListAttachmentsService listAttachmentsService;

	@Autowired
	private FileDownloadService fileDownloadService;

	@Autowired
	private FileDeletionService fileDeletionService;

	@RequestMapping(value = {"/download.do"}, method = {RequestMethod.GET})
	public String downloadGet(Model model) {
		model.addAttribute("charsetMap", this.fileDownloadService.getCharsetMap());
		return "download";
	}

	@RequestMapping(value = {"/downloadFile.do"}, method = {RequestMethod.GET})
	public void downloadFileGet(
		@RequestParam(value = "charset") String charset,
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) throws FileDownloadException {

		try {
			this.fileDownloadService.download(charset, httpServletRequest, httpServletResponse);
		} catch (IOException e) {
			throw new FileDownloadException(charset);
		}
	}
}
