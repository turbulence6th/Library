package com.turbulence6th.repository;

import com.turbulence6th.model.Model;

public interface Repository<T extends Model> {
	
	boolean create(T model);
		
	boolean update(T model);
	
	boolean delete(T model);
	
	T findBy(Object... params);
	
}
