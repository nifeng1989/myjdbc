package net.fengni.jdbc.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * User: fengni
 */

@Component
public class ApplicationContextUtil implements ApplicationContextAware {
	private static ApplicationContext context = null;

	private ApplicationContextUtil() {
	}

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

	public static ApplicationContextUtil getInstance() {
		return (ApplicationContextUtil)context.getBean("applicationContextUtil");
	}

    public static Object getBean(String name){
        return context.getBean(name);
    }

    public static Object getBean(Class classz){
        return context.getBean(classz);
    }
}
