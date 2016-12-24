package kr.co.easymanual.dao;

import java.util.List;

import kr.co.easymanual.model.EmAttachments;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmAttachmentsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EmAttachments emAttachments);

    int insertSelective(EmAttachments emAttachments);

    EmAttachments selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EmAttachments emAttachments);

    int updateByPrimaryKey(EmAttachments emAttachments);

    List<EmAttachments> selectAll();

    List<EmAttachments> selectAllByField(@Param("fieldName") String fieldName, @Param("sort") String sort);

    EmAttachments selectByHashName(String hashName);
}