package net.riking.service;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service("asyncService")
public class AsyncServiceImpl {

	@Async
	public Future<String> sendA() throws Exception {
		System.out.println("send A");
		Long startTime = System.currentTimeMillis();
		Thread.sleep(2000);
		Long endTime = System.currentTimeMillis();
		System.out.println("耗时：" + (endTime - startTime));
		return new AsyncResult<String>("success");
	}

}
