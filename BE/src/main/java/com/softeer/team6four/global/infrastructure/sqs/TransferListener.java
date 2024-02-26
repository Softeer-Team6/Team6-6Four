package com.softeer.team6four.global.infrastructure.sqs;

import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.annotation.SqsListener;

@Component
public class TransferListener {

	@SqsListener("${application.amazon.sqs.queue-name}")
	public void messageListener(String message) {
		System.out.println("Listener: " + message);
	}

}
