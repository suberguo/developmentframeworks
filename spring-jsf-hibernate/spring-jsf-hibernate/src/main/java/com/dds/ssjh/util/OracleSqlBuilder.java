package com.dds.ssjh.util;

import java.lang.reflect.Field;
//import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.type.Type;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
public class OracleSqlBuilder implements SqlBuilder {

	private StringBuffer buffer = new StringBuffer();
	private HashMap<String, String> propertyMap = new HashMap<String, String>();
	private HashMap<String, Type> proTypeMap = new HashMap<String, Type>();
	private List<Object> listOfParameterValue = new ArrayList<Object>();

	@Override
	public String toString() {
		return buffer.toString();
	}

	@Override
	public SqlBuilder select() {
		buffer.append("SELECT ");
		return this;
	}

	@Override
	public SqlBuilder project(String columnName, String propertyName, String ext) {
		buffer.append(" " + columnName + " as " + propertyName + ext + " ");
		propertyMap.put(propertyName, columnName);
		return this;
	}

	@Override
	public SqlBuilder project(String part) {
		buffer.append(part);
		return this;
	}

	@Override
	public SqlBuilder from(String tableName) {
		buffer.append(" FROM " + tableName);
		return this;
	}

	@Override
	public SqlBuilder from(String tableName, String alias) {
		buffer.append(" FROM " + tableName + " " + alias);
		return this;
	}

	@Override
	public SqlBuilder where() {
		buffer.append(" WHERE ");
		return this;
	}

	@Override
	public SqlBuilder where(String... conditions) {
		if (conditions.length > 0) {
			buffer.append(" WHERE 1=1 ");
			for (int i = 0; i < conditions.length; i++) {
				buffer.append(" AND " + conditions[i]);
			}
		}
		return this;
	}

	@Override
	public SqlBuilder from(Class<?> clz, String alias) {
		if (clz.isAnnotationPresent(Entity.class)) {
			propertyMap.clear();
			proTypeMap.clear();
			Entity table = clz.getAnnotation(Entity.class);
			String tableName = table.name();
			Field[] arrayOfField = clz.getDeclaredFields();
			for (int i = 0; i < arrayOfField.length; i++) {
				if (arrayOfField[i].isAnnotationPresent(Column.class)) {
					Column col = arrayOfField[i].getAnnotation(Column.class);
					String colName = col.name();
					propertyMap.put(arrayOfField[i].getName(), colName);
					// proTypeMap.put(arrayOfField[i].getName(), Hibernate.STRING);
				}
			}

			if (propertyMap.size() > 0) {
				buffer.append("SELECT ");
				Set<String> keySet = propertyMap.keySet();
				Iterator<String> iteratorKey = keySet.iterator();
				while (iteratorKey.hasNext()) {
					String key = iteratorKey.next();
					buffer.append(
							alias + "." + propertyMap.get(key) + " AS " + key + (iteratorKey.hasNext() ? "," : ""));
				}
				buffer.append(" FROM " + tableName + " " + alias);
			}

		}
		return this;
	}

	@Override
	public SqlBuilder join(String tableName, String alias) {
		buffer.append(" JOIN " + tableName + " " + alias);
		return this;
	}

	@Override
	public SqlBuilder join(String part) {
		buffer.append(" JOIN " + part);
		return this;
	}

	@Override
	public SqlBuilder on(String colunmName1, String columnName2) {
		buffer.append(" ON " + colunmName1 + "=" + columnName2);
		return this;
	}

	@Override
	public SqlBuilder on(String... column) {
		if (column.length % 2 == 0) {
			buffer.append(" ON ");
			for (int i = 0; i < column.length; i += 2) {
				buffer.append(" " + column[i] + "=" + column[i + 1] + " ");
			}
			return this;
		} else {
			throw new RuntimeException("Arguments is invalid!");
		}
	}

	@Override
	public SqlBuilder equal(String columnName, Object value) {
		if (value != null) {
			buffer.append(" " + columnName + " = ?");
			listOfParameterValue.add(value);
		} else {
			buffer.append(" " + columnName + " IS NULL ");
		}
		return this;
	}

	@Override
	public SqlBuilder like(String columnName, Object value) {
		if (value != null) {
			buffer.append(" " + columnName + " LIKE '%?%'");
			listOfParameterValue.add(value);
		}
		return this;
	}

	@Override
	public SqlBuilder condition(String columnName, String compare, Object value) {
		if (value != null) {
			buffer.append(" " + columnName + " " + compare + " ?");
			listOfParameterValue.add(value);
		} else {
			buffer.append(" " + columnName + " " + compare + " NULL");
		}

		return this;
	}

	@Override
	public SqlBuilder and() {
		buffer.append(" AND ");
		return this;
	}

	@Override
	public SqlBuilder or() {
		buffer.append(" OR ");
		return this;
	}

	@Override
	public SqlBuilder among(String columnName, Object from, Object to) {
		buffer.append(" " + columnName + " >= " + from + " AND " + columnName + " <= " + to);
		return this;
	}

	@Override
	public List<Object> parameters() {
		return listOfParameterValue;
	}

	@Override
	public HashMap<String, String> properties() {
		return propertyMap;
	}

	@Override
	public HashMap<String, Type> types() {
		return proTypeMap;
	}

}
