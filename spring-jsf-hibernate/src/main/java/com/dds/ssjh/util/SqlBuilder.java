package com.dds.ssjh.util;

import java.util.HashMap;
import java.util.List;

import org.hibernate.type.Type;

public interface SqlBuilder {

	SqlBuilder select();

	SqlBuilder project(String columnName, String alias, String fragment);

	SqlBuilder project(String part);

	SqlBuilder from(String tableName);

	SqlBuilder from(String tableName, String alias);

	SqlBuilder from(Class<?> clz, String alias);

	SqlBuilder join(String tableName, String alias);

	SqlBuilder join(String part);

	SqlBuilder on(String colunmName1, String columnName2);

	SqlBuilder on(String... colunm);

	SqlBuilder where();

	SqlBuilder where(String... conditions);

	SqlBuilder equal(String columnName, Object value);

	SqlBuilder like(String columnName, Object value);

	SqlBuilder condition(String columnName, String compare, Object value);

	SqlBuilder and();

	SqlBuilder or();

	SqlBuilder among(String columnName, Object from, Object to);

	SqlBuilder order(String orderby, boolean ascending);

	List<Object> parameters();

	HashMap<String, String> properties();

	HashMap<String, Type> types();

}
