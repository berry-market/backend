package com.berry.user.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class S3UploadServiceTest {

    @Autowired
    private S3UploadService s3UploadService;

    @Test
    public void testConvertToWebpWithResize() throws Exception {
        // Given
        File testFile = new ClassPathResource("DSCF1381.JPG").getFile();
        MockMultipartFile multipartFile = new MockMultipartFile(
            "file",
            "DSCF1381.JPG",
            "image/jpeg",
            new FileInputStream(testFile)
        );
        String filePath = changeFileName(multipartFile.getOriginalFilename());

        // When
        File convertedFile = s3UploadService.convertToWebp(multipartFile, filePath);

        // Then
        double originalFileSizeKB = testFile.length() / 1024.0;
        double convertedFileSizeKB = convertedFile.length() / 1024.0;

        double compressionRate = 100 - (convertedFileSizeKB / originalFileSizeKB) * 100; // 압축률 계산

        System.out.printf("Original File Size: %.2f KB%n", originalFileSizeKB);
        System.out.printf("Converted File Size: %.2f KB%n", convertedFileSizeKB);
        System.out.printf("Compression Rate: %.2f%%%n", compressionRate);

        assertTrue(compressionRate > 0, "압축률이 0% 이상이어야 합니다.");
        Files.deleteIfExists(convertedFile.toPath());
    }

    private String changeFileName(String originalFileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return originalFileName + "_" + LocalDateTime.now().format(formatter);
    }
}