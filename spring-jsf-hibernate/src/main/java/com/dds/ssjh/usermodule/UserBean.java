package com.dds.ssjh.usermodule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import com.dds.ssjh.model.PaginatedResult;
import com.dds.ssjh.model.User;
import com.dds.ssjh.service.UserService;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class UserBean extends AbstractBean {

	@Autowired
	private transient UserService userService;

	private User edittingUser = new User();

	private List<User> users = new ArrayList<User>();

	private LazyDataModel<User> userLazyModel = null;

	public void load() {
		try {
			setUserLazyModel(new LazyDataModel<User>() {
				@Override
				public List<User> load(int first, int pageSize, String sortField, SortOrder sortOrder,
						Map<String, Object> filters) {
					PaginatedResult<User> r = userService.listPaginated(pageSize, first / pageSize);
					setPageSize(pageSize);
					setRowCount(r.getTotal());
					return r.getData();
				}
			});

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
		}
	}

	public void doLogin() throws IOException, ServletException {
		doLoginAndLogout("j_spring_security_check");
	}

	public void logout() {
		doLoginAndLogout("j_spring_security_logout");
	}

	private void doLoginAndLogout(String processHandler) {
		try {
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
					.getRequestDispatcher("/" + processHandler);
			dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage()));
		}
	}

	public void editUser(User user) {
		this.edittingUser = user;
	}

	public void newUser() {
		this.edittingUser = new User();
	}

	public void saveUser() {
		try {
			if (this.edittingUser.getId() == null) {
				this.userService.save(edittingUser);
			} else {
				this.userService.update(edittingUser);
			}
			this.users = this.userService.listUser();
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage()));
		}
	}

	public void remove(User user) {
		try {
			this.userService.remove(user);
			this.users = this.userService.listUser();
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage()));
		}
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

	public LazyDataModel<User> getUserLazyModel() {
		return userLazyModel;
	}

	public void setUserLazyModel(LazyDataModel<User> userLazyModel) {
		this.userLazyModel = userLazyModel;
	}
}
