package com.turbulence6th.repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MigrationRepository {

	private Connection connection;

	public MigrationRepository(Connection connection) {
		this.connection = connection;
	}
	
	public boolean checkMigrationsTableExists() {
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
	
	public void createMigrationsTable(URL createMigrationsUrl) {
		
		String sql = null;
		
		try {
			sql = new String(Files.readAllBytes(Paths.get(createMigrationsUrl.toURI())));
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
	
	public void runMigrations(URL url) {
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
		String sql = "SELECT 1 FROM migrations WHERE migration_id = ? LIMIT 1";
		
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
