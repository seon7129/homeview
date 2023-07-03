package com.example.demo1.service;

import com.example.demo1.dto.reply.ReplyResponseDTO;
import com.example.demo1.dto.reply.ReplySaveDTO;
import com.example.demo1.dto.reply.ReplyUpdateDTO;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Posting;
import com.example.demo1.entity.Reply;
import com.example.demo1.repository.MemberRepository;
import com.example.demo1.repository.PostingRepository;
import com.example.demo1.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Reply save(ReplySaveDTO replySaveDTO) {

        Member newMember = memberRepository.findById(replySaveDTO.getMemberId())
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : memberId를 찾을 수 없습니다.");
                });

        Posting newPosting = postingRepository.findById(replySaveDTO.getPostId())
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : postId를 찾을 수 없습니다.");
                });

        /*Reply newReply = new Reply(
                newPosting,
                newMember,
                replySaveDTO.getContent(),
                replySaveDTO.getCommentTime());*/

        Reply newReply = replySaveDTO.toEntity(newPosting, newMember);

        return replyRepository.save(newReply);
    }

    @Transactional
    public void update(Long commentId, ReplyUpdateDTO updateParam) {
        Reply reply = replyRepository.findById(commentId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : postId를 찾을 수 없습니다.");
                });
        reply.setContent(updateParam.getContent());
        replyRepository.save(reply);
    }

    // 댓글 목록
    public List<ReplyResponseDTO> list(Long postId) {
        List<Reply> replies = replyRepository.findByPostId(postId);
        List<ReplyResponseDTO> replyResponseList = new ArrayList<>();
        for (Reply reply : replies) {
            ReplyResponseDTO replyResponseDTO = ReplyResponseDTO.builder()
                    .commentId(reply.getCommentId())
                    .postId(reply.getPosting().getPostId())
                    .memberId(reply.getMember().getId())
                    .memberNickName(reply.getMember().getNickname())
                    .content(reply.getContent())
                    .commentTime(reply.getCommentTime())
                    .build();

            replyResponseList.add(replyResponseDTO);
        }
        return replyResponseList;
    }




    // 댓글 상세보기 -> 수정할 때 사용
    public ReplyResponseDTO content(Long commentId) {

        Optional<Reply> reply = replyRepository.findById(commentId);
        Optional<ReplyResponseDTO> replyResponse = Optional.ofNullable(ReplyResponseDTO.builder()
                .commentId(reply.get().getCommentId())
                .postId(reply.get().getPosting().getPostId())
                .memberId(reply.get().getMember().getId())
                .memberNickName(reply.get().getMember().getNickname())
                .content(reply.get().getContent())
                .commentTime(reply.get().getCommentTime())
                .build());

        return replyResponse
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 상세보기 실패 : postId를 찾을 수 없습니다.");
                });
    }


    @Transactional
    public void delete(Long commentId) {
        replyRepository.deleteById(commentId);
    }
}