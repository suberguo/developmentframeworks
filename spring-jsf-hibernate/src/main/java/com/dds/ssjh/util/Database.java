package com.dds.ssjh.util;

import java.io.Serializable;
import java.util.List;

import com.dds.ssjh.model.PaginatedResult;

public interface Database {

	<T> T loadById(Class<?> className, Serializable id);

	<T> T loadBy(Class<?> className, String attribute, Object value);

	<T> List<T> listBy(Class<?> clz);

	<T> List<T> listBy(SqlBuilder sqlBuilder, Class<?> clz);

	<T> PaginatedResult<T> listPaginated(Class<?> clz, int pageSize, int pageIndex);

	<T> PaginatedResult<T> listPaginated(SqlBuilder sqlBuilder, Class<?> clz, int pageSize, int pageIndex);

	<T> void add(T object);

	<T> void remove(T object);

	<T> void update(T object);

}
