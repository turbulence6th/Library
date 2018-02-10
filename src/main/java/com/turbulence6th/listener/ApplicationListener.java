package com.turbulence6th.listener;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.google.gson.Gson;
import com.turbulence6th.repository.BookRepository;
import com.turbulence6th.repository.MigrationRepository;

@WebListener
public class ApplicationListener implements ServletContextListener {
	
	private Properties applicationProperties;
	
	private Connection connection;

	public void contextDestroyed(ServletContextEvent event) {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();

		this.applicationProperties = new Properties();
		
		try {
			this.applicationProperties
					.load(ApplicationListener.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		context.setAttribute("applicationProperties", this.applicationProperties);
		
		this.initializeConnection();
		
		context.setAttribute("connection", this.connection);
		
		MigrationRepository migrationRepository = new MigrationRepository(connection);
		
		if(!migrationRepository.checkMigrationsTableExists()) {
			URL createMigrationsUrl = ApplicationListener.class.getClassLoader()
					.getResource("create_migrations.sql");
			migrationRepository.createMigrationsTable(createMigrationsUrl);
		}

		URL migrationUrl = ApplicationListener.class.getClassLoader().getResource("sql");
		migrationRepository.runMigrations(migrationUrl);
		
		context.setAttribute("bookRepository", new BookRepository(this.connection));

		context.setAttribute("gson", new Gson());
	}
	
	private void initializeConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			this.connection = DriverManager.getConnection(applicationProperties.getProperty("jdbc.connection"),
					applicationProperties.getProperty("jdbc.username"),
					applicationProperties.getProperty("jdbc.password"));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
