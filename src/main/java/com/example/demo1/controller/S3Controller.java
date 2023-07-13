package com.example.demo1.controller;

import com.example.demo1.service.S3UploadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/s3")
@AllArgsConstructor
@ResponseBody
public class S3Controller {
    private S3UploadService s3UploadService;

    /*파일 업로드 (1)개*/
    @PostMapping("/upload")
    public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
        return s3UploadService.saveFile(multipartFile);
    }

    /*파일 업로드 여러개*/
    @PostMapping("/uploads")
    public List<String> uploadFiles(@RequestParam("images")List<MultipartFile> multipartFile) throws IOException {
        return s3UploadService.uploadFile(multipartFile);
    }

    /*이미지가 삭제됐을 시에 프론트에서 'x'를 나타내는 이미지로 변경해주면 좋을듯?*/
    @DeleteMapping("/delete")
    public void delete(@RequestParam("fileName") String fileName){
        s3UploadService.deleteImage(fileName);
    }

}

