package kr.co.easymanual.service;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.easymanual.entity.EmAttachments;
import kr.co.easymanual.repository.EmAttachmentsRepository;

@Service
public class FileDeletionManager {
	private static final Logger logger = LoggerFactory.getLogger(FileDeletionManager.class);

	@Autowired
	EmAttachmentsRepository emAttachmentsRepository;

	public void deleteList(List<String> list) {
		for(String identity: list) {
			int id = Integer.parseInt(identity);
			EmAttachments emAttachments = this.emAttachmentsRepository.findOne(id);

			if(emAttachments != null) {
				File file = new File(emAttachments.getPath());
				FileUtils.deleteQuietly(file);
				this.emAttachmentsRepository.delete(id);
				logger.info("delete id: " + id);
			}
		}
	}
}
