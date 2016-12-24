package kr.co.easymanual.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import kr.co.easymanual.dao.EmAttachmentsMapper;
import kr.co.easymanual.model.EmAttachments;
import kr.co.easymanual.task.Index;
import kr.co.easymanual.utils.TbxUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author mskw
 * 1. 파일 저장
 * 2. TBX 파일로 부터 국가 정보 가져오기
 * 3. DB에 INSERT 하기
 * 4. 인덱싱 작업 하기
 */

@Service
public class FileUploadManager {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadManager.class);

	@Autowired
	private EmAttachmentsMapper emAttachmentsMapper;

	@Autowired
	private String attachmentsDirectory;

	@Autowired
	private ThreadPoolTaskExecutor taskExcutor;

	public void insertAndindexing(List<MultipartFile> multiPartFiles) throws IOException {
			this.prepare();
			this.process(multiPartFiles);
	}

	private void prepare() {
/*
		Path path = Paths.get(this.attachmentsDirectory);
		Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-xr-x");
		FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
		Files.createDirectory(path, attr);
*/
		// TODO Path 인터페이스로 교체
		File file = new File(this.attachmentsDirectory);
		if(!file.exists()) {
			file.mkdir();
		}
	}

	private void process(List<MultipartFile> multiPartFiles) throws IOException {
		for(MultipartFile multipartFile: multiPartFiles) {
			String name = multipartFile.getOriginalFilename();
			String hashName = UUID.randomUUID().toString(); // 3e2841f7-2710-4072-9969-4ac401ae422d
			String path = this.attachmentsDirectory + "/" + hashName;
			String extension = FilenameUtils.getExtension(name);
			long size = multipartFile.getSize();
			String uploader = StringUtils.defaultString(null, "Anonymous");
			Date createdTime = new Date();
			Date updatedTime = new Date();

			// 1. 파일 저장
			try {
				this.saveFile(path, multipartFile.getInputStream());
			} catch (IOException e) {
				throw new IOException();
			}

			// 2. TBX 파일로 부터 langset(charset) 정보 가져오기
			String langSet = TbxUtils.getLangSet(path);

			// 3. DB에 INSERT 하기
			EmAttachments emAttachments = new EmAttachments();
			emAttachments.setName(name);
			emAttachments.setLangset(langSet);
			emAttachments.setHashName(hashName);
			emAttachments.setPath(path);
			emAttachments.setExtension(extension);
			emAttachments.setSize(size);
			emAttachments.setUploader(uploader);
			emAttachments.setCreatedTime(createdTime);
			emAttachments.setUpdatedTime(updatedTime);

			// 레코드 하나 등록 성공 시 1 리턴
			this.emAttachmentsMapper.insertSelective(emAttachments);

			// 4. 인덱싱 작업 하기
			this.taskExcutor.execute(new Index(path));
		}
	}

	private void saveFile(String path, InputStream inputStream) throws IOException {
			FileUtils.copyInputStreamToFile(inputStream, new File(path));
	}
}