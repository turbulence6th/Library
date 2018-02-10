package com.turbulence6th.listener;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.turbulence6th.repository.BookRepository;

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
		
		if(!this.checkMigrationsTableExists()) {
			this.createMigrationsTable();
		}

		this.runMigrations();
		
		context.setAttribute("bookRepository", new BookRepository(this.connection));

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
	
	private boolean checkMigrationsTableExists() {
		String sql = "SELECT 1 FROM information_schema.tables" + 
						" WHERE table_schema = 'public'" + 
						" AND table_name = 'migrations'";
		try(Statement statement = this.connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql)) {
			
			System.out.println(sql);
			
			while(resultSet.next()) {
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private void createMigrationsTable() {
		
		String sql = null;
		
		try {
			sql = new String(Files.readAllBytes(Paths.get(ApplicationListener.class.getClassLoader().getResource("create_migrations.sql").toURI())));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		
		try(Statement statement = this.connection.createStatement()) {
			
			System.out.println(sql);
			
			statement.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void runMigrations() {
		URL url = ApplicationListener.class.getClassLoader().getResource("sql");
		try {
			DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(url.toURI()));
			for(Path path: directoryStream) {
				String filename = path.getFileName().toString();
				int underscoreIndex = filename.lastIndexOf('_');
				int dotIndex = filename.lastIndexOf('.');
				String name = filename.substring(0, underscoreIndex);
				int migrationId = Integer.parseInt(filename.substring(underscoreIndex + 1, dotIndex));
				
				if(!checkMigrationExists(migrationId)) {
					String[] migrations = new String(Files.readAllBytes(path)).trim().split(";");
					boolean success = true;
					for(String sql: migrations) {
						success = success && migrate(sql);
					}
					
					if(success) {
						registerMigration(migrationId, name);
					}
				}
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkMigrationExists(int migrationId) {
		String sql = "SELECT 1 FROM migrations WHERE migration_id = ? limit 1";
		
		try(PreparedStatement statement = this.connection.prepareStatement(sql)) {
			
			statement.setInt(1, migrationId);
			
			System.out.println(statement);
			
			try(ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					return true;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private boolean migrate(String sql) {
		try(Statement statement = this.connection.createStatement()) {
			
			System.out.println(sql);
			
			statement.executeUpdate(sql);
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private void registerMigration(int migrationId, String name) {
		String sql = "INSERT INTO migrations (migration_id, name) VALUES (?, ?)";
		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			
			statement.setInt(1, migrationId);
			statement.setString(2, name);
			
			System.out.println(statement);
			
			statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
