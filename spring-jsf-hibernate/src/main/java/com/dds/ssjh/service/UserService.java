package com.dds.ssjh.service;

import java.io.Serializable;
import java.util.List;

import com.dds.ssjh.model.PaginatedResult;
import com.dds.ssjh.model.User;

public interface UserService {

	User loadById(Serializable id);

	User loadByName(String name);

	User login(String name, String password);

	List<User> listUser();

	PaginatedResult<User> listPaginated(int pageSize, int pageIndex);

	void save(User user);

	void remove(User user);

	void update(User user);

}
