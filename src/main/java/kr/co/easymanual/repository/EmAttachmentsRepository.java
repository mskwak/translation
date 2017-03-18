package kr.co.easymanual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.easymanual.entity.EmAttachments;

public interface EmAttachmentsRepository extends JpaRepository<EmAttachments, Integer> {
	List<EmAttachments> findByCharsetIn(List<String> charsetList);
}
