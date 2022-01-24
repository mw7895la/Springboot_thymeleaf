package org.zerock.ex2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex2.entity.Memo;

import java.util.List;


//<Memo,Long> Entity의 type정보와  @Id 타입
public interface MemoRepository extends JpaRepository<Memo,Long> {

    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from,Long to);
}
