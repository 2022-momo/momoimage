package com.woowacourse.momoimage.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.woowacourse.momoimage.service.ImageDto;
import com.woowacourse.momoimage.service.ImageService;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/api/images")
    public ResponseEntity<Void> imageUpload(@RequestBody ImageDto imageDto) {
        String fileName = imageService.save(imageDto);

        return ResponseEntity.created(URI.create("/api/images/" + fileName)).build();
    }
}
