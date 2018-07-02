package com.dds.ssjh.usermodule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.dds.ssjh.model.User;
import com.dds.ssjh.service.UserService;

@SuppressWarnings("serial")
@ManagedBean
public class UserBean extends AbstractBean {

	@Autowired
	private transient UserService userService;

	private User user = new User();

	private User edittingUser = new User();

	private List<User> users = new ArrayList<User>();

	public void load() {
		try {
			this.users = userService.listUser();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
		}
	}

	public String doLogin() throws IOException, ServletException {
		/*
		 * try { user = userService.login(user.getName(), user.getPassword()); if (user
		 * == null) { FacesContext.getCurrentInstance().addMessage(null, new
		 * FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
		 * "Username or password is invalid.")); } else {
		 * addCallbackParams("authenticated", true); } } catch (Exception e) {
		 * FacesContext.getCurrentInstance().addMessage(null, new
		 * FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage())); }
		 */
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
				.getRequestDispatcher("/j_spring_security_check");

		dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
		FacesContext.getCurrentInstance().responseComplete();
		return null;
	}

	public void editUser(User user) {
		this.edittingUser = user;
	}

	public void newUser() {
		this.edittingUser = new User();
	}

	public void saveUser() {
		this.userService.save(edittingUser);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getEdittingUser() {
		return edittingUser;
	}

	public void setEdittingUser(User edittingUser) {
		this.edittingUser = edittingUser;
	}
}
