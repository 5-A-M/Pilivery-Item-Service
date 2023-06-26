package com.fiveam.searchservice.talk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fiveam.searchservice.exception.bussiness.BusinessLogicException;
import com.fiveam.searchservice.exception.bussiness.ExceptionCode;
import com.fiveam.searchservice.talk.entity.TalkComment;
import com.fiveam.searchservice.talk.repository.TalkCommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TalkCommentService {

    private final TalkCommentRepository talkCommentRepository;

    public TalkComment createTalkComment(TalkComment talkComment) {

        return talkCommentRepository.save(talkComment);
    }

    public TalkComment updateTalkComment(TalkComment talkComment) {
        TalkComment findTalkComment = findTalkComment(talkComment.getTalkCommentId());
        findTalkComment.setContent(talkComment.getContent());

        return talkCommentRepository.save(findTalkComment);
    }

    public TalkComment findTalkComment(long talkCommentId) {
        TalkComment talkComment = findVerifiedTalkComment(talkCommentId);

        return talkComment;
    }

    public List<TalkComment> findTalkComments(Long userId) {
        List<TalkComment> commentsByUser = talkCommentRepository.findAllByUserId(userId);

        return commentsByUser;
    }

    public void deleteTalk(long talkCommentId, long userId) {
        TalkComment talkComment = findVerifiedTalkComment(talkCommentId);
        long writerId = findTalkCommentWriter(talkCommentId);

        if(userId != writerId) {
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED_USER);
        }

        talkCommentRepository.delete(talkComment);
    }

    public long findTalkCommentWriter(long talkCommentId) {
        TalkComment talkComment = findVerifiedTalkComment(talkCommentId);

        return talkComment.getUserId();
    }

    public TalkComment findVerifiedTalkComment(long talkCommnetId) {
        Optional<TalkComment> optionalTalkComment = talkCommentRepository.findById(talkCommnetId);
        TalkComment findTalkComment = optionalTalkComment.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.TALK_NOT_FOUND));

        return findTalkComment;
    }
}
