package com.softeer.team6four.global.infrastructure.sqs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SQSSender {

	private final SqsTemplate template;

	@Value("${application.amazon.sqs.queue-name}")
	private String queueName;

	public SendResult<String> sendMessage(String message) {
		System.out.println("Sender: " + message);
		return template.send(to -> to
			.queue(queueName)
			.payload(message));
	}

}
