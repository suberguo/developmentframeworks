package com.dds.ssjh.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Entity;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Component;

import com.dds.ssjh.model.PaginatedResult;

@Component("database")
public class OracleDatabase implements Database {

	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T loadById(Class<?> className, Serializable id) {
		return (T) sessionFactory.getCurrentSession().load(className, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T loadBy(Class<?> className, String propertyName, Object value) {
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(className);
		cr.add(Restrictions.eq(propertyName, value));
		
		@SuppressWarnings("rawtypes")
		List r = cr.list();
		if (r.size() > 0) {
			
			return (T) r.get(0);
		} else {
			return null;
		}
	}

	@Override
	public <T> List<T> listBy(Class<?> clz) {
		SqlBuilder builder = new OracleSqlBuilder();
		builder.from(clz, "");
		return listBy(builder, clz);
	}

	@Override
	public <T> void add(T object) {
		sessionFactory.getCurrentSession().save(object);
	}

	@Override
	public <T> void remove(T object) {
		sessionFactory.getCurrentSession().delete(object);
		sessionFactory.getCurrentSession().flush();
	}

	@Override
	public <T> void update(T object) {
		sessionFactory.getCurrentSession().update(object);
		sessionFactory.getCurrentSession().flush();

	}

	private int countRows(Class<?> clz) {
		if (clz.isAnnotationPresent(Entity.class)) {
			return (int) sessionFactory.getCurrentSession().createCriteria(clz).setProjection(Projections.rowCount())
					.list().get(0);
		} else {
			throw new RuntimeException("Object haven't include annotation!");
		}
	}

	private int countRows(SqlBuilder sqlBuilder) {
		String sql = sqlBuilder.toString();
		sql = "SELECT COUNT(1) FROM (" + sql + ") ";
		SQLQuery q = sessionFactory.getCurrentSession().createSQLQuery(sql);
		List<Object> parameters = sqlBuilder.parameters();
		for (int i = 0; i < parameters.size(); i++) {
			q.setParameter(i, parameters.get(i));
		}
		return (int) q.list().get(0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> listBy(SqlBuilder sqlBuilder, Class<?> clz) {
		String sql = sqlBuilder.toString();
		SQLQuery q = sessionFactory.getCurrentSession().createSQLQuery(sql);
		q.setResultTransformer(new AliasToBeanResultTransformer(clz));
		HashMap<String, String> proMap = sqlBuilder.properties();
		// HashMap<String, Type> typeMap = sqlBuilder.types();
		Set<String> keySet = proMap.keySet();
		Iterator<String> iteratorKey = keySet.iterator();
		while (iteratorKey.hasNext()) {
			String alias = iteratorKey.next();
			// String column = proMap.get(alias);
			q.addScalar(alias);
		}

		List<Object> parameters = sqlBuilder.parameters();
		for (int i = 0; i < parameters.size(); i++) {
			q.setParameter(i, parameters.get(i));
		}
		List r = q.list();
		return (List<T>) r;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> PaginatedResult<T> listPaginated(Class<?> clz, int pageSize, int pageIndex) {
		PaginatedResult<T> r = new PaginatedResult<T>();
		r.setTotal(this.countRows(clz));
		r.setPageSize(pageSize);
		double d = Math.ceil(new Double(r.getTotal()) / r.getPageSize());
		r.setPageCount((int) d);
		SqlBuilder builder = new OracleSqlBuilder();
		builder.from(clz, "");
		SQLQuery q = sessionFactory.getCurrentSession().createSQLQuery(builder.toString());
		q.setResultTransformer(new AliasToBeanResultTransformer(clz));
		HashMap<String, String> proMap = builder.properties();
		// HashMap<String, Type> typeMap = builder.types();
		Set<String> keySet = proMap.keySet();
		Iterator<String> iteratorKey = keySet.iterator();
		while (iteratorKey.hasNext()) {
			String alias = iteratorKey.next();
			// String column = proMap.get(alias);
			q.addScalar(alias);
		}

		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageIndex * pageSize);
		
		r.setData((List<T>) q.list());
		
		return r;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> PaginatedResult<T> listPaginated(SqlBuilder sqlBuilder, Class<?> clz, int pageSize, int pageIndex) {
		PaginatedResult<T> r = new PaginatedResult<T>();
		r.setTotal(this.countRows(sqlBuilder));
		r.setPageSize(pageSize);
		double d = Math.ceil(new Double(r.getTotal()) / r.getPageSize());
		r.setPageCount((int) d);
		String sql = sqlBuilder.toString();
		SQLQuery q = sessionFactory.getCurrentSession().createSQLQuery(sql);
		q.setResultTransformer(new AliasToBeanResultTransformer(clz));
		HashMap<String, String> proMap = sqlBuilder.properties();
		// HashMap<String, Type> typeMap = builder.types();
		Set<String> keySet = proMap.keySet();
		Iterator<String> iteratorKey = keySet.iterator();
		while (iteratorKey.hasNext()) {
			String alias = iteratorKey.next();
			// String column = proMap.get(alias);
			q.addScalar(alias);
		}
		List<Object> parameters = sqlBuilder.parameters();
		for (int i = 0; i < parameters.size(); i++) {
			q.setParameter(i, parameters.get(i));
		}
		q.setFirstResult((pageIndex - 1) * pageSize);
		q.setMaxResults(pageIndex * pageSize);
		r.setData((List<T>) q.list());
		return r;
	}

}
