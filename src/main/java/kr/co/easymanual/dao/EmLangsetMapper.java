package kr.co.easymanual.dao;

import java.util.List;

import kr.co.easymanual.model.EmLangset;
import kr.co.easymanual.model.EmLangsetExample;

import org.apache.ibatis.annotations.Param;

public interface EmLangsetMapper {
    int countByExample(EmLangsetExample example);

    int deleteByExample(EmLangsetExample example);

    int insert(EmLangset record);

    int insertSelective(EmLangset record);

    List<EmLangset> selectByExample(EmLangsetExample example);

    int updateByExampleSelective(@Param("record") EmLangset record, @Param("example") EmLangsetExample example);

    int updateByExample(@Param("record") EmLangset record, @Param("example") EmLangsetExample example);

    int insertByHashName(@Param("langSet") String langSet, @Param("hashName") String hashName);

    List<String> selectDistinctByLangSet();
}