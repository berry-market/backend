package com.berry.user.application.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3Client s3Client;

    @Value("${s3.bucket}")
    private String bucket;
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMddHHmmss";

    public String imageUpload(MultipartFile image){
        String fileName = changeFileName(image.getOriginalFilename());
        ObjectMetadata metadata = createMetadataFromFile(image);

        try {
            s3Client.putObject(bucket, fileName, image.getInputStream(), metadata);
        } catch (Exception e) {
            throw new CustomApiException(ResErrorCode.INTERNAL_SERVER_ERROR, "S3 업로드 중 오류가 발생하였습니다.");
        }

        return s3Client.getUrl(bucket, fileName).toString();
    }

    private String changeFileName(String originalFileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD);
        return originalFileName + "_" + LocalDateTime.now().format(formatter);
    }

    private ObjectMetadata createMetadataFromFile(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        return metadata;
    }

}