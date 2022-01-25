package org.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex2.entity.Memo;

import java.util.List;


//<Memo,Long> Entity의 type정보와  @Id 타입
public interface MemoRepository extends JpaRepository<Memo,Long> {

    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from,Long to);

    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    void deleteMemoByMnoLessThan(Long num);

    @Query("select m from Memo m order by m.mno asc")
    List<Memo> getListAsec(Long from,Long to,Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText= :memoText where m.mno=:mno")
    int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);
}
