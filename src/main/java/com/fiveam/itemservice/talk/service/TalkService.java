package com.fiveam.itemservice.talk.service;

import com.fiveam.itemservice.exception.bussiness.BusinessLogicException;
import com.fiveam.itemservice.exception.bussiness.ExceptionCode;
import com.fiveam.itemservice.item.entity.Item;
import com.fiveam.itemservice.talk.entity.Talk;
import com.fiveam.itemservice.talk.repository.TalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TalkService {

    private final TalkRepository talkRepository;

    public Talk createTalk(Talk talk) { // 토크 작성 - 상세페이지
        return talkRepository.save(talk);
    }

    public Talk updateTalk(Talk talk) { // 토크 수정 - 상세페이지, 마이페이지
        Talk findTalk = findVerifiedTalk(talk.getTalkId());
        findTalk.setContent(talk.getContent());
        Talk updatedTalk = talkRepository.save(findTalk);

        return updatedTalk;
    }

    public Talk findTalk(long talkId) { // 특정 토크 찾기
        Talk talk = findVerifiedTalk(talkId);

        return talk;
    }

    public List<Talk> findTalks(Long userId) { // 유저의 토크 목록 조회
        List<Talk> talksByUser = talkRepository.findAllByUserId(userId);

        return talksByUser;
    }

    public Page<Talk> findItemTalks(Item item, int page, int size) {
        Page<Talk> pageTalk = talkRepository.findAllByItem(
                PageRequest.of(page, size, Sort.by("talkId").descending()), item);

        return pageTalk;
    }

    public void deleteTalk(long talkId, long userId) { // 토크 삭제 - 작성자만 가능
        Talk talk = findVerifiedTalk(talkId);
        long writerId = findTalkWriter(talkId);

        if(userId != writerId) {
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED_USER);
        }

        talkRepository.delete(talk);
    }

    public long findTalkWriter(long talkId) { // 작성자만 수정, 삭제를 할 수 있도록 작성자 찾기
        Talk talk = findVerifiedTalk(talkId);

        return talk.getUserId();
    }

    public Talk findVerifiedTalk(long talkId) {
        Optional<Talk> optionalTalk = talkRepository.findById(talkId);
        Talk findTalk = optionalTalk.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.TALK_NOT_FOUND));

        return findTalk;
    }
}
