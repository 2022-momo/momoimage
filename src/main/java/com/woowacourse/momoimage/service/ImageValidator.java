package com.woowacourse.momoimage.service;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.woowacourse.momoimage.exception.ImageException;

@Component
public class ImageValidator {

    private static final List<String> IMAGE_CONTENT_TYPES =
            List.of(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE);

    public void validateContentType(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null || isContentTypeNotImage(contentType)) {
            throw new ImageException(String.format("올바른 컨텐츠 타입이 아닙니다. [%s]", contentType));
        }
    }

    private boolean isContentTypeNotImage(String contentType) {
        return IMAGE_CONTENT_TYPES.stream()
                .noneMatch(contentType::equals);
    }

    public void validateFileIsImage(MultipartFile multipartFile) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(multipartFile.getBytes());
            BufferedImage read = ImageIO.read(byteArrayInputStream);
            if (read == null) {
                throw new ImageException("올바른 이미지 파일이 아닙니다!!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
