package com.woowacourse.momoimage.service;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import java.util.List;

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
}
