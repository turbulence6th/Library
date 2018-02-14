package com.turbulence6th;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.mockito.Mockito;

import com.turbulence6th.annotation.Test;
import com.turbulence6th.listener.ApplicationListener;
import com.turbulence6th.repository.BookRepository;
import com.turbulence6th.repository.RepositoryFactory;
import com.turbulence6th.servlet.book.BookNewServletTest;
import com.turbulence6th.validator.BookValidatorTest;

public class TestMain {

	private static final Class<?>[] TEST_CLASSES = { BookValidatorTest.class, BookNewServletTest.class };

	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException, SQLException {

		initialize();

		testClassesFor: for (Class<?> clazz : TEST_CLASSES) {

			Object testObject = clazz.getConstructor().newInstance();
			for (Method testMethod : clazz.getDeclaredMethods()) {
				if (testMethod.isAnnotationPresent(Test.class)) {
					try {
						testMethod.invoke(testObject);
					} catch (InvocationTargetException ite) {
						if (ite.getCause() instanceof AssertException) {
							AssertException ae = (AssertException) ite.getCause();
							Test test = testMethod.getAnnotation(Test.class);
							System.out.println(test.value() + " -> " + ae.getMessage());
							break testClassesFor;
						}
						
						else {
							throw (RuntimeException) ite.getCause();
						}
					}
				}
			}

		}

		System.out.println("All tests have run successfully");
		
		terminate();

	}

	private static void initialize() throws ClassNotFoundException, SQLException  {

		databaseInitialize();
		
		listenerInitialize();
		
		repositoryInitialize();

	}
	
	private static void databaseInitialize() throws ClassNotFoundException, SQLException {
		Globals.testProperties = new Properties();

		try {
			Globals.testProperties.load(TestMain.class.getClassLoader().getResourceAsStream("test.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Class.forName("org.postgresql.Driver");

		Globals.connection = DriverManager.getConnection(Globals.testProperties.getProperty("jdbc.connection"),
				Globals.testProperties.getProperty("jdbc.username"), Globals.testProperties.getProperty("jdbc.password"));

		try (Statement statement = Globals.connection.createStatement()) {

			String sql = "DROP SCHEMA public CASCADE";
			System.out.println(sql);
			statement.execute(sql);

			sql = "CREATE SCHEMA public";
			System.out.println(sql);
			statement.execute("CREATE SCHEMA public");

		}

	}
	
	private static void listenerInitialize() {
		Globals.applicationListener = Mockito.spy(new ApplicationListener());
		Globals.context = Mockito.mock(ServletContext.class);
		
		Map<String, Object> map = new HashMap<>();
		Mockito.doAnswer(invocation -> {
			String key = invocation.getArgument(0);
	        Object value = invocation.getArgument(1);
	        if(key.equals("applicationProperties")) {
	        	map.put(key, Globals.testProperties);
	        	Field field = ApplicationListener.class.getDeclaredField("applicationProperties");
	        	field.setAccessible(true);
	        	field.set(Globals.applicationListener, Globals.testProperties);
	        }
	        
	        else {
	        	map.put(key, value);
	        }
	        
	        return null;
		}).when(Globals.context).setAttribute(Mockito.any(String.class), Mockito.any(Object.class));
		
		Mockito.doAnswer(invocation -> {
			String key = invocation.getArgument(0);
			return map.get(key);
		}).when(Globals.context).getAttribute(Mockito.any(String.class));
		
		Globals.contextEvent = Mockito.mock(ServletContextEvent.class);
		Mockito.when(Globals.contextEvent.getServletContext()).thenReturn(Globals.context);
		Globals.applicationListener.contextInitialized(Globals.contextEvent);
	}
	
	private static void repositoryInitialize() {
		RepositoryFactory repositoryFactory = new RepositoryFactory(Globals.connection);
		Globals.bookRepository = repositoryFactory.get(BookRepository.class);
	}

	private static void terminate() {
		try {
			Globals.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Globals.applicationListener.contextDestroyed(Globals.contextEvent);
	}

}
