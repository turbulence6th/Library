package com.turbulence6th;

import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.turbulence6th.listener.ApplicationListener;
import com.turbulence6th.repository.BookRepository;

public class Globals {

	public static Properties testProperties;
	
	public static Connection connection;
	
	public static ApplicationListener applicationListener;
	
	public static ServletContext context;
	
	public static ServletContextEvent contextEvent;
	
	public static BookRepository bookRepository;
	
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	
}
