package com.study.springboot.repository;

import com.study.springboot.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Modifying
    @Transactional
    @Query(value = "insert into notice(seq, title, content, parent) values " +
            "(:#{#notice.seq}, :#{#notice.title}, :#{#notice.content}, :#{#notice.parent}) "
            , nativeQuery = true)
    public void insertNotice(@Param("notice") Notice notice);

    @Query(value = "select max(seq) from notice ", nativeQuery = true)
    public Long selectMaxSeq();

    @Query(value = "select * from notice order by seq desc", nativeQuery = true)
    public List<Notice> selectOrderSeq();

    public List<Notice> findAllByOrderBySeqDesc();
}
