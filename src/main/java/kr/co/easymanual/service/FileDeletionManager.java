package kr.co.easymanual.service;

import java.io.File;
import java.util.List;

import kr.co.easymanual.dao.EmAttachmentsMapper;
import kr.co.easymanual.model.EmAttachments;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileDeletionManager {
	private static final Logger logger = LoggerFactory.getLogger(FileDeletionManager.class);

	@Autowired
	private EmAttachmentsMapper emAttachmentsMapper;

	public void deleteList(List<String> list) {
		for(String identity: list) {
			int id = Integer.parseInt(identity);
			EmAttachments emAttachments = this.emAttachmentsMapper.selectByPrimaryKey(id);

			if(emAttachments != null) {
				File file = new File(emAttachments.getPath());
				FileUtils.deleteQuietly(file);
				int result = this.emAttachmentsMapper.deleteByPrimaryKey(id);
				logger.info("delete id: " + id + " return value: " + result);
			}
		}
	}
}
