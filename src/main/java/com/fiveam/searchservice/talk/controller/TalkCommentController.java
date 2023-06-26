package com.fiveam.searchservice.talk.controller;

import com.fiveam.searchservice.client.OrderServiceClient;
import com.fiveam.searchservice.client.UserServiceClient;
import com.fiveam.searchservice.item.service.ItemService;
import com.fiveam.searchservice.response.SingleResponseDto;
import com.fiveam.searchservice.response.UserInfoResponseDto;
import com.fiveam.searchservice.talk.dto.TalkDto;
import com.fiveam.searchservice.talk.entity.TalkComment;
import com.fiveam.searchservice.talk.mapper.TalkMapper;
import com.fiveam.searchservice.talk.service.TalkCommentService;
import com.fiveam.searchservice.talk.service.TalkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping("/talks/comments")
@RequiredArgsConstructor
public class TalkCommentController {

    private final TalkCommentService commentService;
    private final TalkMapper talkMapper;
    private final TalkService talkService;
    private final UserServiceClient userService;
    private final OrderServiceClient orderService;
    private final ItemService itemService;

    @PostMapping("/{talk-id}") // 토크 코멘트 등록
    public ResponseEntity postTalkComment(@PathVariable("talk-id") @Positive long talkId,
                                          @RequestParam(value="itemId") long itemId,
                                          @RequestBody TalkDto talkDto) {

        TalkComment talkComment = commentService.createTalkComment(talkMapper.talkDtoToTalkComment(
                itemId, talkId, userService, orderService, talkService, itemService, talkDto));

        return new ResponseEntity<>(new SingleResponseDto<>(
                talkMapper.talkCommentToTalkCommentDto(talkComment, userService)), HttpStatus.CREATED);
    }

    @PatchMapping("/{talkComment-id}") // 토크 코멘트 수정 - 상세페이지, 작성글 목록
    public ResponseEntity updateTalkComment(@PathVariable("talkComment-id") @Positive long talkCommentId,
                                            @RequestBody TalkDto talkDto) {

        TalkComment talkComment = talkMapper.talkDtoToTalkComment(talkCommentId, userService, commentService, talkDto);

        TalkComment updatedComment = commentService.updateTalkComment(talkComment);

        return new ResponseEntity<>(new SingleResponseDto<>(
                talkMapper.talkCommentToTalkCommentDto(updatedComment, userService)), HttpStatus.OK);
    }

    @GetMapping("/{talkComment-id}")
    public ResponseEntity getTalkComment(@PathVariable("talkComment-id") @Positive long talkCommentId) {

        TalkComment talkComment = commentService.findTalkComment(talkCommentId);

        return new ResponseEntity<>(new SingleResponseDto<>(
                talkMapper.talkCommentToTalkCommentDto(talkComment, userService)), HttpStatus.OK);
    }

    @DeleteMapping("/{talkComment-id}") // 토크 코멘트 삭제 - 상세페이지, 작성글 목록
    public ResponseEntity deleteTalk(@PathVariable("talkComment-id") @Positive long talkCommentId) {

        UserInfoResponseDto user = userService.getLoginUser();
        commentService.deleteTalk(talkCommentId, user.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
