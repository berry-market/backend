package com.berry.user.application.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.berry.common.exceptionhandler.CustomApiException;
import com.berry.common.response.ResErrorCode;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
        File webpFile = convertToWebp(image, changeFileName(image.getOriginalFilename()));
        ObjectMetadata metadata = createMetadataFromFile(webpFile);

        try (FileInputStream fileInputStream = new FileInputStream(webpFile)) {
            s3Client.putObject(bucket, webpFile.getName(), fileInputStream, metadata);
        } catch (Exception e) {
            throw new CustomApiException(ResErrorCode.INTERNAL_SERVER_ERROR, "S3 업로드 중 오류가 발생하였습니다.");
        }

        return s3Client.getUrl(bucket, webpFile.getName()).toString();
    }

    public File convertToWebp(MultipartFile file, String fileName) {
        try {
            File originalFile = convertMultipartToFile(file);
            File webpFile = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName + ".webp");

            ImmutableImage.loader()
                .fromFile(originalFile)
                .output(WebpWriter.DEFAULT, webpFile);

            return webpFile;
        } catch (Exception e) {
            throw new CustomApiException(ResErrorCode.INTERNAL_SERVER_ERROR, "이미지 변환 중 오류가 발생하였습니다.");
        }
    }

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + File.separator + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }

    private String changeFileName(String originalFileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD);
        return originalFileName + "_" + LocalDateTime.now().format(formatter);
    }

    private ObjectMetadata createMetadataFromFile(File file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/webp");
        metadata.setContentLength(file.length());
        return metadata;
    }

}