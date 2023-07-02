package com.example.demo1.service;

import com.example.demo1.dto.posting.CategorySaveDTO;
import com.example.demo1.entity.Category;
import com.example.demo1.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void save(CategorySaveDTO categorySaveDTO) {

        // 같은 이름일 경우 에러
        if (categoryRepository.findByName(categorySaveDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 입니다.");
        }

        Category newCategory = categorySaveDTO.toEntity();
        categoryRepository.save(newCategory);
    }

    public List<Category> list() {
        return categoryRepository.findAll();
    }

    @Transactional
    public void delete(Long categoryId) {

        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : categoryId를 찾을 수 없습니다.");
                });

        categoryRepository.deleteById(categoryId);
    }
}
