package com.dds.ssjh.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dds.ssjh.model.User;
import com.dds.ssjh.util.Database;
import com.dds.ssjh.util.OracleSqlBuilder;
import com.dds.ssjh.util.SqlBuilder;

@Service
public class UserServiceImpl implements UserService {

	@Resource(name = "database")
	private Database database;

	@Override
	public User loadById(Serializable id) {
		return database.loadById(User.class, id);
	}

	@Override
	public User loadByName(String name) {
		return database.loadBy(User.class, "name", name);
	}

	@Override
	public User login(String name, String password) {
		User user = database.loadBy(User.class, "name", name);
		if (user != null) {
			if (user.getPassword().equals(password)) {
				return user;
			} else {
				throw new UsernameNotFoundException("Username or password is invalid!");
			}
		} else {
			throw new UsernameNotFoundException("Username or password is invalid!");
		}
	}

	@Override
	public List<User> listUser() {
		SqlBuilder builder = new OracleSqlBuilder();
		builder.from(User.class, "u");
		return database.listBy(builder, User.class);
	}

	@Override
	public void save(User user) {
		if (user.getId() == null) {
			database.add(user);
		} else {
			database.update(user);
		}
	}

}
