package kr.co.easymanual.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.easymanual.dao.EmAttachmentsMapper;
import kr.co.easymanual.model.EmAttachments;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
public class FileDownloadManager {
	private static final Logger logger = LoggerFactory.getLogger(FileDownloadManager.class);

	@Autowired
	private EmAttachmentsMapper emAttachmentsMapper;

	@Autowired
	private String attachmentsDirectory;

	public void download(String id, String extension, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		//String id = FilenameUtils.getBaseName(idWithExtension);
		EmAttachments emAttachments = this.emAttachmentsMapper.selectByPrimaryKey(Integer.parseInt(id));

		String fileName = FilenameUtils.getBaseName(emAttachments.getName()) + "." + extension;
		File path = new File(emAttachments.getPath());
		int size = emAttachments.getSize().intValue();

		if(!path.exists()) {
			logger.error("file not exist. " + "file: " + fileName);
			throw new FileNotFoundException();
		}

		httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + this.getEncodedFileName(fileName) + ";");
		httpServletResponse.setHeader("Content-Type", "application/octet-stream");
		//httpServletResponse.setHeader("Content-Type", "application/download");
		httpServletResponse.setContentLength(size);
		httpServletResponse.setHeader("Content-Transfer-Encoding", "binary;");
		httpServletResponse.setHeader("Pragma", "no-cache;");
		httpServletResponse.setHeader("Expires", "-1;");

		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path));
		OutputStream outputStream = httpServletResponse.getOutputStream();
		FileCopyUtils.copy(bufferedInputStream, outputStream);
	}

	private String getEncodedFileName(String fileName) {
		String encodedFileName = null;

		try {
			encodedFileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		}

		return encodedFileName;
	}
}
