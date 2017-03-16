package kr.co.easymanual.service;

import java.io.File;

import kr.co.easymanual.task.Index;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class CronManager {
	@Autowired
	private ThreadPoolTaskExecutor taskExcutor;

	@Autowired
	private String attachmentsDirectory;

	public void retryIndex() {
		File file = new File(this.attachmentsDirectory);
		File[] files = file.listFiles();
		long ctime = System.currentTimeMillis();

		for(File f: files) {
			String str = f.toString();
			long mtime = f.lastModified();
			long elapsedTime = ctime - mtime;

			// 24시간 (86400000) 이 지났고 확장자가 .index 인 파일에 대해 실시간 인덱스 실패로 간주하여 다시 인덱스 작업 수행한다.
			// 하나의 파일에 대한 인덱스 작업은 24 시간 이내에 종료된다는 가정이 있다. 이 가정이 깨질경우 서로 다른 2개의 스레드가 하나의 파일에 대해 동시에 인덱스 작업을 수행하게 된다.
			if(elapsedTime > 86400000 && StringUtils.endsWithIgnoreCase(str, ".index")) {
				this.taskExcutor.execute(new Index(f));
				//System.out.println(this.taskExcutor.toString() + ":" + this.attachmentsDirectory + ":" +  str + ":" + elapsedTime);
			}
		}
	}
}
