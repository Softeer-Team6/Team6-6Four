package com.softeer.team6four.domain.carbob.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.softeer.team6four.domain.carbob.application.exception.CarbobException;
import com.softeer.team6four.domain.carbob.application.response.CarbobImgUrl;
import com.softeer.team6four.global.response.ErrorCode;
import com.softeer.team6four.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarbobImgUploadService {
    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public ResponseDto<CarbobImgUrl> saveFile(MultipartFile multipartFile) {
        try {
            String originFilename = multipartFile.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            amazonS3.putObject(bucket, originFilename, multipartFile.getInputStream(), metadata);
            URL url = amazonS3.getUrl(bucket, originFilename);

            CarbobImgUrl carbobImgUrl = CarbobImgUrl.builder()
                    .imgUrl(url.toString())
                    .build();
            return ResponseDto.map(HttpStatus.OK.value(), "S3에 이미지 등록 후 링크 반환이 성공했습니다.", carbobImgUrl);
        } catch (IOException e) {
            throw new CarbobException(ErrorCode.METHOD_NOT_ALLOWED);
        }
    }
}
