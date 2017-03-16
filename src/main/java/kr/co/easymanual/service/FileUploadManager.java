package kr.co.easymanual.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.easymanual.dao.EmAttachmentsMapper;
import kr.co.easymanual.dao.EmLangsetMapper;
import kr.co.easymanual.exception.FileSaveException;
import kr.co.easymanual.model.EmAttachments;
import kr.co.easymanual.task.Index;
import kr.co.easymanual.utils.TbxUtils;

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
	private EmLangsetMapper emLangsetMapper;

	@Autowired
	private String attachmentsDirectory;

	@Autowired
	private ThreadPoolTaskExecutor taskExcutor;

	@Transactional
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

	// http://netframework.tistory.com/entry/Spring-Transactional%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC
	// private 메소드에서의 @Transactional 설정은 작동하지 않는다.
	// @Transactional
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
				throw new FileSaveException();
			}

			// 2. TBX 파일의 <martif type="TBX" xml:lang="en"> 태그로 부터 langset(charset) 정보 가져오기
			String workinglangSet = TbxUtils.getCharsetFromMartif(path);

			// 3. DB에 INSERT 하기
			EmAttachments emAttachments = new EmAttachments();
			emAttachments.setName(name);
			emAttachments.setLangset(workinglangSet);
			emAttachments.setHashName(hashName);
			emAttachments.setPath(path);
			emAttachments.setExtension(extension);
			emAttachments.setSize(size);
			emAttachments.setUploader(uploader);
			emAttachments.setCreatedTime(createdTime);
			emAttachments.setUpdatedTime(updatedTime);
			// 레코드 하나 등록 성공 시 1 리턴
			this.emAttachmentsMapper.insertSelective(emAttachments);

			// 4. TBX 파일의 termEntry 태그 하위의 모든 langSet 태그에 설정되어 있는 langSet 정보 INSERT
			List<String> langSetList = TbxUtils.getAllLangSet(path);

			for(String langSet: langSetList) {
				//langSet = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
				this.emLangsetMapper.insertByHashName(langSet, hashName);
			}

			// 5. 인덱싱 작업 하기
			this.taskExcutor.execute(new Index(path));
		}
	}

	private void saveFile(String path, InputStream inputStream) throws IOException {
			FileUtils.copyInputStreamToFile(inputStream, new File(path));
	}
}