package com.dds.ssjh.util;

import java.lang.reflect.Field;

import javax.annotation.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import net.jawr.web.util.StringUtils;

public class BeanUtil {

	private static BeanFactory beanFactory;

	public static BeanFactory getBeanFactory() {
		if (beanFactory == null) {
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			ServletContext servletCxt = (ServletContext) extContext.getContext();
			if (servletCxt == null)
				return null;
			beanFactory = WebApplicationContextUtils.getWebApplicationContext(servletCxt);
		}
		return beanFactory;
	}

	public static void inject(Object obj) {
		if (obj == null)
			return;

		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object bean = null;
			boolean canWrite = false;
			if (field.isAnnotationPresent(Resource.class)) {
				Resource r = field.getAnnotation(Resource.class);
				String name = r.name();
				if (StringUtils.isNotEmpty(name)) {
					canWrite = true;
					bean = BeanUtil.getBeanFactory().getBean(name, field.getType());
				}
			} else if (field.isAnnotationPresent(Autowired.class)) {
				canWrite = true;
				bean = BeanUtil.getBeanFactory().getBean(field.getType());
			}
			if (!canWrite) {
				continue;
			}
			try {
				field.setAccessible(true);
				field.set(obj, bean);
				field.setAccessible(false);
			} catch (Exception e) {
				ReflectionUtils.handleReflectionException(e);
			}
		}
	}
}
