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
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

import com.turbulence6th.model.Migration;

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
			Set<Migration> migrations = new TreeSet<>();
			for(Path path: directoryStream) {
				String filename = path.getFileName().toString();
				int underscoreIndex = filename.indexOf('_');
				int dotIndex = filename.lastIndexOf('.');
				Long migrationId = Long.valueOf(filename.substring(0, underscoreIndex));
				String name = filename.substring(underscoreIndex + 1, dotIndex);
				
				if(!checkMigrationExists(migrationId)) {
					migrations.add(new Migration(migrationId, name, path));
				}
			}
			
			for(Migration migration: migrations) {
				String[] subMigrations = new String(Files.readAllBytes(migration.getPath())).trim().split(";");
				boolean success = true;
				for(String sql: subMigrations) {
					success = success && migrate(sql);
				}
				
				if(success) {
					registerMigration(migration);
				}
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkMigrationExists(Long migrationId) {
		String sql = "SELECT 1 FROM migrations WHERE migration_id = ? LIMIT 1";
		
		try(PreparedStatement statement = this.connection.prepareStatement(sql)) {
			
			statement.setLong(1, migrationId);
			
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
	
	private void registerMigration(Migration migration) {
		String sql = "INSERT INTO migrations (migration_id, name, created_at) VALUES (?, ?, ?)";
		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			
			statement.setLong(1, migration.getId());
			statement.setString(2, migration.getName());
			statement.setObject(3, LocalDateTime.now());
			
			System.out.println(statement);
			
			statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
