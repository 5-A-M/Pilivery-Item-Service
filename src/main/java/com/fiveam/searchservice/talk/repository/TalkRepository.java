package com.fiveam.searchservice.talk.repository;

import com.fiveam.searchservice.item.entity.Item;
import com.fiveam.searchservice.talk.entity.Talk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalkRepository extends JpaRepository<Talk, Long> {

    List<Talk> findAllByUserId(Long userId);

    Page<Talk> findAllByItem(Pageable pageable, Item item);
}
