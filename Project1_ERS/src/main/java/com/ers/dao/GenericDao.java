package com.ers.dao;

import java.util.List;

public interface GenericDao<T> { 
	List<T> getAll();
	void update(T entity);
	void insert(T entity);
	void delete(T entity);
}