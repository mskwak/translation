package kr.co.easymanual.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.easymanual.dao.EmAttachmentsMapper;
import kr.co.easymanual.dao.EmLangsetMapper;
import kr.co.easymanual.model.EmAttachments;
import kr.co.easymanual.utils.GeneralUtils;
import kr.co.easymanual.utils.TbxUtils;

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
	private EmLangsetMapper emLangsetMapper;

	@Autowired
	private String attachmentsDirectory;

	// TODO 한글이름 다운로드 안되는 문제 해결 필요
	public void download(String charset, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		List<EmAttachments> emAttachmentsList = this.getUniqEmAttachmentsList(this.emAttachmentsMapper.selectByLangSet(this.getCharsetList(charset)));

		if(emAttachmentsList.isEmpty()) {
			throw new FileNotFoundException();
		}

		if(emAttachmentsList.size() > 1) {
			this.getZipFile(emAttachmentsList, httpServletRequest, httpServletResponse);
		} else {
			this.getOriginalFile(emAttachmentsList.get(0), httpServletRequest, httpServletResponse);
		}

		return;
	}

	/**
	 * 파일 이름이 중복되는 경우 zip 으로 압축 시 에러가 발생한다. 따라서 파일 이름이 중복되지 않게 처리할 필요가 있었다.
	 * 처리 로직 및 전제 조건은 다음과 같다.
	 * 1. 테이블 레코드 상에 동일한 파일 이름이 있는 경우 그 파일들은 동일한 파일이라 가정했다.
	 * 2. 테이블 레코드 상에 같은 이름이 존재할 경우, created_time 필드의 값이 최신인 레코드의 값을 취한다.
	 * 3. 가장 최신의 레코드가 맵에 담기도록 하기 위해서 쿼리는 테이블에서 가장 오래된 레코드가 리스트에서 앞의 요소에 위치하도록 했다.
	 */
	private List<EmAttachments> getUniqEmAttachmentsList(List<EmAttachments> emAttachmentsList) {
		Map<String, EmAttachments> map = new HashMap<String, EmAttachments>();
		for(EmAttachments emAttachments: emAttachmentsList) {
			map.put(emAttachments.getName(), emAttachments);
			//System.out.println(emAttachments.getPath());
		}

		return new ArrayList<EmAttachments>(map.values());
	}

	// TODO 아무 생각없이 Copy & Paste 한 코드. 중복제거 및 리팩토링 필요
    public void getZipFile(List<EmAttachments> emAttachmentsList, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
    	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    	ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
    	byte bytes[] = new byte[2048];

    	for (EmAttachments e : emAttachmentsList) {
    		FileInputStream fileInputStream = new FileInputStream(e.getPath());
    		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
    		zipOutputStream.putNextEntry(new ZipEntry(e.getName()));

    		int bytesRead;
    		while ((bytesRead = bufferedInputStream.read(bytes)) != -1) {
    			zipOutputStream.write(bytes, 0, bytesRead);
    		}

    		zipOutputStream.closeEntry();
    		bufferedInputStream.close();
    		fileInputStream.close();
    	}

    	zipOutputStream.flush();
    	byteArrayOutputStream.flush();
    	zipOutputStream.close();
    	byteArrayOutputStream.close();

    	httpServletResponse.setHeader("Content-Type", "application/zip");
    	httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + this.getEncodedFileName("tbx.zip") + ";");
    	//httpServletResponse.setContentLength(byteArrayOutputStream.size());
		httpServletResponse.setHeader("Content-Transfer-Encoding", "binary;");
		httpServletResponse.setHeader("Pragma", "no-cache;");
		httpServletResponse.setHeader("Expires", "-1;");

    	OutputStream outputStreamm = httpServletResponse.getOutputStream();
    	outputStreamm.write(byteArrayOutputStream.toByteArray());
    }

	private void getOriginalFile(EmAttachments emAttachments, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		File file = new File(emAttachments.getPath());
		int size = emAttachments.getSize().intValue();

		if(!file.exists()) {
			logger.error("file not exist. " + "file: " + file.toString());
			throw new FileNotFoundException();
		}

		httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + this.getEncodedFileName(emAttachments.getName()) + ";");
		httpServletResponse.setHeader("Content-Type", "application/octet-stream");
		//httpServletResponse.setHeader("Content-Type", "application/download");
		httpServletResponse.setContentLength(size);
		httpServletResponse.setHeader("Content-Transfer-Encoding", "binary;");
		httpServletResponse.setHeader("Pragma", "no-cache;");
		httpServletResponse.setHeader("Expires", "-1;");

		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
		OutputStream outputStream = httpServletResponse.getOutputStream();
		FileCopyUtils.copy(bufferedInputStream, outputStream);
	}
/*
    public void getZipFile(List<EmAttachments> emAttachmentsList, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ArchiveException {
    	File file = new File("xxx.zip");
        OutputStream outputStream = new FileOutputStream(file);
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);

        for (EmAttachments e : emAttachmentsList) {
            String fileName = e.getName();
            ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(fileName);
            archiveOutputStream.putArchiveEntry(zipArchiveEntry);

            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(e.getPath())));

            IOUtils.copy(bufferedInputStream, archiveOutputStream);
            bufferedInputStream.close();
            archiveOutputStream.closeArchiveEntry();
        }

        OutputStream outputStreamm = httpServletResponse.getOutputStream();
        outputStreamm.write(outputStream.);
        FileCopyUtils.copy(outputStream, outputStream);

		httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + this.getEncodedFileName("xxx.zip" + ";"));
		httpServletResponse.setHeader("Content-Type", "application/zip");
		//httpServletResponse.setHeader("Content-Type", "application/download");
		httpServletResponse.setContentLength(size);
		httpServletResponse.setHeader("Content-Transfer-Encoding", "binary;");
		httpServletResponse.setHeader("Pragma", "no-cache;");
		httpServletResponse.setHeader("Expires", "-1;");

        archiveOutputStream.finish();
        outputStream.close();
    }
  */

//		for(EmAttachments emAttachments: emAttachmentsList) {
//			logger.info("aaaaaaaaaa: " + emAttachments.toString());
//		}



//		String fileName = FilenameUtils.getBaseName(emAttachments.getName()) + "." + extension;
//		File path = new File(emAttachments.getPath());
//		int size = emAttachments.getSize().intValue();

//		if(!path.exists()) {
//			logger.error("file not exist. " + "file: " + fileName);
//			throw new FileNotFoundException();
//		}
//
//		httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + this.getEncodedFileName(fileName) + ";");
//		httpServletResponse.setHeader("Content-Type", "application/octet-stream");
//		//httpServletResponse.setHeader("Content-Type", "application/download");
//		httpServletResponse.setContentLength(size);
//		httpServletResponse.setHeader("Content-Transfer-Encoding", "binary;");
//		httpServletResponse.setHeader("Pragma", "no-cache;");
//		httpServletResponse.setHeader("Expires", "-1;");
//
//		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path));
//		OutputStream outputStream = httpServletResponse.getOutputStream();
//		FileCopyUtils.copy(bufferedInputStream, outputStream);
//	}
//
	private String getEncodedFileName(String fileName) {
		String encodedFileName = null;

		try {
			encodedFileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		}

		return encodedFileName;
	}

	/**
	 * tbx 문서 파일에서 langSet의 종류가 en-us, en_us, en, en-US, en_US 등 다양하게 표현될 수 있다.
	 * 이러한 charset에 대해 모두 대응할 수 있어야 했으며, 이로 인해 이 메소드가 추가되었다.
	 * 1. 우선 jsp 파일에서 charset 은 en-us 와 같이 - 로 연결되어 있다.
	 * 2. en-us를 받았을 때, en_us, en 2가지 포맷을 더 생성하여 리턴한다.
	 */
	private List<String> getCharsetList(String charset) {
		String charset1 = charset.replaceAll("-", "_");
		String[] charset2 = charset.split("-");

		List<String> list = new ArrayList<String>();
		list.add(charset);
		list.add(charset1);
		list.add(charset2[0]);

		return list;
	}

	/**
	 * 다운로드 페이지에서 charset에 대응하는 국가 이름이 없는 파일은 다운로드 하지 못하도록 뷰에서 제거한다.
	 */
	public Map<String, String> getCharsetMap() {
		List<String> list = this.emLangsetMapper.selectDistinctByLangSet();
		Map<String, String> map = new HashMap<String, String>();

		for(String s: list) {
			String c = TbxUtils.getCountryName(s);

			if("Unknown".equals(c)) {
				continue;
			}

			map.put(s, c);
		}

		return GeneralUtils.sortByValue(map);
	}
}