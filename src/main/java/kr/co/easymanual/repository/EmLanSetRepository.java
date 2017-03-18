package kr.co.easymanual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.co.easymanual.entity.EmLangSet;

public interface EmLanSetRepository extends JpaRepository<EmLangSet, Integer> {
	@Query(value="SELECT DISTINCT(langset) FROM em_langset", nativeQuery=true)
	List<String> findDistinctLangSet();
}
