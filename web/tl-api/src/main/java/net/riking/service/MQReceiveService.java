package net.riking.service;

public interface MQReceiveService {
	public void init(String queueName, Object mqListener);
}
