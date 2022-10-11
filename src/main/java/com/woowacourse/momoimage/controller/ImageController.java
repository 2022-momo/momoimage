package com.woowacourse.momoimage.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.woowacourse.momoimage.service.dto.ImageDto;
import com.woowacourse.momoimage.service.ImageService;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/api/images")
    public ResponseEntity<Void> imageUpload(@ModelAttribute ImageDto imageDto) {
        final var fullPath = imageService.save(imageDto);

        return ResponseEntity.created(URI.create(fullPath)).build();
    }
}
