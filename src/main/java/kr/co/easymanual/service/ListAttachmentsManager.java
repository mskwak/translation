package kr.co.easymanual.service;

import java.util.List;

import kr.co.easymanual.dao.EmAttachmentsMapper;
import kr.co.easymanual.model.EmAttachments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListAttachmentsManager {
	@Autowired
	private EmAttachmentsMapper emAttachmentsMapper;

	public List<EmAttachments> listAttachments() {
		return this.emAttachmentsMapper.selectAll();
	}

	public List<EmAttachments> listAttachments(String fieldName, String sort) {
		return this.emAttachmentsMapper.selectAllByField(fieldName, sort);
	}
}
