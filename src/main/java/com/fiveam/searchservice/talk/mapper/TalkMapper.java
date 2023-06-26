package com.fiveam.searchservice.talk.mapper;

import com.fiveam.searchservice.client.OrderServiceClient;
import com.fiveam.searchservice.client.UserServiceClient;
import com.fiveam.searchservice.response.UserInfoResponseDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.fiveam.searchservice.exception.bussiness.BusinessLogicException;
import com.fiveam.searchservice.exception.bussiness.ExceptionCode;
import com.fiveam.searchservice.item.mapper.ItemMapper;
import com.fiveam.searchservice.item.service.ItemService;
import com.fiveam.searchservice.talk.dto.*;
import com.fiveam.searchservice.talk.entity.Talk;
import com.fiveam.searchservice.talk.entity.TalkComment;
import com.fiveam.searchservice.talk.service.TalkCommentService;
import com.fiveam.searchservice.talk.service.TalkService;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TalkMapper {

    default Talk talkDtoToTalk(long itemId, UserServiceClient userService, OrderServiceClient orderService,
                                     ItemService itemService, TalkDto talkDto) { // 토크 등록

        UserInfoResponseDto user = userService.getLoginUser();

        Talk talk = new Talk();
        talk.setItem(itemService.findVerifiedItem(itemId));
        talk.setUserId(user.getId());
        talk.setContent(talkDto.getContent());
        talk.setShopper(orderService.isShopper(itemId, talk.getUserId()));

        return talk;
    }

    default Talk talkDtoToTalk(long talkId, UserServiceClient userService,
                               TalkService talkService, TalkDto talkDto) { // 수정

        UserInfoResponseDto user = userService.getLoginUser();

        if(user.getId() != talkService.findTalkWriter(talkId)) {
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED_USER);
        } // 토크 작성자만 토크를 수정할 수 있음

        Talk talk = new Talk();
        talk.setTalkId(talkId);
        talk.setUserId(user.getId());
        talk.setContent(talkDto.getContent());

        return talk;
    }

    default TalkResponseDto talkToTalkResponseDto(Talk talk, UserServiceClient userService) {
        UserInfoResponseDto user = userService.getLoginUser();

        TalkResponseDto talkResponseDto = new TalkResponseDto();
        talkResponseDto.setTalkId(talk.getTalkId());
        talkResponseDto.setItemId(talk.getItem().getItemId());
        talkResponseDto.setUserId(user.getId());
        talkResponseDto.setDisplayName(user.getDisplayName());
        talkResponseDto.setContent(talk.getContent());
        talkResponseDto.setShopper(talk.isShopper());
        talkResponseDto.setCreatedAt(talk.getCreatedAt().toLocalDate().toString());
        talkResponseDto.setUpdatedAt(talk.getUpdatedAt().toLocalDate().toString());

        return talkResponseDto;
    }

    default List<TalkResponseDto> talksToTalkResponseDtos(List<Talk> talks, UserServiceClient userService) {

        if(talks == null) return null;

        List<TalkResponseDto> talkResponseDtos = new ArrayList<>();

        for(Talk talk : talks) {
            talkResponseDtos.add(talkToTalkResponseDto(talk, userService));
        }

        return talkResponseDtos;
    }

    default TalkAndCommentDto talkToTalkAndCommentDto(Talk talk, UserServiceClient userService) {
        UserInfoResponseDto user = userService.getLoginUser();

        TalkAndCommentDto talkAndCommentDto = new TalkAndCommentDto();
        talkAndCommentDto.setTalkId(talk.getTalkId());
        talkAndCommentDto.setUserId(user.getId());
        talkAndCommentDto.setDisplayName(user.getDisplayName());
        talkAndCommentDto.setItemId(talk.getItem().getItemId());
        talkAndCommentDto.setContent(talk.getContent());
        talkAndCommentDto.setShopper(talk.isShopper());
        talkAndCommentDto.setCreatedAt(talk.getCreatedAt().toLocalDate().toString());
        talkAndCommentDto.setUpdatedAt(talk.getUpdatedAt().toLocalDate().toString());

        List<TalkCommentDto> talkCommentDtos = talkCommentsToTalkCommentDtos(talk.getTalkComments(), userService);
        talkAndCommentDto.setTalkComments(talkCommentDtos);

        return talkAndCommentDto;
    }

    default List<TalkAndCommentDto> talksToTalkAndCommentDtos(List<Talk> talks, UserServiceClient userService) {

        if(talks == null) return null;

        List<TalkAndCommentDto> talkAndCommentDtos = new ArrayList<>();

        for(Talk talk : talks) {
            talkAndCommentDtos.add(talkToTalkAndCommentDto(talk, userService));
        }
        return talkAndCommentDtos;
    }

    default TalkDetailResponseDto talkToTalkDetailResponseDto(Talk talk, ItemMapper itemMapper, UserServiceClient userService) {

        TalkDetailResponseDto talkResponseDto = new TalkDetailResponseDto();
        UserInfoResponseDto user = userService.getLoginUser();

        talkResponseDto.setTalkId(talk.getTalkId());
        talkResponseDto.setUserId(user.getId());
        talkResponseDto.setItem(itemMapper.itemToItemSimpleResponseDto(talk.getItem()));
        talkResponseDto.setContent(talk.getContent());
        talkResponseDto.setShopper(talk.isShopper());
        talkResponseDto.setCreatedAt(talk.getCreatedAt().toLocalDate().toString());
        talkResponseDto.setUpdatedAt(talk.getUpdatedAt().toLocalDate().toString());

        return talkResponseDto;
    }

    default List<TalkDetailResponseDto> talksToTalkDetailResponseDtos(List<Talk> talks, ItemMapper itemMapper, UserServiceClient userService) {

        if(talks == null) return null;

        List<TalkDetailResponseDto> talkResponseDtos = new ArrayList<>();

        for(Talk talk : talks) {
            talkResponseDtos.add(talkToTalkDetailResponseDto(talk, itemMapper, userService));
        }

        return talkResponseDtos;
    }

    default TalkComment talkDtoToTalkComment(long itemId, long talkId, UserServiceClient userService, OrderServiceClient orderService,
                                             TalkService talkService, ItemService itemService, TalkDto talkDto) { // 토크 코멘트 등록

        TalkComment comment = new TalkComment();
        UserInfoResponseDto user = userService.getLoginUser();

        comment.setTalk(talkService.findVerifiedTalk(talkId));
        comment.setItem(itemService.findVerifiedItem(itemId));
        comment.setUserId(user.getId());
        comment.setContent(talkDto.getContent());
        comment.setShopper(orderService.isShopper(itemId, comment.getUserId()));

        return comment;
    }

    default TalkComment talkDtoToTalkComment(long talkCommentId, UserServiceClient userService,
                                             TalkCommentService commentService, TalkDto talkDto) { // 토크 코멘트 수정

        UserInfoResponseDto user = userService.getLoginUser();

        if(user.getId() != commentService.findTalkCommentWriter(talkCommentId)) {
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED_USER);
        } // 코멘트 작성자만 수정 가능

        TalkComment talkComment = new TalkComment();
        talkComment.setTalkCommentId(talkCommentId);
        talkComment.setUserId(user.getId());
        talkComment.setContent(talkDto.getContent());

        return talkComment;
    }

    default TalkCommentDto talkCommentToTalkCommentDto(TalkComment talkComment, UserServiceClient userService) { // 상세페이지
        TalkCommentDto commentDto = new TalkCommentDto();
        UserInfoResponseDto user = userService.getLoginUser();

        commentDto.setTalkCommentId(talkComment.getTalkCommentId());
        commentDto.setUserId(user.getId());
        commentDto.setDisplayName(user.getDisplayName());
        commentDto.setContent(talkComment.getContent());
        commentDto.setShopper(talkComment.isShopper());
        commentDto.setCreatedAt(talkComment.getCreatedAt().toLocalDate().toString());
        commentDto.setUpdatedAt(talkComment.getUpdatedAt().toLocalDate().toString());

        return commentDto;
    }

    default List<TalkCommentDto> talkCommentsToTalkCommentDtos(List<TalkComment> talkComments, UserServiceClient userService) {

        if(talkComments == null) return null;

        List<TalkCommentDto> talkCommentDtos = new ArrayList<>();

        for(TalkComment talkComment : talkComments) {
            talkCommentDtos.add(talkCommentToTalkCommentDto(talkComment, userService));
        }

        return talkCommentDtos;
    }

    default TalkOrCommentDto toTalkOrCommentDto(Talk talk, ItemMapper itemMapper) { // 마이페이지 - 토크 조회
        TalkOrCommentDto talkOrCommentDto = new TalkOrCommentDto();
        talkOrCommentDto.setTalkId(talk.getTalkId());
        talkOrCommentDto.setItem(itemMapper.itemToItemSimpleResponseDto(talk.getItem()));
        talkOrCommentDto.setContent(talk.getContent());
        talkOrCommentDto.setReply(false);
        talkOrCommentDto.setCreatedAt(talk.getCreatedAt().toLocalDate().toString());
        talkOrCommentDto.setUpdatedAt(talk.getUpdatedAt().toLocalDate().toString());

        return talkOrCommentDto;
    }

    default TalkOrCommentDto toTalkOrCommentDto(TalkComment talkComment, ItemMapper itemMapper) { // 마이페이지 - 토크 조회
        TalkOrCommentDto talkOrCommentDto = new TalkOrCommentDto();
        talkOrCommentDto.setTalkCommentId(talkComment.getTalkCommentId());
        talkOrCommentDto.setItem(itemMapper.itemToItemSimpleResponseDto(talkComment.getItem()));
        talkOrCommentDto.setContent(talkComment.getContent());
        talkOrCommentDto.setReply(true);
        talkOrCommentDto.setCreatedAt(talkComment.getCreatedAt().toLocalDate().toString());
        talkOrCommentDto.setUpdatedAt(talkComment.getUpdatedAt().toLocalDate().toString());

        return talkOrCommentDto;
    }

    default List<TalkOrCommentDto> toTalkOrCommentDtos(List<Talk> talks,
                                                       List<TalkComment> talkComments, ItemMapper itemMapper) {

        if(talks == null && talkComments == null) return null;

        List<TalkOrCommentDto> talkOrCommentDtos = new ArrayList<>();

        for(Talk talk : talks) {
            talkOrCommentDtos.add(toTalkOrCommentDto(talk, itemMapper));
        }

        for(TalkComment talkComment : talkComments) {
            talkOrCommentDtos.add(toTalkOrCommentDto(talkComment, itemMapper));
        }

        return talkOrCommentDtos;
    }

    default Page<TalkOrCommentDto> toPageDtos(Pageable pageable, List<TalkOrCommentDto> talkOrCommentDtos) {
        return new PageImpl<>(talkOrCommentDtos);
    }

}
