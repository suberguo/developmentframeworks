package com.dds.ssjh.service;

import java.io.Serializable;
import java.util.List;

import com.dds.ssjh.model.User;

public interface UserService {

	User loadById(Serializable id);

	User loadByName(String name);

	User login(String name, String password);
	
	List<User> listUser();
	
	void save(User user);

}
