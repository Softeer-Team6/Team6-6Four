package com.softeer.team6four.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SQSConfig {

	@Value("${spring.cloud.aws.region.static}")
	private String region;

	@Value("${spring.cloud.aws.credentials.accessKey}")
	private String accessKey;

	@Value("${spring.cloud.aws.credentials.secretKey}")
	private String secretKey;

	@Bean
	public SqsAsyncClient sqsAsyncClient() {
		return SqsAsyncClient.builder()
			.credentialsProvider(() -> new AwsCredentials() {
				@Override
				public String accessKeyId() {
					return accessKey;
				}
				@Override
				public String secretAccessKey() {
					return secretKey;
				}
			})
			.region(Region.of(region))
			.build();
	}

	@Bean
	public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
		return SqsMessageListenerContainerFactory
			.builder()
			.sqsAsyncClient(sqsAsyncClient())
			.build();
	}

	@Bean
	public SqsTemplate sqsTemplate() {
		return SqsTemplate.newTemplate(sqsAsyncClient());
	}
}
