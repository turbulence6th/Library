package com.turbulence6th.repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.turbulence6th.annotation.Column;
import com.turbulence6th.annotation.Id;
import com.turbulence6th.annotation.Query;
import com.turbulence6th.annotation.Table;
import com.turbulence6th.model.Model;

public class RepositoryFactory {

	private Connection connection;

	public RepositoryFactory(Connection connection) {
		this.connection = connection;
	}

	@SuppressWarnings("unchecked")
	public <T extends Repository<?>> T get(Class<T> clazz) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, new InvocationHandler() {

			@SuppressWarnings("rawtypes")
			@Override
			public Object invoke(Object object, Method method, Object[] args) throws Throwable {

				if (method.getName().equals("create")) {
					return create((Model) args[0]);
				}

				else if (method.getName().equals("update")) {
					return update((Model) args[0]);
				}

				else if (method.getName().equals("delete")) {
					return delete((Model) args[0]);
				}
				
				else if(method.getName().equals("findBy")) {
					
					Object[] params = (Object[]) args[0];
					
					if(params.length % 2 != 0) {
						throw new IllegalArgumentException();
					}
					
					Class<?> returnClazz = clazz.getMethod("findBy", Object[].class).getReturnType();
				
					String tableName = returnClazz.getAnnotation(Table.class).name();
					String sql = "SELECT * from %s WHERE %s";
					
					List<String> parameters = new ArrayList<>();
					for(int i = 0; i < params.length; i = i + 2) {
						parameters.add(params[i] + " = ?");
					}
					
					sql = String.format(sql, tableName, String.join(", ", parameters));
					try(PreparedStatement statement = connection.prepareStatement(sql)) {
						for(int i = 0; i < parameters.size(); i++) {
							statement.setObject(i + 1, params[i * 2 + 1]);
						}
						
						System.out.println(statement);
						
						try(ResultSet resultSet = statement.executeQuery()) {
							if(resultSet.next()) {
								Object returnObject = returnClazz.getConstructor().newInstance();
								for(Field field: getAllFields(returnClazz)) {
									field.setAccessible(true);
									if (field.isAnnotationPresent(Column.class)) {
										Column column = field.getAnnotation(Column.class);
										if (field.getType().equals(LocalDateTime.class) || field.getType().equals(LocalDate.class)
												|| field.getType().equals(LocalTime.class)) {
											field.set(returnObject, resultSet.getObject(column.name(), field.getType()));
										}

										else {
											field.set(returnObject, resultSet.getObject(column.name()));
										}

									}
								}
								
								return returnObject;
							}
						}
					}
					
				}

				String sql = method.getAnnotation(Query.class).value();

				try (PreparedStatement statement = connection.prepareStatement(sql)) {

					if (args != null) {
						for (int i = 0; i < args.length; i++) {
							statement.setObject(i + 1, args[i]);
						}
					}

					System.out.println(statement);

					Class<?> returnClazz = method.getReturnType();

					if (returnClazz.isInterface()) {
						Collection collection = null;
						Class<?> genericClazz = (Class<?>) ((ParameterizedType) method.getGenericReturnType())
								.getActualTypeArguments()[0];
						if (returnClazz.isAssignableFrom(List.class)) {
							collection = new ArrayList();
							fillCollection(collection, statement, genericClazz);
						}

						else if (returnClazz.isAssignableFrom(Set.class)) {
							collection = new HashSet();
							fillCollection(collection, statement, genericClazz);
						}

						else if (returnClazz.isAssignableFrom(Deque.class)) {
							collection = new ArrayDeque();
							fillCollection(collection, statement, genericClazz);
						}

						else if (returnClazz.isAssignableFrom(Queue.class)) {
							collection = new LinkedList();
							fillCollection(collection, statement, genericClazz);
						}

						return collection;
					}

					else {
						Object returnObject = returnClazz.getConstructor().newInstance();
						if (returnObject instanceof Collection) {
							Collection collection = (Collection) returnObject;
							Class<?> genericClazz = (Class<?>) ((ParameterizedType) method.getGenericReturnType())
									.getActualTypeArguments()[0];
							fillCollection(collection, statement, genericClazz);
							return collection;
						}

						else {
							return getSingleObject(returnObject, statement, returnClazz);
						}
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}

				return null;
			}

		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void fillCollection(Collection collection, PreparedStatement statement, Class<?> genericClazz)
			throws IllegalArgumentException, IllegalAccessException, SQLException, InstantiationException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		
		try (ResultSet resultSet = statement.executeQuery()) {
			List<Field> fields = RepositoryFactory.getAllFields(genericClazz);
			while (resultSet.next()) {
				Object genericObject = genericClazz.getConstructor().newInstance();
				for (Field field : fields) {
					field.setAccessible(true);
					if (field.isAnnotationPresent(Column.class)) {
						Column column = field.getAnnotation(Column.class);
						if (field.getType().equals(LocalDateTime.class) || field.getType().equals(LocalDate.class)
								|| field.getType().equals(LocalTime.class)) {
							field.set(genericObject, resultSet.getObject(column.name(), field.getType()));
						}

						else {
							field.set(genericObject, resultSet.getObject(column.name()));
						}

					}
				}

				collection.add(genericObject);
			}
		}
	}

	private Object getSingleObject(Object object, PreparedStatement statement, Class<?> returnClazz)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, SQLException {
		
		try (ResultSet resultSet = statement.executeQuery()) {
			List<Field> fields = RepositoryFactory.getAllFields(returnClazz);
			if (resultSet.next()) {
				for(Field field: fields) {
					field.setAccessible(true);
					if (field.isAnnotationPresent(Column.class)) {
						Column column = field.getAnnotation(Column.class);
						if (field.getType().equals(LocalDateTime.class) || field.getType().equals(LocalDate.class)
								|| field.getType().equals(LocalTime.class)) {
							field.set(object, resultSet.getObject(column.name(), field.getType()));
						}

						else {
							field.set(object, resultSet.getObject(column.name()));
						}

					}
				}
				
				return object;
			}
		}
		
		return null;
	}

	private <T extends Model> boolean create(T model) {

		Class<? extends Model> clazz = model.getClass();

		LocalDateTime now = LocalDateTime.now();
		model.setCreatedAt(now);
		model.setModifiedAt(now);

		String tableName = clazz.getAnnotation(Table.class).name();

		List<String> columnNames = new ArrayList<>();

		List<Object> columnValues = new ArrayList<>();

		List<Field> fields = RepositoryFactory.getAllFields(clazz);

		for (Field field : fields) {
			field.setAccessible(true);
			if (field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class)) {
				columnNames.add(field.getAnnotation(Column.class).name());
				try {
					columnValues.add(field.get(model));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		List<String> questionMarks = Collections.nCopies(columnNames.size(), "?");

		String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, String.join(", ", columnNames),
				String.join(", ", questionMarks));

		try (PreparedStatement statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			for (int i = 0; i < columnValues.size(); i++) {
				statement.setObject(i + 1, columnValues.get(i));
			}

			System.out.println(statement);

			statement.execute();

			try (ResultSet resultSet = statement.getGeneratedKeys()) {
				if (resultSet.next()) {
					Field idField = fields.stream().filter(field -> field.isAnnotationPresent(Id.class)).findFirst()
							.get();
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

	private <T extends Model> boolean update(T model) {

		Class<? extends Model> clazz = model.getClass();

		LocalDateTime now = LocalDateTime.now();
		model.setModifiedAt(now);

		String tableName = clazz.getAnnotation(Table.class).name();

		List<String> columnNames = new ArrayList<>();

		List<Object> columnValues = new ArrayList<>();

		List<Field> fields = RepositoryFactory.getAllFields(clazz);

		for (Field field : fields) {
			field.setAccessible(true);
			if (field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class)) {
				columnNames.add(field.getAnnotation(Column.class).name());
				try {
					columnValues.add(field.get(model));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		Field idField = fields.stream().filter(field -> field.isAnnotationPresent(Id.class)).findFirst().get();

		String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?", tableName, String.join(" = ?, ", columnNames),
				idField.getAnnotation(Column.class).name());

		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {

			for (int i = 0; i < columnValues.size(); i++) {
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

	private <T extends Model> boolean delete(T model) {
		Class<? extends Model> clazz = model.getClass();

		String tableName = clazz.getAnnotation(Table.class).name();

		List<Field> fields = RepositoryFactory.getAllFields(clazz);

		Field idField = fields.stream().filter(field -> field.isAnnotationPresent(Id.class)).findFirst().get();
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

	private static List<Field> getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();

		do {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}

		while (clazz != null);

		return fields;
	}

}
