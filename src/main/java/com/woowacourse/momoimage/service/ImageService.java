package com.woowacourse.momoimage.service;

import static org.springframework.http.MediaType.IMAGE_GIF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import com.woowacourse.momoimage.exception.ImageException;
import com.woowacourse.momoimage.service.dto.ImageDto;

@Service
@RequiredArgsConstructor
public class ImageService {

    private static final String PATH_PREFIX = "./image-save";
    private static final String IMAGE_DOMAIN = "http://image.moyeora.site";
    private static final int NOT_FOUND_EXTENSION = -1;

    private final ImageValidator imageValidator;

    public String save(ImageDto imageDto) {
        // 이미지서버 파일 저장 규칙 : path의 앞에는 /가 나오며, 뒤에는 /가 붙지 않는다.

        MultipartFile multipartFile = imageDto.getFile();
        String path = Optional.of(imageDto.getPath())
                .orElse("");
        imageValidator.validateContentType(multipartFile);
        imageValidator.validateFileIsImage(multipartFile);
        String targetPath = PATH_PREFIX + imageDto.getPath();
        String extension = extractExtension(multipartFile.getOriginalFilename());
        String changedFileName = UUID.randomUUID().toString() + "." + extension;

        File savedFile = new File(targetPath + "/" + changedFileName);

        createDirectories(targetPath);
        saveFile(savedFile);
        try (OutputStream outputStream = new FileOutputStream(savedFile)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new ImageException("파일 입출력 에러입니다.");
        }

        return IMAGE_DOMAIN + path + "/" + savedFile.getName();
    }

    private String extractExtension(String originalFilename) {
        int index = originalFilename.lastIndexOf(".");

        if (index == NOT_FOUND_EXTENSION) {
            throw new ImageException("잘못된 확장자 입력입니다.");
        }

        return originalFilename.substring(index + 1);
    }


    private void createDirectories(String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new ImageException("이미지 디렉토리 생성 오류입니다.");
        }
    }

    private void saveFile(File savedFile) {
        try {
            if (!savedFile.createNewFile()) {
                throw new ImageException("이미지 파일 생성 에러입니다.");
            }
        } catch (IOException e) {
            throw new ImageException(String.format("이미지 파일 생성 에러입니다. [%s]", e.getMessage()));
        }
    }
}
