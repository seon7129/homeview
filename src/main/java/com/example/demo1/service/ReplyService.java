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
import jakarta.servlet.http.HttpSession;
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
    public Reply save(ReplySaveDTO replySaveDTO, HttpSession session) {

        Member newMember = getInfo(session);
        Posting newPosting = makeNewPosting(replySaveDTO.getPostId());

        Reply newReply = replySaveDTO.toEntity(newPosting, newMember);
        return replyRepository.save(newReply);
    }

    @Transactional
    public void update(Long commentId, ReplyUpdateDTO updateParam) {
        Reply reply = makeNewReply(commentId);
        reply.setContent(updateParam.getContent());
        replyRepository.save(reply);
    }

    // 어드민에서 모든 댓글 리스트 불러오기
    public List<ReplyResponseDTO> allRepliesinAdmin() {
        List<Reply> replies = replyRepository.findAll();
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

    // 댓글 목록
    public List<ReplyResponseDTO> list(Long postId) {

        List<Reply> replies = replyRepository.findByPostId(postId); // 여기서 필터링이 제대로 안되는 것 같다
        List<ReplyResponseDTO> replyResponseList = new ArrayList<>();
        for (Reply reply : replies) {
            log.info(String.valueOf(reply.getPosting().getPostId()));
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

    public List<Reply> listofPosting(Long postId) {
        List<Reply> list = replyRepository.findByPostId(postId);
        return list;
    }

    @Transactional
    public void deleteRepliesinPosting(Long postId) {
        List<Reply> list = listofPosting(postId);
        for (Reply replies : list) {
            replyRepository.deleteById(replies.getCommentId());
        }
    }


    @Transactional
    public void delete(Long commentId) {
        replyRepository.deleteById(commentId);
    }


    public boolean checkIdentification(Long commentId, HttpSession session) {
        Reply reply = makeNewReply(commentId);
        Member member = getInfo(session);

        if (reply.getMember().getId() == member.getId()) {
            return true;
        }
        return false;
    }


    private Member getInfo(HttpSession session){
        String email = (String) session.getAttribute("email");
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.get();
    }

    private Posting makeNewPosting(Long postId) {
        Posting newPosting = postingRepository.findById(postId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : postId를 찾을 수 없습니다.");
                });
        return newPosting;
    }

    private Reply makeNewReply(Long commentId) {
        Reply reply = replyRepository.findById(commentId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : replyId를 찾을 수 없습니다.");
                });
        return reply;
    }
}