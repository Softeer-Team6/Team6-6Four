package com.softeer.team6four.global.infrastructure.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.softeer.team6four.global.exception.BusinessException;
import com.softeer.team6four.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Slf4j
@Component
@Service
@RequiredArgsConstructor
public class S3Service {
	private final AmazonS3 amazonS3;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${spring.cloud.aws.s3.defaultImgUrl}")
	private String defaultImageUrl;

	public String saveCarbobAndReturnImageUrl(MultipartFile multipartFile) {
		try {
			if (multipartFile == null || multipartFile.isEmpty()) {
				return defaultImageUrl;
			}

			String originFilename = multipartFile.getOriginalFilename();

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(multipartFile.getSize());
			metadata.setContentType(multipartFile.getContentType());

			InputStream inputStream = multipartFile.getInputStream();

			amazonS3.putObject(bucket, originFilename, multipartFile.getInputStream(), metadata);

			URL url = amazonS3.getUrl(bucket, originFilename);
			if (url == null) {
				return defaultImageUrl;
			}

			return url.toString();
		} catch (IOException e) {
			throw new BusinessException(ErrorCode.METHOD_NOT_ALLOWED);
		}
	}


	public String saveCarbobQrAndReturnImageUrl(byte[] qrByteArray) {
		try {
			String fileName = "QR/" + UUID.randomUUID().toString() + ".png";
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(qrByteArray.length);
			metadata.setContentType("image/png");

			InputStream byteArrayInputStream = new ByteArrayInputStream(qrByteArray);

			amazonS3.putObject(bucket, fileName, new ByteArrayInputStream(qrByteArray), metadata);

			URL url = amazonS3.getUrl(bucket, fileName);
			return url.toString();
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BusinessException(ErrorCode.METHOD_NOT_ALLOWED);
		}
	}

}
