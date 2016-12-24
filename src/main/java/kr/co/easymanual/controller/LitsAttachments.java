package kr.co.easymanual.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.easymanual.service.FileDeletionManager;
import kr.co.easymanual.service.FileDownloadManager;
import kr.co.easymanual.service.FileUploadManager;
import kr.co.easymanual.service.ListAttachmentsManager;
import kr.co.easymanual.utils.Return;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@Controller
public class LitsAttachments {
	private static final Logger logger = LoggerFactory.getLogger(LitsAttachments.class);

	@Autowired
	private ListAttachmentsManager listAttachmentsManager;

	@Autowired
	private FileDownloadManager fileDownloadManager;

	@Autowired
	private FileDeletionManager fileDeletionManager;

	@Autowired
	private FileUploadManager fileUploadManager;

/*
	@RequestMapping(value = {"/", "/listAttachments.do"}, method = {RequestMethod.GET})
	public String listAttachmentsGet(Model model) {
		model.addAttribute("listAttachments", this.listAttachmentsManager.listAttachments());
		return "listAttachments";
	}
*/
	@RequestMapping(value = {"/listAttachments.do"}, method = {RequestMethod.GET})
	public String listAttachmentsGet(
			@RequestParam(value = "fieldName", defaultValue = "created_time") String fieldName,
			@RequestParam(value = "sort", defaultValue = "DESC") String sort,
			Model model) {
		model.addAttribute("listAttachments", this.listAttachmentsManager.listAttachments(fieldName, sort));

		return "tiles:listAttachments";
	}

	@RequestMapping(value = {"/downloadFile.do"}, method = {RequestMethod.GET})
	public void downloadFileGet(
			@RequestParam(value = "id") String id,
			@RequestParam(value = "extension") String extension,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {

			this.fileDownloadManager.download(id, extension, httpServletRequest, httpServletResponse);
	}

//	@RequestMapping(value = {"/downloadFile.do"}, method = {RequestMethod.GET})
//	@ResponseBody
//	public Map<String, String> downloadFileGet(
//			@RequestParam(value = "id") String id,
//			@RequestParam(value = "extension") String extension,
//			HttpServletRequest httpServletRequest,
//			HttpServletResponse httpServletResponse) throws IOException {
//
//			this.fileDownloadManager.download(id, extension, httpServletRequest, httpServletResponse);
//
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("stauts", "success");
//			return map;
//	}

	@RequestMapping(value = {"/deleteLists.do"}, method = {RequestMethod.POST})
	public String deleteListsPOST(@RequestParam(value = "list") List<String> list) {
		this.fileDeletionManager.deleteList(list);
		return "tiles:listAttachments";
	}

	@RequestMapping(value = "/uploadByAjaxP.do", method = {RequestMethod.GET})
	public String uploadByAjaxPGet() {
		return "uploadByAjaxP";
	}

	@RequestMapping(value = "/uploadByAjaxP.do", method = {RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> uploadByAjaxPPost(MultipartHttpServletRequest multipartHttpServletRequest) throws IOException {
		List<MultipartFile> multiPartFiles = multipartHttpServletRequest.getFiles("upload");

		try {
			this.fileUploadManager.insertAndindexing(multiPartFiles);
		} catch (IOException e) {
			logger.error("", e);
			throw new IOException();
		}

		return Return.success();
	}
}
