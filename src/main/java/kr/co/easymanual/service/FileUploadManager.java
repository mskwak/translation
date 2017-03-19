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

import kr.co.easymanual.entity.EmAttachments;
import kr.co.easymanual.entity.EmLangSet;
import kr.co.easymanual.exception.FileSaveException;
import kr.co.easymanual.repository.EmAttachmentsRepository;
import kr.co.easymanual.repository.EmLanSetRepository;
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
	private EmAttachmentsRepository emAttachmentsRepository;

	@Autowired
	private EmLanSetRepository emLanSetRepository;

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
			emAttachments.setLangSet(workinglangSet);
			emAttachments.setHashName(hashName);
			emAttachments.setPath(path);
			emAttachments.setExtension(extension);
			emAttachments.setSize(size);
			emAttachments.setUploader(uploader);
			emAttachments.setCreatedTime(createdTime);
			emAttachments.setUpdatedTime(updatedTime);

			// TODO 리턴값은 무엇일까? -> the saved emAttachments가 리턴값이다. 이 리턴된 emAttachments에는 id(시퀀스, PK)가 채워져 있겠지. -> 그렇다. 채워져있다.
			emAttachments = this.emAttachmentsRepository.save(emAttachments);

			// 4. TBX 파일의 termEntry 태그 하위의 모든 langSet 태그에 설정되어 있는 langSet 정보 INSERT
			List<String> langSetList = TbxUtils.getAllLangSet(path);

			for(String langSet: langSetList) {
				EmLangSet emLangSet = new EmLangSet();
				emLangSet.setLangSet(langSet);
				// EmAttachments 엔티티의 PK가  EmLangSet 엔티티 attachment_id 필드에  FK로 저장되는 것을 기대했다. -> 그렇다. 기대대로 FK로 저장된다.
				// 단, EmLangSet 엔티티 attachment_id 필드가 insertable=false, updatable=false로 되어 있을 경우 FK로 저장되지 않는다.
				emLangSet.setEmAttachments(emAttachments);
				this.emLanSetRepository.save(emLangSet);
			}

//			MyBatis를 이용했을 경우 아래와 같이 em_attachments 테이블로 부터 PK를 가져와 em_langset 테이블에 FK로 INSERT를 했다.
//			em_attachments 테이블의 primary key (serial type) 을 가져와서 em_langset 테이블에 INSERT 한다.
//			<insert id="insertByHashName" parameterType="map">
//			    INSERT INTO em_langset SELECT id, #{langSet,jdbcType=VARCHAR} FROM em_attachments WHERE hash_name = #{hashName,jdbcType=VARCHAR}
//			</insert>

//			for(String langSet: langSetList) {
//				this.emLangsetMapper.insertByHashName(langSet, hashName);
//			}

			// 5. 인덱싱 작업 하기
			this.taskExcutor.execute(new Index(path));
		}
	}

	private void saveFile(String path, InputStream inputStream) throws IOException {
			FileUtils.copyInputStreamToFile(inputStream, new File(path));
	}
}