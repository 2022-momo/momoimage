package com.woowacourse.momoimage.controller;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import com.woowacourse.momoimage.service.ImageService;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/api/images")
    public ResponseEntity<Void> imageUpload(@RequestParam String path, @RequestParam("imageFile") MultipartFile imageFile) {
        String fileName = imageService.save(imageFile);

        return ResponseEntity.created(URI.create("/api/images/" + fileName)).build();
    }
}
