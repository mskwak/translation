package kr.co.easymanual.config;

import kr.co.easymanual.service.CronManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

//http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#scheduling-enable-annotation-support
@Configuration
@EnableAsync
@EnableScheduling
public class CronConfig {

	@Autowired
	private CronManager cronManager;

	// http://kanetami.tistory.com/entry/Schedule-Spring-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8A%A4%EC%BC%80%EC%A5%B4-%EC%84%A4%EC%A0%95%EB%B2%95-CronTab
	// 초, 분, 시, 날, 달, 요일
	@Scheduled(cron = "0 5 0 * * ?")
	//@Scheduled(fixedDelay=50000)
	public void doSomething() {
		this.cronManager.retryIndex();
	}
}
