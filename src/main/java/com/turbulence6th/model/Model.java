package com.turbulence6th.model;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.turbulence6th.annotation.Column;

public abstract class Model {
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;
	
	private Map<String, List<String>> errors;
	
	@Override
	public String toString() {
		List<Field> fields = getAllFields(this.getClass());
		List<String> values = new LinkedList<>();
		for(Field field: fields) {
			if(field.isAnnotationPresent(Column.class)) {
				field.setAccessible(true);
				try {
					values.add(field.getName() + "=" + field.get(this));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		return this.getClass().getSimpleName() + ": [" + String.join(", ", values) + "]";
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
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	
	public Map<String, List<String>> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, List<String>> errors) {
		this.errors = errors;
	}
	
}
