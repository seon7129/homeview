package com.example.demo1.controller;

import com.example.demo1.dto.reply.ReplyResponseDTO;
import com.example.demo1.dto.reply.ReplySaveDTO;
import com.example.demo1.dto.reply.ReplyUpdateDTO;
import com.example.demo1.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/comment")
@AllArgsConstructor
@ResponseBody
public class ReplyController {

    private ReplyService replyService;
    @GetMapping("/list/{postId}")
    public List<ReplyResponseDTO> index(@PathVariable Long postId) {
        return replyService.list(postId);
    }

    // 새로운 작성 폼 열기
    @GetMapping("/add")
    public ResponseEntity saveForm() {
        return new ResponseEntity(HttpStatus.OK);
    }


    // 댓글 저장
    @PostMapping("/add")
    public ResponseEntity save(@Valid @RequestBody ReplySaveDTO replySaveDTO, HttpSession session, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        replyService.save(replySaveDTO, session);
        return new ResponseEntity(HttpStatus.CREATED);
    }


    // 수정 폼 열기
    @GetMapping("/{commentId}/edit")
    public ResponseEntity editForm(@PathVariable Long commentId, HttpSession session) {

        if (!replyService.checkIdentification(commentId, session)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        ReplyResponseDTO reply = replyService.content(commentId);
        return new ResponseEntity(reply, HttpStatus.OK);

    }

    // 댓글 수정 완료
    @PostMapping("/{commentId}/edit")
    public ResponseEntity edit(@PathVariable Long commentId, @Valid @RequestBody ReplyUpdateDTO reply, HttpSession session, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        if (!replyService.checkIdentification(commentId, session)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        replyService.update(commentId, reply);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 댓글 삭제
    @GetMapping("/{commentId}/delete")
    public ResponseEntity deleteById(@PathVariable Long commentId, HttpSession session) {

        if (!replyService.checkIdentification(commentId, session)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        replyService.delete(commentId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
