package com.fiveam.searchservice.talk.repository;

import com.fiveam.searchservice.talk.entity.TalkComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalkCommentRepository extends JpaRepository<TalkComment, Long> {

    List<TalkComment> findAllByUserId(Long userId);
}
