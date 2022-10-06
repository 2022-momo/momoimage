package com.woowacourse.momoimage.service;

import static org.springframework.http.MediaType.IMAGE_GIF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.woowacourse.momoimage.exception.ImageException;

@Service
public class ImageService {

    private static final String PATH_PREFIX = "./image-save/";
    private static final List<String> IMAGE_CONTENT_TYPES = List.of(IMAGE_GIF_VALUE, IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE);
    private static final int NOT_FOUND_EXTENSION = -1;

    public String save(ImageDto imageDto) {
        MultipartFile multipartFile = imageDto.getFile();
        validateContentType(multipartFile);
        String targetPath = PATH_PREFIX + imageDto.getPath();
        String extension = extractExtension(multipartFile.getOriginalFilename());
        String changedFileName = UUID.randomUUID().toString() + "." + extension;

        File savedFile = new File(targetPath + changedFileName);
        File directory = new File(PATH_PREFIX);

        fileInit(savedFile, directory);

        try (OutputStream outputStream = new FileOutputStream(savedFile)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new ImageException("파일 입출력 에러입니다.");
        }

        return savedFile.getName();
    }

    private String extractExtension(String originalFilename) {
        int index = originalFilename.lastIndexOf(".");

        if (index == NOT_FOUND_EXTENSION) {
            throw new ImageException("잘못된 확장자 입력입니다.");
        }

        return originalFilename.substring(index + 1);
    }

    private void validateContentType(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null || isContentTypeNotImage(contentType)) {
            throw new ImageException("올바른 컨텐츠 타입이 아닙니다.");
        }
    }

    private boolean isContentTypeNotImage(String contentType) {
        return IMAGE_CONTENT_TYPES.stream()
                .noneMatch(contentType::equals);
    }

    private void fileInit(File temporary, File directory) {
        try {
            if (!directory.exists()) {
                directory.mkdirs();
            }
            temporary.createNewFile();
        } catch (IOException e) {
            throw new ImageException("파일/폴더 생성 에러입니다.");
        }
    }
}
