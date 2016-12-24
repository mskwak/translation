package kr.co.easymanual.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringBean implements ApplicationContextAware {
	private static ApplicationContext context;

	private SpringBean() {}

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    // 누가 메소드 파라미터인 applicationContext에 ApplicationContext를 꽂아주나? 스프링이?
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static <T> T getBean(Class<T> requiredType) {
		return context.getBean(requiredType);
	}

	public static Object getBean(String name) {
		return context.getBean(name);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return context.getBean(name, requiredType);
	}
}
