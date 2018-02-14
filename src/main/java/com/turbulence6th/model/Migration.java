package com.turbulence6th.model;

import java.nio.file.Path;

public class Migration implements Comparable<Migration> {

	private Long id;
	
	private String name;
	
	private Path path;
	
	public Migration(Long id, String name, Path path) {
		this.id = id;
		this.name = name;
		this.path = path;
	}
	
	@Override
	public int compareTo(Migration migration) {
		return (int) (this.id - migration.id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
	
}
