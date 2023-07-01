package com.example.demo1.controller;

import com.example.demo1.dto.posting.PostingContentResponseDTO;
import com.example.demo1.dto.posting.PostingSaveDTO;
import com.example.demo1.dto.posting.PostingResponseDTO;
import com.example.demo1.dto.posting.PostingUpdateDTO;
import com.example.demo1.service.PostingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class PostingController { // 스테이터스로만 보내는걸로. 문자든 숫자든
    private PostingService postingService;


    @GetMapping("/list")
    public List<PostingResponseDTO> index() {
        return postingService.list();
    }

    // 새로운 작성 폼 열기
    @GetMapping("/add")
    public ResponseEntity saveForm() {
        return new ResponseEntity(HttpStatus.OK);
    }


    // 포스팅 저장
    @PostMapping("/add")
    public ResponseEntity save(@Valid @RequestBody PostingSaveDTO postingSaveDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        postingService.save(postingSaveDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 작성된 포스팅 열기
    @GetMapping("/{postId}")
    public ResponseEntity findById(@PathVariable Long postId) {
        PostingContentResponseDTO posting = postingService.content(postId);
        return new ResponseEntity(posting, HttpStatus.OK);
    }

    // 수정 폼 열기
    @GetMapping("/{postId}/edit")
    public ResponseEntity editForm(@PathVariable Long postId) {
        PostingContentResponseDTO posting = postingService.content(postId);
        return new ResponseEntity(posting, HttpStatus.OK);

    }

    // 포스팅 수정 완료
    @PostMapping("/{postId}/edit")
    public ResponseEntity edit(@PathVariable Long postId, @Valid @RequestBody PostingUpdateDTO posting, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        postingService.update(postId, posting);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 포스팅 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity deleteById(@PathVariable Long postId) {
        postingService.delete(postId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}