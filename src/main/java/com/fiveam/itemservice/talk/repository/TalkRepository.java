package com.fiveam.itemservice.talk.repository;

import com.fiveam.itemservice.item.entity.Item;
import com.fiveam.itemservice.talk.entity.Talk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalkRepository extends JpaRepository<Talk, Long> {

    List<Talk> findAllByUserId(Long userId);

    Page<Talk> findAllByItem(Pageable pageable, Item item);
}
