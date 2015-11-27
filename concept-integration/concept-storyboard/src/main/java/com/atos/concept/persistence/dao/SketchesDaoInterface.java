package com.atos.concept.persistence.dao;

import java.io.Serializable;
import java.util.List;

public interface SketchesDaoInterface<T, Id extends Serializable> {

	public void persist(T entity);

	public void update(T entity);

	public T findById(Id id);

	public void delete(T entity);

    public List<T> findByEntity(T entity);
    
}
