package com.example.demo1.controller;

import com.example.demo1.dto.posting.CategorySaveDTO;
import com.example.demo1.entity.Category;
import com.example.demo1.service.CategoryService;
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
@RequestMapping("/api/category")
@AllArgsConstructor
@ResponseBody
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping("/list")
    public List<Category> index() {
        return categoryService.list();
    }

    // 새로운 작성 폼 열기
    @GetMapping("/add")
    public ResponseEntity saveForm() {
        return new ResponseEntity(HttpStatus.OK);
    }


    // 카테고리 저장
    @PostMapping("/add")
    public ResponseEntity save(@Valid @RequestBody CategorySaveDTO categorySaveDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        categoryService.save(categorySaveDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 카테고리 삭제
    @GetMapping("/{categoryId}/delete")
    public ResponseEntity deleteById(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}