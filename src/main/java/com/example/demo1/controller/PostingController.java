package com.example.demo1.controller;

import com.example.demo1.dto.posting.*;
import com.example.demo1.service.LikeService;
import com.example.demo1.service.PostingService;
import com.example.demo1.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/posting")
@AllArgsConstructor
@ResponseBody
public class PostingController { // 스테이터스로만 보내는걸로

    private PostingService postingService;
    private LikeService likeService;
    private ReplyService replyService;

    @GetMapping("/list/{categoryId}")
    public Page<PostingResponseDTO> index(@PathVariable Long categoryId, @PageableDefault(sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {
        return postingService.list(categoryId, pageable);
    }

    // 새로운 작성 폼 열기
    @GetMapping("/add")
    public ResponseEntity saveForm() {
        return new ResponseEntity(HttpStatus.OK);
    }


    // 포스팅 저장
    @PostMapping("/add")
    public ResponseEntity save(@Valid @RequestBody PostingSaveDTO postingSaveDTO, HttpSession session, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        postingService.save(postingSaveDTO, session);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 작성된 포스팅 열기
    @GetMapping("/{postId}")
    public ResponseEntity findById(@PathVariable Long postId) {
        PostingContentResponseDTO posting = postingService.content(postId);
        postingService.updatePostHits(postId);
        return new ResponseEntity(posting, HttpStatus.OK);
    }

    // 수정 폼 열기
    @GetMapping("/{postId}/edit")
    public ResponseEntity editForm(@PathVariable Long postId, HttpSession session) {
        if (!postingService.checkIdentification(postId, session)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        PostingContentResponseDTO posting = postingService.content(postId);
        return new ResponseEntity(posting, HttpStatus.OK);

    }

    // 포스팅 수정 완료
    @PostMapping("/{postId}/edit")
    public ResponseEntity edit(@PathVariable Long postId, @Valid @RequestBody PostingUpdateDTO posting, HttpSession session, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        if (!postingService.checkIdentification(postId, session)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        postingService.update(postId, posting);
        return new ResponseEntity(HttpStatus.OK);
    }


    // 좋아요 클릭 -> 저장
    @PostMapping("/like/save")
    public ResponseEntity saveLike(@Valid @RequestBody LikeSaveDTO likeSaveDTO, HttpSession session, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        boolean save = likeService.save(likeSaveDTO, session);
        if (save == true) {
            return new ResponseEntity(HttpStatus.CREATED); // 201 저장이 잘 됨
        }
        return new ResponseEntity(HttpStatus.ACCEPTED); // 202 이미 눌려서 저장 안됨
    }

    @PostMapping("/like/check")
    public ResponseEntity checkLike(@RequestBody LikeSaveDTO likeSaveDTO, HttpSession session) {  // 갯수 가져오기
        boolean alreadyChecked = likeService.isAlreadyChecked(session, likeSaveDTO.getPostId());
        int countLikes = likeService.countLikes(likeSaveDTO.getPostId());
        if (alreadyChecked == false) {
            return new ResponseEntity(countLikes, HttpStatus.CREATED); // 201 안눌렸으
        }
        return new ResponseEntity(countLikes, HttpStatus.ACCEPTED); // 202 눌렸으
    }


    // 좋아요 삭제
    @PostMapping("/like/delete")  // 프론트에서 likeid 를 찾지 못함
    public ResponseEntity deleteLike(@RequestBody LikeSaveDTO likeSaveDTO, HttpSession session) {
        likeService.delete(session, likeSaveDTO.getPostId());
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    // 포스팅 삭제
    @GetMapping("/{postId}/delete")
    public ResponseEntity deleteById(@PathVariable Long postId, HttpSession session) {

        if (!postingService.checkIdentification(postId, session)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        likeService.deleteLikesinPosting(postId);
        replyService.deleteRepliesinPosting(postId);
        postingService.delete(postId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping("/search/{categoryId}")
    public ResponseEntity search(String keyword, @PathVariable Long categoryId, @PageableDefault(sort = "postId", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostingResponseDTO> searchList = postingService.search(keyword, categoryId, pageable);
        if (searchList == null) {
            return new ResponseEntity(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        log.info("keyword = {}", keyword);
        return new ResponseEntity(searchList, HttpStatus.OK);
    }


}