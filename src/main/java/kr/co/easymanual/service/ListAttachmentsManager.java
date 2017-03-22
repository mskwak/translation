package kr.co.easymanual.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.easymanual.entity.EmAttachments;
import kr.co.easymanual.repository.EmAttachmentsRepository;

@Service
public class ListAttachmentsManager {
	@Autowired
	private EmAttachmentsRepository emAttachmentsRepository;

	public List<EmAttachments> listAttachments() {
		return this.emAttachmentsRepository.findAll();
	}

	public List<EmAttachments> listAttachments(String fieldName, String sort) {
		// Hibernate 버전
		// TODO 가변적인 데이터인 fieldName을 ORDER BY fieldName로 설정하여 spring data에서 지원하는 메서드를 작성할 수 있는가?
		return this.emAttachmentsRepository.findAll();

		// MyBatis 버전
		// <!-- List<EmAttachments> selectAllByField(@Param("fieldName") String fieldName, @Param("sort") String sort); -->
		// <select id="selectAllByField" parameterType="java.util.HashMap" resultType="java.util.HashMap">
		// SELECT <include refid="Base_Column_List" /> FROM em_attachments ORDER BY ${fieldName} ${sort}
		// </select>
		// return this.emAttachmentsMapper.selectAllByField(fieldName, sort);
	}
}
