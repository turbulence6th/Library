package com.turbulence6th.repository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

import com.turbulence6th.annotation.Query;

public interface R {

	@SuppressWarnings("unchecked")
	static <T extends R> T  get(Connection connection, Class<T> clazz) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, new InvocationHandler() {
			
			@Override
			public Object invoke(Object object, Method method, Object[] args) throws Throwable {
				Query query = method.getAnnotation(Query.class);
				return null;
			}
		});
	}
	
}
