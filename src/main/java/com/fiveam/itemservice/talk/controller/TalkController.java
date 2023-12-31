package com.fiveam.itemservice.talk.controller;

import com.fiveam.itemservice.client.OrderServiceClient;
import com.fiveam.itemservice.client.UserServiceClient;
import com.fiveam.itemservice.item.mapper.ItemMapper;
import com.fiveam.itemservice.item.service.ItemService;
import com.fiveam.itemservice.response.MultiResponseDto;
import com.fiveam.itemservice.response.SingleResponseDto;
import com.fiveam.itemservice.response.UserInfoResponseDto;
import com.fiveam.itemservice.talk.dto.TalkDto;
import com.fiveam.itemservice.talk.dto.TalkOrCommentDto;
import com.fiveam.itemservice.talk.entity.Talk;
import com.fiveam.itemservice.talk.entity.TalkComment;
import com.fiveam.itemservice.talk.mapper.TalkMapper;
import com.fiveam.itemservice.talk.service.TalkCommentService;
import com.fiveam.itemservice.talk.service.TalkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping("/talks")
@RequiredArgsConstructor
public class TalkController {
    private final TalkService talkService;
    private final TalkMapper talkMapper;
    private final TalkCommentService commentService;

    private final UserServiceClient userService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final OrderServiceClient orderService;

    @PostMapping("/{item-id}") // 주문 상세 내역에서 작성
    public ResponseEntity postTalk(@PathVariable("item-id") @Positive long itemId,
                                   @RequestBody TalkDto talkDto) {

        Talk talk = talkService.createTalk(
                talkMapper.talkDtoToTalk(itemId, userService, orderService, itemService, talkDto));

        return  new ResponseEntity<>(
                new SingleResponseDto<>(talkMapper.talkToTalkResponseDto(talk, userService)), HttpStatus.CREATED);
    }

    @PatchMapping("/{talk-id}")
    public ResponseEntity updateTalk(@PathVariable("talk-id") @Positive long talkId,
                                     @RequestBody TalkDto talkDto) {

        Talk talk = talkMapper.talkDtoToTalk(talkId, userService, talkService, talkDto);

        Talk updatedTalk = talkService.updateTalk(talk);

        return new ResponseEntity<>(new SingleResponseDto<>(
                talkMapper.talkToTalkResponseDto(updatedTalk, userService)), HttpStatus.OK);
    }

    @GetMapping("/{talk-id}")
    public ResponseEntity getTalk(@PathVariable("talk-id") @Positive long talkId) {

        Talk talk = talkService.findTalk(talkId);

        return new ResponseEntity<>(new SingleResponseDto<>(
                talkMapper.talkToTalkResponseDto(talk, userService)), HttpStatus.OK);
    }

    @GetMapping("/mypage") // 유저가 작성한 토크, 토크 코멘트 한번에 ! 토크일 경우 토크에 달린 코멘트는 X
    public ResponseEntity getUserTalk(Pageable pageable) {

        UserInfoResponseDto user = userService.getLoginUser();
        List<Talk> talks = talkService.findTalks(user.getId());
        List<TalkComment> talkComments = commentService.findTalkComments(user.getId());

        List<TalkOrCommentDto> talkOrCommentDtos = talkMapper.toTalkOrCommentDtos(talks, talkComments, itemMapper);
        Page<TalkOrCommentDto> talkOrCommentDtoPage = talkMapper.toPageDtos(pageable, talkOrCommentDtos);

        return new ResponseEntity<>(new MultiResponseDto<>(talkOrCommentDtos, talkOrCommentDtoPage), HttpStatus.OK);
    }

    @DeleteMapping("/{talk-id}")
    public ResponseEntity deleteTalk(@PathVariable("talk-id") @Positive long talkId) {

        UserInfoResponseDto user = userService.getLoginUser();
        talkService.deleteTalk(talkId, user.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
