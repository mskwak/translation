package kr.co.easymanual.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.easymanual.dao.EmAttachmentsMapper;
import kr.co.easymanual.entity.EmAttachments;
import kr.co.easymanual.repository.EmAttachmentsRepository;

@Service
public class ListAttachmentsManager {
	@Autowired
	private EmAttachmentsMapper emAttachmentsMapper;

	@Autowired
	private EmAttachmentsRepository emAttachmentsRepository;

	public List<EmAttachments> listAttachments() {
		return this.emAttachmentsRepository.findAll();
	}

	public List<EmAttachments> listAttachments(String fieldName, String sort) {
		return this.emAttachmentsMapper.selectAllByField(fieldName, sort);
	}
}
