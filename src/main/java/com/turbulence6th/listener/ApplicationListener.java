package com.turbulence6th.listener;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.Session;

import com.google.gson.Gson;
import com.turbulence6th.repository.BookRepository;
import com.turbulence6th.repository.MigrationRepository;
import com.turbulence6th.repository.RepositoryFactory;

@WebListener
public class ApplicationListener implements ServletContextListener {
	
	private Properties applicationProperties;
	
	private Connection connection;

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
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
		
		RepositoryFactory repositoryFactory = new RepositoryFactory(this.connection);
		context.setAttribute("bookRepository", repositoryFactory.get(BookRepository.class));

		context.setAttribute("gson", new Gson());
		
		Map<String, Set<Session>> webSocketSessionMap = Collections.synchronizedMap(new HashMap<>());
		webSocketSessionMap.put("/books", Collections.synchronizedSet(new HashSet<>()));
		context.setAttribute("webSocketSessionMap", webSocketSessionMap);
	}
	
	private void initializeConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			
			String databaseUrl = System.getenv("DATABASE_URL");
			if(databaseUrl != null) {
				URI dbUri = new URI(databaseUrl);

				String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
			    String username = dbUri.getUserInfo().split(":")[0];
			    String password = dbUri.getUserInfo().split(":")[1];
			    
			    this.connection = DriverManager.getConnection(dbUrl, username, password);
			}
			
			else {
				this.connection = DriverManager.getConnection(applicationProperties.getProperty("jdbc.connection"),
						applicationProperties.getProperty("jdbc.username"),
						applicationProperties.getProperty("jdbc.password"));
			}
			
		} catch (ClassNotFoundException | SQLException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

}
