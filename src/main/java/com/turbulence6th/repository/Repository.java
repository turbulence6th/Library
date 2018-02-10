package com.turbulence6th.repository;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.turbulence6th.annotation.Column;
import com.turbulence6th.annotation.Id;
import com.turbulence6th.annotation.Table;
import com.turbulence6th.model.Model;

public abstract class Repository<T extends Model> {
	
	protected Connection connection;
	
	public Repository(Connection connection) {
		this.connection = connection;
	}

	public boolean create(T model) {
		
		Class<? extends Model> clazz = model.getClass();
		
		LocalDateTime now = LocalDateTime.now();
		model.setCreatedAt(now);
		model.setModifiedAt(now);
		
		String tableName = clazz.getAnnotation(Table.class).name();
		
		List<String> columnNames = new ArrayList<>();
		
		List<Object> columnValues = new ArrayList<>();
		
		List<Field> fields = Repository.getAllFields(clazz);
		
		for(Field field: fields) {
			field.setAccessible(true);
			if(field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class)) {
				columnNames.add(field.getAnnotation(Column.class).name());
				try {
					columnValues.add(field.get(model));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		List<String> questionMarks = Collections.nCopies(columnNames.size(), "?");
		
		String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName,
				String.join(", ", columnNames), String.join(", ", questionMarks));
		
		try (PreparedStatement statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			for(int i = 0; i < columnValues.size(); i++) {
				statement.setObject(i + 1, columnValues.get(i));
			}
			
			System.out.println(statement);
			
			statement.execute();
			
			try(ResultSet resultSet = statement.getGeneratedKeys()) {
				if(resultSet.next()) {
					Field idField = fields.stream()
							.filter(field -> field.isAnnotationPresent(Id.class))
							.findFirst().get();
					idField.set(model, resultSet.getObject(1));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
public boolean update(T model) {
		
		Class<? extends Model> clazz = model.getClass();
		
		LocalDateTime now = LocalDateTime.now();
		model.setModifiedAt(now);
		
		String tableName = clazz.getAnnotation(Table.class).name();
		
		List<String> columnNames = new ArrayList<>();
		
		List<Object> columnValues = new ArrayList<>();
		
		List<Field> fields = Repository.getAllFields(clazz);
		
		for(Field field: fields) {
			field.setAccessible(true);
			if(field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class)) {
				columnNames.add(field.getAnnotation(Column.class).name());
				try {
					columnValues.add(field.get(model));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		Field idField = fields.stream().filter(field -> field.isAnnotationPresent(Id.class))
				.findFirst().get();
		
		String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?", tableName,
				String.join(" = ?, ", columnNames), idField.getAnnotation(Column.class).name());
		
		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			
			for(int i = 0; i < columnValues.size(); i++) {
				statement.setObject(i + 1, columnValues.get(i));
			}
			
			try {
				statement.setObject(columnValues.size() + 1, idField.get(model));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
			System.out.println(statement);
			
			statement.execute();
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public boolean delete(T model) {
		Class<? extends Model> clazz = model.getClass();
		
		String tableName = clazz.getAnnotation(Table.class).name();
		
		List<Field> fields = Repository.getAllFields(clazz);
		
		Field idField = fields.stream().filter(field -> field.isAnnotationPresent(Id.class))
				.findFirst().get();
		idField.setAccessible(true);
		
		String sql = String.format("DELETE FROM %s WHERE %s = ?", tableName,
				idField.getAnnotation(Column.class).name());
		
		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			
			try {
				statement.setObject(1, idField.get(model));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
			System.out.println(statement);
			
			statement.execute();
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	protected static List<Field> getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		
		do {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
	    
	    while (clazz != null);

	    return fields;
	}
	
}
