package com.berry.post.application.service.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

  private final AmazonS3Client s3Client;

  @Value("${s3.bucket}")
  private String bucket;

  public String upload(MultipartFile image) throws IOException {
    // 업로드 파일 이름 변경
    String originalFileName = image.getOriginalFilename();
    String fileName = changeFileName(originalFileName);

    // 업로드 시 메타 데이터 생성
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(image.getContentType());
    metadata.setContentLength(image.getSize());

    // 파일 업로드
    s3Client.putObject(bucket, fileName, image.getInputStream(), metadata);

    // 업로드한 파일의 s3 url 주소 반환
    return s3Client.getUrl(bucket, fileName).toString();
  }

  private String changeFileName(String originalFileName) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    return originalFileName + "_" + LocalDateTime.now().format(formatter);
  }

}
