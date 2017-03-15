package kr.co.easymanual.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="em_langset")
@Data
public class EmLangSet {
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;

	@Column(name="langset")
	private String langSet;

	// 외래키를 이 테이블에 만들어야 하니, 이 Entity에다 무언가 설정을 해주어야 하겠지? -> 엉. 가설이 맞았어
	// 이 테이블의 서로 다른 Id 여러 개가 하나의 FK를 가질 수 있으니 @ManyToOne 으로 해볼까? -> 성공

	// 나는 왜 아래의 설정이 이 테이블에 필드를 추가한다고 생각했을까? -> JoinColumn(name="attachment_id")을 어떻게 설정하느냐에 맞는 가설, 틀린 가설이 될 수 있다.
	@ManyToOne
	@JoinColumn(name="attachment_id", insertable=false, updatable=false)
	private EmAttachments emAttachments;
}
